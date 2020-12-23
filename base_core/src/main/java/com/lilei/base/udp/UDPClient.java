package com.lilei.base.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/*
 * 客户端
 */

public class UDPClient {
    static int num = 0;
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 35; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    start();
                }
            };
            new Thread(runnable).start();
        }
    }

    private static void start() {
        Random r = new Random(1);
        while (true) {
            try {
                num++;
                Thread.sleep(100);
                short id = (short) 0x0102;
                String str = "$GPGGA,092204.999,4250.5589,S,14718.5084,E,1,"+r.nextInt(100)+",24.4,19.7,M,,,,0000*1F\n";
                byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
                int len = bytes.length;
                ByteBuffer byteBuffer = ByteBuffer.allocate(32 + len);
                byteBuffer.putShort(id);
                byteBuffer.putShort((short) len);
                byteBuffer.position(32);
                byteBuffer.put(bytes);
                byte[] array = byteBuffer.array();
                sendBroadcast(array, 50250);
                System.out.println(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public static byte[] send(byte[] data) throws IOException {
        /*
         * 向服务器端发送数据
         */
        // 1.定义服务器的地址、端口号、数据
        InetAddress address = InetAddress.getByName("192.168.1.252");
        int port = 50250;

        // 2.创建数据报，包含发送的数据信息
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.创建DatagramSocket对象
        DatagramSocket socket = new DatagramSocket();
        // 4.向服务器端发送数据报
        socket.send(packet);

        /*
         * 接收服务器端响应的数据
         */
        // 1.创建数据报，用于接收服务器端响应的数据
        byte[] data2 = new byte[4068];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        // 2.接收服务器响应的数据
        socket.receive(packet2);
        // 3.读取数据
        String reply = new String(data2, 0, packet2.getLength());
        System.out.println("我是客户端，服务器说：" + reply);
        // 4.关闭资源
        socket.close();
        return data2;
    }
}