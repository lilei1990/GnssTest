package com.example.demo.rxtx;

import com.example.demo.utils.HexUtil;
import com.example.demo.utils.LoggerUtil;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * @name: SerialPortUtil
 * @author: tuacy.
 * @date: 2019/6/26.
 * @version: 1.0
 * @Description: 串口工具类
 */
public class SerialPortUtil {

    /**
     * 获得系统可用的端口名称列表(COM0、COM1、COM2等等)
     *
     * @return List<String>可用端口名称列表
     */


    public static List<String> getSerialPortList() {

        List<String> systemPorts = new ArrayList<>();
        //获得系统可用的端口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();//获得端口的名字
            systemPorts.add(portName);
        }
        return systemPorts;
    }

    /**
     * 打开串口
     *
     * @param serialPortName 串口名称
     * @return SerialPort 串口对象
     * @throws NoSuchPortException               对应串口不存在
     * @throws PortInUseException                串口在使用中
     * @throws UnsupportedCommOperationException 不支持操作操作
     */
    public static SerialPort openSerialPort(String serialPortName)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName);
        return openSerialPort(parameter);
    }

    /**
     * 打开串口
     *
     * @param serialPortName 串口名称
     * @param baudRate       波特率
     * @return SerialPort 串口对象
     * @throws NoSuchPortException               对应串口不存在
     * @throws PortInUseException                串口在使用中
     * @throws UnsupportedCommOperationException 不支持操作操作
     */
    public static SerialPort openSerialPort(String serialPortName, int baudRate)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName, baudRate);
        return openSerialPort(parameter);
    }

    /**
     * 打开串口
     *
     * @param serialPortName 串口名称
     * @param baudRate       波特率
     * @param timeout        串口打开超时时间
     * @return SerialPort 串口对象
     * @throws NoSuchPortException               对应串口不存在
     * @throws PortInUseException                串口在使用中
     * @throws UnsupportedCommOperationException 不支持操作操作
     */
    public static SerialPort openSerialPort(String serialPortName, int baudRate, int timeout)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        SerialPortParameter parameter = new SerialPortParameter(serialPortName, baudRate);
        return openSerialPort(parameter, timeout);
    }

    /**
     * 打开串口
     *
     * @param parameter 串口参数
     * @return SerialPort 串口对象
     * @throws NoSuchPortException               对应串口不存在
     * @throws PortInUseException                串口在使用中
     * @throws UnsupportedCommOperationException 不支持操作操作
     */
    public static SerialPort openSerialPort(SerialPortParameter parameter)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        return openSerialPort(parameter, 2000);
    }

    /**
     * 打开串口
     *
     * @param parameter 串口参数
     * @param timeout   串口打开超时时间
     * @return SerialPort串口对象
     * @throws NoSuchPortException               对应串口不存在
     * @throws PortInUseException                串口在使用中
     * @throws UnsupportedCommOperationException 不支持操作操作
     */
    public static SerialPort openSerialPort(SerialPortParameter parameter, int timeout)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        //通过端口名称得到端口
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(parameter.getSerialPortName());
        //打开端口，（自定义名字，打开超时时间）
        CommPort commPort = null;

        commPort = portIdentifier.open(parameter.getSerialPortName(), timeout);

        //判断是不是串口
        if (commPort instanceof SerialPort) {
            SerialPort serialPort = (SerialPort) commPort;
            //设置串口参数（波特率，数据位8，停止位1，校验位无）
            serialPort.setSerialPortParams(parameter.getBaudRate(), parameter.getDataBits(), parameter.getStopBits(), parameter.getParity());
            System.out.println("开启串口成功，串口名称：" + parameter.getSerialPortName());
            serialPort.setDTR(false);
            serialPort.setRTS(false);
            return serialPort;
        } else {
            //是其他类型的端口
            throw new NoSuchPortException();
        }
    }


    /**
     * 关闭串口
     *
     * @param serialPort 要关闭的串口对象
     */
    public static void closeSerialPort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            System.out.println("关闭了串口：" + serialPort.getName());
        }
    }

    /**
     * 向串口发送数据
     *
     * @param serialPort 串口对象
     * @param data       发送的数据
     */
    public static void sendData(SerialPort serialPort, byte[] data) {
        OutputStream os = null;
        try {
            //获得串口的输出流
            os = serialPort.getOutputStream();
            os.write(data);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 要读取的串口
     * @return 读取的数据
     */
    public static byte[] readData(SerialPort serialPort) {
        InputStream is = null;
        byte[] bytes = new byte[0];
        try {
            //获得串口的输入流
            is = serialPort.getInputStream();
            //获得数据长度
            int bufflenth = is.available();
            while (bufflenth != 0) {
                //初始化byte数组
                bytes = new byte[bufflenth];
                is.read(bytes);
                bufflenth = is.available();
            }
            return bytes;

        } catch (IOException e) {
            LoggerUtil.LOGGER.debug("串口错误");
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static byte[] readDataRSSI(SerialPort serialPort) {
        try {
            InputStream inputStream = serialPort.getInputStream();
            // 通过输入流对象的available方法获取数组字节长度
            int available = inputStream.available();
            byte[] readBuffer = new byte[available];

            // 从线路上读取数据流
            int len = 0;
            while ((len = inputStream.read(readBuffer)) != -1) {
//
//                String data = new String(readBuffer, 0, len).trim();
//
//                System.out.println("data:" + HexUtil.toHexString(readBuffer));
//                System.out.println("dataHex:");// 读取后置空流对象
                inputStream.close();
                inputStream = null;
                break;
            }
            return readBuffer;
        } catch (IOException e) {

        }
        return null;
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 要读取的串口
     * @return 读取的数据
     */
    public static void readDataAlways(SerialPort serialPort) {
        serialPort.notifyOnBreakInterrupt(true);
        serialPort.notifyOnDataAvailable(true);
        InputStream inputStream = null;
        try {
            //获得串口的输入流
            inputStream = serialPort.getInputStream();
            //获得数据长度

            //4.从串口读入数据
            //定义用于缓存读入数据的数组
            byte[] cache = new byte[1024];
            //记录已经到达串口COM21且未被读取的数据的字节（Byte）数。
            int availableBytes = 0;

            //无限循环，每隔20毫秒对串口COM21进行一次扫描，检查是否有数据到达
            while (true) {
                //获取串口COM21收到的可用字节数
                availableBytes = inputStream.available();
                //如果可用字节数大于零则开始循环并获取数据
                while (availableBytes > 0) {
                    //从串口的输入流对象中读入数据并将数据存放到缓存数组中
                    inputStream.read(cache);
                    //将获取到的数据进行转码并输出
                    for (int j = 0; j < cache.length && j < availableBytes; j++) {
                        //因为COM11口发送的是使用byte数组表示的字符串，
                        //所以在此将接收到的每个字节的数据都强制装换为char对象即可，
                        //这是一个简单的编码转换，读者可以根据需要进行更加复杂的编码转换。
                        System.out.print((char) cache[j]);
                    }
                    System.out.println();
                    //更新循环条件
                    availableBytes = inputStream.available();
                }
                //让线程睡眠20毫秒
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给串口设置监听
     *
     * @param serialPort serialPort 要读取的串口
     * @param listener   SerialPortEventListener监听对象
     * @throws TooManyListenersException 监听对象太多
     */
    public static void setListenerToSerialPort(SerialPort serialPort, SerialPortEventListener listener) throws TooManyListenersException {

        //给串口添加事件监听
        serialPort.addEventListener(listener);

        //串口有数据监听
        serialPort.notifyOnDataAvailable(true);
        //中断事件监听
        serialPort.notifyOnBreakInterrupt(true);

    }

}


