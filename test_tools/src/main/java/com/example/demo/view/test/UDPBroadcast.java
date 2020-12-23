package com.example.demo.view.test;

/**
 * 作者 : lei
 * 时间 : 2020/12/11.
 * 邮箱 :416587959@qq.com
 * 描述 :广播监听
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPBroadcast {
    private final int port;

    public boolean isRun = false;
    private CallBacks listener;

    public static void main(String[] args) {

        UDPBroadcast broadcast = new UDPBroadcast(50250);
        broadcast.run();
    }

    public UDPBroadcast(int port) {
        this.port = port;
    }


    public UDPBroadcast run() {
        isRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receive();
                } catch (IOException e) {
                    isRun = false;
                    e.printStackTrace();
                }
            }
        }).start();
        return this;

    }

    public void stop() {
        isRun = false;
    }

    private void receive() throws IOException {
        isRun = true;


        DatagramSocket ds = new DatagramSocket(port);
        while (isRun) {
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//            System.out.println(packet.getAddress());
//            InetAddress address = packet.getAddress();
            ds.receive(packet);
//            String s = new String(packet.getData(), 0, packet.getLength());
//            System.out.println(packet.getAddress() + ":" + packet.getPort() + "    →    " + s);
            //                System.out.println(udp_msg.getStr());
            num++;
            if (listener != null) {
                listener.send(Pack.INSTANCE.unPack(buffer), packet.getAddress().getHostAddress());

            }
        }
    }

    public static void sendBroadcast(String msg, int port) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
                InetAddress.getByName("255.255.255.255"), port);
        ds.send(dp);
        ds.close();
    }

    public static void sendBroadcast(byte[] msg, int port) {
        try {
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(msg, msg.length,
                    InetAddress.getByName("255.255.255.255"), port);
            ds.send(dp);
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int num = 0;

    public static byte[] send(byte[] msg, int port) {
        try {
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp = new DatagramPacket(msg, msg.length,
                    InetAddress.getByName("192.168.1.252"), port);
            ds.send(dp);
            /*
             * 接收服务器端响应的数据
             */
            // 1.创建数据报，用于接收服务器端响应的数据
            byte[] data2 = new byte[2048];
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
            ds.setSoTimeout(20000);
            // 2.接收服务器响应的数据
            ds.receive(packet2);

            // 3.读取数据
//        String reply = new String(data2, 0, packet2.getLength());
            //                System.out.println(udp_msg.getStr());

            // 4.关闭资源
            ds.close();
            return data2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 给设置监听
     */
    public UDPBroadcast setListener(CallBacks list) {
        //给添加事件监听
        listener = list;
        return this;
    }


}
