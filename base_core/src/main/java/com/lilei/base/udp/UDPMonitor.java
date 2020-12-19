package com.lilei.base.udp;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者 : lei
 * 时间 : 2020/12/09.
 * 邮箱 :416587959@qq.com
 * 描述 : Udp 监听服务
 */
public class UDPMonitor {
    //单例
    private static UDPMonitor instance;
    //运行监听线程
    private Thread thread;
    //缓冲区长度
    private int buffSize = 2048;
    //端口号
    private int port = 8800;
    //数据监听
    private List<IDataListener> dataListenerList = new ArrayList<>();

    public static void main(String[] args) {
        Broadcast broadcast = new Broadcast(50250);
        new Thread(broadcast).start();
//        UDPMonitor.getInstance().runUDPMonitor();
    }

    /**
     * 单例
     *
     * @return
     */
    public static UDPMonitor getInstance() {
        if (instance == null) {
            instance = new UDPMonitor();
        }
        return instance;
    }



    /**
     * 停止运行
     */
    public void stop() {
        dataListenerList.clear();
        thread.interrupt();
    }
    /**
     * 开始运行
     */
    public void runUDPMonitor() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    /*
                     * 接收客户端发送的数据
                     */
                    // 1.创建服务器端DatagramSocket，指定端口
                    DatagramSocket socket = new DatagramSocket(port);
                    while (!isInterrupted()) {
                        // 2.创建数据报，用于接收客户端发送的数据
                        byte[] data = new byte[buffSize];// 创建字节数组，指定接收的数据包的大小
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        // 3.接收客户端发送的数据
                        System.out.println("****服务器端已经启动，等待客户端发送数据");

                        socket.receive(packet);// 此方法在接收到数据报之前会一直阻塞

                        System.out.println("我是服务器，客户端说：" + JSON.toJSON(packet));
                        // 4.读取数据
//                        String info = new String(data, 0, packet.getLength());
                        for (IDataListener iDataListener : dataListenerList) {
                            iDataListener.onReceived(data);
                        }
                    }
                    System.out.println("停止服务");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
//        thread.interrupt();
        thread.start();
    }

    /**
     * 添加数据监听
     */
    public void addDataListener(IDataListener listener) {
        if (dataListenerList.contains(listener) == false) {
            dataListenerList.add(listener);
        }
    }

    /**
     * 移除数据监听
     */
    public void removeDataListener(IDataListener listener) {
        if (dataListenerList.contains(listener)) {
            dataListenerList.remove(listener);
        }
    }

}

/**
 * 广播监听
 */
 class Broadcast extends Thread {
    private final int port;

    public Broadcast(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() throws IOException {
        byte[] buffer = new byte[65507];
        @SuppressWarnings("resource")
        DatagramSocket ds = new DatagramSocket(port);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) {
            ds.receive(packet);
            String s = new String(packet.getData(), 0, packet.getLength());
            System.out.println(packet.getAddress() + ":" + packet.getPort() + "    →    " + s);
//            new Tests().unPack(0x0101,buffer);
        }
    }

    public void send(String msg, int port) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                InetAddress.getByName("255.255.255.255"), port);
        ds.send(dp);
        ds.close();
    }
}
interface IDataListener {
    /**
     * 接收到数据
     *
     * @param buffer 数据
     */
    void onReceived(byte[] buffer);
}