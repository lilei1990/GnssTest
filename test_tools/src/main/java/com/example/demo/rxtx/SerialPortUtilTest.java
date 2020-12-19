package com.example.demo.rxtx;


import gnu.io.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * @name: SerialPortUtilTest
 * @author: tuacy.
 * @date: 2019/6/26.
 * @version: 1.0
 * @Description: 串口测试代码
 */
public class SerialPortUtilTest {

    public static final String SERIAL_PORT_NAME = "COM3";
    public static final int BAUD_RATE = 115200;
    public static boolean IS_STOP;
    public static byte[] DMBytes;
    public static byte countDM1;
    public static byte countTruble;

    /**
     * 测试获取串口列表
     */
    @Test
    public void getSystemPortList() {

        List<String> portList = SerialPortUtil.getSerialPortList();
        for (String s : portList) {
            System.out.println(s);
        }


    }


    /**
     * 测试串口3打开，读，写操作
     */
    @Test
    public void serialPortAction3() {
        try {
            final SerialPort serialPort = SerialPortUtil.openSerialPort(SERIAL_PORT_NAME, BAUD_RATE);


            //设置串口的listener


            SerialPortUtil.setListenerToSerialPort(serialPort, event -> {
                //数据通知
                if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    byte[] bytes = SerialPortUtil.readData(serialPort);
                    /*System.out.print("收到的数据长度：" + bytes.length+"-->");*/
                    System.out.println("收到的数据：" + new String(bytes));
                    //System.out.print( new String(bytes));

                    /*System.out.println("收到的数据长度：" + bytes.length);
                    System.out.println("收到的数据：" + ByteUtils.bytesToHexString(bytes));
                    System.out.println("收到的数据：" + ByteUtils.bytesToHexString(bytes));*/



                }
            });
            try {
                // sleep 一段时间保证线程可以执行完
                Thread.sleep(3 * 30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    private void packDM(byte[] bytes) {

    }


    /**
     * 测试串口3打开，读，写操作
     */
    @Test
    public void serialPortAction4() {
        try {
            final SerialPort serialPort = SerialPortUtil.openSerialPort(SERIAL_PORT_NAME, BAUD_RATE);

            SerialPortUtil.readDataAlways(serialPort);

        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试获取串口列表
     */
    @Test
    public void closeComTest() {

        IS_STOP = true;

    }


    @Test
    public void test2() {

        String hexValue = "103182397";
        BigDecimal value = new BigDecimal(hexValue);
        BigDecimal valueRule = new BigDecimal("0.125");

        BigDecimal multiply = value.multiply(valueRule);

        System.out.println(multiply.toString());
       // System.out.println(byteToInt2(bytes)*0.125);

    }
    @Test
    public void test3() {


    }

    @Test
    public void test4() {


    }



}

