package com.example.demo.view.test

import com.alibaba.fastjson.JSON
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.utils.ByteUtils
import com.example.demo.utils.HexUtil
import com.example.demo.utils.LoggerUtil
import com.example.demo.view.test.gnss.GnssConfig
import com.example.demo.view.test.gnss.GnssTestData
import gnu.io.SerialPort
import gnu.io.SerialPortEvent
import java.io.IOException
import java.nio.ByteBuffer

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object UdpUtlis {

    /**
     * 写单板id
     */
    fun writeBID(bid: String): Boolean {

        var str = "{\"bid\":${bid.toInt(16)}}"
        val send = UDPBroadcast.send(Pack.pack(0x0001, str), 50250)
        return parkRet(send)
    }

    /**
     * 写整机id
     */
    fun writeID(bid: String): Boolean {

        var str = "{\"bid\":${bid.toInt(16)}}"
        val send = UDPBroadcast.send(Pack.pack(0x0003, str), 50250)
        return parkRet(send)
    }

    /**
     * wifi+开关（0-关；1-开），例：{"wifi":0}
     */
    fun enableWifi(enable: Boolean): Boolean {
        val str = if (enable) {
            "{\"wifi\":1}"
        } else {
            "{\"wifi\":0}"
        }
        val send = UDPBroadcast.send(Pack.pack(0x0005, str), 50250)
        return parkRet(send)
    }

    /**
     * LORA配置请求	chan+信道，power+功率（0-低功率；1-高功率），例：{"chan":9,"power":0}
     */
    fun loraCfg(chan: Int): Boolean {
        val str = "{\"chan\":$chan}"
        val send = UDPBroadcast.send(Pack.pack(0x0007, str), 50250)
        return parkRet(send)
    }

    /**
     * LCD显示请求	lcd+显示值（0-白；1-黑），例：{“lcd":0}
     */
    fun showLcd(lcd: Int): Boolean {
        val str = "{\"lcd\":$lcd}"

        val send = UDPBroadcast.send(Pack.pack(0x000b, str), 50250)
        return parkRet(send)
    }

    /**
     * 4g1+开关（0-关；1-开）或4g2+开关，例：{"4g1":0}或{"4g2":0}
     */
    fun enable4G1(enable: Boolean): Boolean {
        val str = if (enable) {
            "{\"net4g1\":1}"
        } else {
            "{\"net4g1\":0}"
        }

        val send = UDPBroadcast.send(Pack.pack(0x000d, str), 50250)
        return parkRet(send)
    }

    /**
     * 4g1+开关（0-关；1-开）或4g2+开关，例：{"4g1":0}或{"4g2":0}
     */
    fun enable4G2(enable: Boolean): Boolean {
        val str = if (enable) {
            "{\"net4g2\":1}"
        } else {
            "{\"net4g2\":0}"
        }

        val send = UDPBroadcast.send(Pack.pack(0x000d, str), 50250)
        return parkRet(send)
    }


    /**
     * 0x0009	KEY清空请求	空
     */
    fun clearKey(): Boolean {
        val str = ""
        val send = UDPBroadcast.send(Pack.pack(0x0009, str), 50250)
        return parkRet(send)
    }

    fun parkRet(data2: ByteArray?): Boolean {
        data2?:return false
        val unPack = Pack.unPack(data2)
        val obj = JSON.parseObject(unPack.str)
        when (unPack.id.toInt()) {
            0x0002, 0x0006, 0x0008, 0x000c, 0x000e, 0x0009, 0x000c -> {
//                putTestInfo(obj.toJSONString())
                if (obj["ret"] == 0) { //成功
                    return true
                }
            }
        }
        return false
    }

    /**
     * 测试usb读写
     */
    fun testSerialPort(serialPort: SerialPort): Boolean {
        var retureFlag = false

        val user: ByteArray = "root\n".toByteArray()
        val password: ByteArray = "i501.1234\n".toByteArray()
        val file: ByteArray = "file /\n".toByteArray()
        val logout: ByteArray = "logout\n".toByteArray()
        var currentTimeMillis = System.currentTimeMillis()
        try {
            if (serialPort != null) {
                LoggerUtil.LOGGER.debug("[bhz] 打开端口");
                //发个换行符激活设备,防止有时候休眠,
                SerialPortUtil.sendData(serialPort, "\n".toByteArray())
                //设置串口的listener
                SerialPortUtil.setListenerToSerialPort(serialPort) { event: SerialPortEvent ->
                    //数据通知
                    if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {


                        try {
                            var bytes = SerialPortUtil.readData(serialPort)
                            val string = String(bytes)
                            var recv = ByteUtils.bytesToHexString(bytes)
                            //超过30秒任务失败
                            if (System.currentTimeMillis() - currentTimeMillis > 30000) {
                                LoggerUtil.LOGGER.debug("usb口检测不通过")
                                serialPort.removeEventListener()
                                //                                hd100Case.add(GNSS_Case(1, "usb口检测","超时","不通过"))
//                                stop(0)
                            }
//                                if (System.currentTimeMillis() - currentTimeMillis > 3000) {
//                                    currentTimeMillis = System.currentTimeMillis()
//                                    SerialPortUtil.sendData(serialPort, "\n".toByteArray())
//                                    LoggerUtil.LOGGER.debug("登录超时发送回车");
//                                }
                            if (string.contains("login:")) {
                                SerialPortUtil.sendData(serialPort, user)
                            }
                            if (string.contains("Password:")) {
                                SerialPortUtil.sendData(serialPort, password)
                            }
                            if (string.contains("root@") && string.contains(":~#")) {
                                SerialPortUtil.sendData(serialPort, file)
                            }
                            if (string.contains("/: directory")) {
                                LoggerUtil.LOGGER.debug(string);
//                                taLog.value = "${taLog.value}${TimeUtil.getSimpleDateTime()}:${string}\n"
                                SerialPortUtil.sendData(serialPort, logout)
                                serialPort.removeEventListener()
                                retureFlag = true
//                                putTestInfo("/: directory 检测通过$retureFlag")
                                //                                fire(ToastEvent("usb读写验证通过"))
//                                hd100Case.add(GNSS_Case(1, "usb口检测","/: directory","通过"))
//                                stop(0)

                            }


                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            e.printStackTrace()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
//            fire(PortWorkEvent(false, serialPortIndex))
            // sleep 一段时间保证线程可以执行完
            Thread.sleep(GnssConfig.defaut_timeOut.value.toLong())

            return retureFlag
        } catch (e: Exception) {
            e.printStackTrace()
            return retureFlag
        }
    }

    /**
     * 测试Lora
     */
    fun testLora(serialPort: SerialPort): Boolean {
        var retureFlag = false
        val byteBuffer = ByteBuffer.allocate(256 * 11)
        val sendByte = ByteArray(256)
        for (i in 0..255) {
            sendByte[i] = i.toByte()
        }
        var currentTimeMillis = System.currentTimeMillis()
        try {
            if (serialPort != null) {
                LoggerUtil.LOGGER.debug("[bhz] 打开端口");
                //设置串口的listener
                SerialPortUtil.setListenerToSerialPort(serialPort) { event: SerialPortEvent ->
                    //数据通知
                    if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {

                        try {
                            var bytes = SerialPortUtil.readData(serialPort)
                            val newbytes = HexUtil.toHexString(bytes)
                            val oldbytes = HexUtil.toHexString(sendByte)
//                            println("=newbytes==${bytes.size}======" + newbytes)
//                            println("=oldbytes===${sendByte.size}=====" + oldbytes)
                            byteBuffer.put(bytes)
                            if (newbytes.contentEquals(oldbytes)) {
                                println("=========" + HexUtil.toHexString(bytes))
                                retureFlag = true
                                serialPort.removeEventListener()
                            }

//                            SerialPortUtil.sendData(serialPort, byteArray)


                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            e.printStackTrace()

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            for (i in 1..GnssConfig.lora_test_count.value.toInt()) {//发送10次数据
                //发个数据
                SerialPortUtil.sendData(serialPort, sendByte)
                Thread.sleep(GnssConfig.lora_test_Intervals.value.toLong())
            }

            val buffstr = HexUtil.toHexString(byteBuffer.array())
            val sendstr = HexUtil.toHexString(sendByte)
            val split = buffstr.split("${GnssTestData.bid.value}$sendstr")
            retureFlag = split.size >= GnssConfig.lora_test_count.value.toInt()
//            putTestInfo("收到次数:${split.size}")

            // sleep 一段时间保证线程可以执行完
            Thread.sleep(GnssConfig.defaut_timeOut.value.toLong())
//            if (!retureFlag) {
//                putTestInfo("超时")
//            } else {
//                putTestInfo("完成")
//            }
            return retureFlag
        } catch (e: Exception) {
//            putTestInfo("异常")
            e.printStackTrace()
            return retureFlag
        }
    }


}