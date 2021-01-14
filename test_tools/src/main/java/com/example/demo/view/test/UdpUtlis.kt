package com.example.demo.view.test

import com.alibaba.fastjson.JSON
import com.example.demo.model.TestStatus
import com.example.demo.net.Api
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.utils.ByteUtils
import com.example.demo.utils.HexUtil
import com.example.demo.utils.LoggerUtil
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.gnss.CenterController
import com.example.demo.view.test.gnss.GnssConfig
import com.example.demo.view.test.gnss.GnssTestData
import gnu.io.SerialPort
import gnu.io.SerialPortEvent
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Thread.sleep

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

        var str = "{\"id\":${bid.toInt(16)}}"
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

    /**
     * 0x0009	Loar清空请求	空
     */
    fun clearLoar(): Boolean {
        val str = ""
        val send = UDPBroadcast.send(Pack.pack(0x000f, str), 50250)
        return parkRet(send)
    }

    /**
     * 0x0011	lora发送命令请求	num+次数，size+大小，timeout+间隔，例：{"num":10,"size":256,"timeout":2}
     */
    fun recLoar(num: Int, size: Int, timeOut: Int): Boolean {
        val str = "{\"num\":$num,\"size\":$size,\"timeout\":$timeOut}"
        val send = UDPBroadcast.send(Pack.pack(0x0011, str), 50250)
        return parkRet(send)
    }

    fun parkRet(data2: ByteArray?): Boolean {
        data2 ?: return false
        val unPack = Pack.unPack(data2)
        val obj = JSON.parseObject(unPack.str)
        when (unPack.id.toInt()) {
            0x0002, 0x0004, 0x0006, 0x0008, 0x000a, 0x000c, 0x000e, 0x0010, 0x0012 -> {
                if (obj["ret"] == 0) { //成功
//                    println("unPack.id:${unPack.id.toInt()}")
                    return true
                }
            }
        }
        return false
    }

    /**
     * 测试串口,usb读写
     */
    fun testSerialPort(serialPort: SerialPort, case: Case): Boolean {
        var retureFlag = false

        val user: ByteArray = "root\n".toByteArray()
        val password: ByteArray = "i501.1234\n".toByteArray()
        val writefile: ByteArray = "echo \"hello\" > /run/media/sda1/testa /\n".toByteArray()
        val readfile: ByteArray = "cat /run/media/sda1/testa\n".toByteArray()
        val logout: ByteArray = "logout\n".toByteArray()
        var currentTimeMillis = System.currentTimeMillis()
        serialPort.removeEventListener()
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
                            if (bytes == null || bytes.isEmpty()) {
                                LoggerUtil.LOGGER.debug("串口数据为空")
                                return@setListenerToSerialPort
                            }
                            val string = String(bytes)
//                            controller.putLogInfo(string)
                            var recv = ByteUtils.bytesToHexString(bytes)
                            //超过30秒任务失败
                            if (System.currentTimeMillis() - currentTimeMillis > 30000) {
                                LoggerUtil.LOGGER.debug("usb口检测不通过")
                                serialPort.removeEventListener()
                            }
                            if (string.contains("login:")) {
                                SerialPortUtil.sendData(serialPort, user)
                            }
                            if (string.contains("Password:")) {
                                SerialPortUtil.sendData(serialPort, password)
                            }
                            if (string.contains("root@") && string.contains(":~#")) {
                                SerialPortUtil.sendData(serialPort, writefile)
                                sleep(1000)
                                SerialPortUtil.sendData(serialPort, readfile)
                            }
                            if (string.contains("hello")) {
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
                            case.putTestInfo("异常终止IOException")
                            serialPort.removeEventListener()
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            case.putTestInfo("异常终止NullPointerException")
                            serialPort.removeEventListener()
                            e.printStackTrace()
                        } catch (e: Exception) {
                            case.putTestInfo("异常终止Exception")
                            LoggerUtil.LOGGER.debug("异常终止Exception-${e.message}")
                            serialPort.removeEventListener()
                            e.printStackTrace()
                        }
                    }
                }
            }
//            controller.putLogInfo("发个换行符激活设备")
            sleep(1000)
            //发个换行符激活设备,防止有时候休眠,
            SerialPortUtil.sendData(serialPort, "\n".toByteArray())
            SerialPortUtil.sendData(serialPort, "\n".toByteArray())
//            fire(PortWorkEvent(false, serialPortIndex))
            // sleep 一段时间保证线程可以执行完
            sleep(GnssConfig.defaut_timeOut.value.toLong())
            serialPort.removeEventListener()
            return retureFlag
        } catch (e: Exception) {
            serialPort.removeEventListener()
            e.printStackTrace()
            return retureFlag
        }
    }

    /**
     * 测试Lora
     */
    fun testLoraRec(serialPort: SerialPort, case: Case): Boolean {
        var count = 0
        var retureFlag = false
        serialPort.removeEventListener()
        println("开始")
        try {
            if (serialPort != null) {
                LoggerUtil.LOGGER.debug("[bhz] 打开端口");
                //设置串口的listener
                SerialPortUtil.setListenerToSerialPort(serialPort) { event: SerialPortEvent ->
                    //数据通知
                    if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {


                        var inputStrieam: InputStream? = null
                        var bytes: ByteArray? = null
                        try {

                            //获得串口的输入流
                            inputStrieam = serialPort.inputStream
                            //获得数据长度
                            var bufflenth = inputStrieam.available()
                            while (bufflenth != 0) {
                                //初始化byte数组
                                bytes = ByteArray(bufflenth)
                                inputStrieam.read(bytes)
                                bufflenth = inputStrieam.available()
                                count += bytes.size
                            }
                            inputStrieam.close()
//                            println("信号强度:$rssi")

//                            LoggerUtil.LOGGER.debug(HexUtil.toHexString(bytes))

//                            LoggerUtil.LOGGER.debug("大小:${bytes.size}==${count}")


                        } catch (e: IOException) {
                            case.putTestInfo("异常终止")
                            serialPort.removeEventListener()
                            inputStrieam?.close()
                            e.printStackTrace()
                        } catch (e: NullPointerException) {
                            case.putTestInfo("异常终止")
                            e.printStackTrace()
                            serialPort.removeEventListener()
                            inputStrieam?.close()
                        } catch (e: Exception) {
                            case.putTestInfo("异常终止")
                            serialPort.removeEventListener()
                            e.printStackTrace()
                            inputStrieam?.close()
                        }
                    }
                }
            }
            recLoar(GnssConfig.lora_test_count.value, 300, GnssConfig.lora_test_Intervals.value)


            //防止上报数据慢,这个时间和上面配置发送数据的间隔和次数有关
            sleep(GnssConfig.lora_test_Intervals.value * GnssConfig.lora_test_count.value * 1000L + 10000)
            case.putTestInfo("发:${GnssTestData.udp_msg0101!!.loraCounter_send}-收:$count")
            LoggerUtil.LOGGER.debug("\n发:${GnssTestData.udp_msg0101!!.loraCounter_send}-收:$count")

            serialPort.removeEventListener()
            if (GnssTestData.udp_msg0101 == null) {
                return false
            }
            //减去rssi的校验位,因为超过240拆包两个
            if (count-GnssConfig.lora_test_count.value*2 == GnssTestData.udp_msg0101!!.loraCounter_send) {
                return true
            }

            return retureFlag
        } catch (e: Exception) {
            case.putTestInfo("异常终止")
            serialPort.removeEventListener()
            e.printStackTrace()
            return retureFlag
        }
    }

    /**
     * 测试Lora
     */
    fun testLoraRssi(serialPort: SerialPort, case: Case): Boolean {
        var count = 0
        var retureFlag = false
        serialPort.removeEventListener()

        if (serialPort != null) {
            SerialPortUtil.setListenerToSerialPort(serialPort) { event: SerialPortEvent ->
                //数据通知
                if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {
                    sleep(1000)
                    val bytes = SerialPortUtil.readData(serialPort)
                    if (bytes.size==51) {
                        //                    val bytes = SerialPortUtil.readData(serialPort)
                        //无线信号中db值射端一般是正值，数bai值越大发射功率越大越好；接收端一般是负值，数值越小代表灵敏度越高越好。
                        val rssi = -(256 - (bytes[bytes.size - 1].toInt() and 0xFF))

                        if (rssi >= GnssConfig.lora_test_strength.value && rssi <= -35 && rssi != 0 && rssi != -256) {
                            retureFlag = true
                        }
//                        else {
//                            retureFlag = false
//                        }
//                        if (rssi > GnssConfig.lora_test_strength.value) {
//                            retureFlag = true
//                        }
                        LoggerUtil.LOGGER.debug("$rssi-$retureFlag")
                        case.putTestInfo("$rssi")
                    }
                    LoggerUtil.LOGGER.debug("${HexUtil.toHexString(bytes)}")

                }
            }
        }
        recLoar(1, 50, GnssConfig.lora_test_Intervals.value)
        sleep(3000)
        LoggerUtil.LOGGER.debug("retureFlag-$retureFlag}")
        return retureFlag
    }

    /**
     * 测试Lora
     */
    fun testLoraSend(serialPort: SerialPort, case: Case): Boolean {
        try {
            serialPort.removeEventListener()
            SerialPortUtil.setListenerToSerialPort(serialPort) {

            }
        } catch (e:Exception) {
            LoggerUtil.LOGGER.debug("测试Lora发生异常")
        }


        var retureFlag = false
        //准备要发送的数据
        val sendByte = ByteArray(256)
        for (i in 0..255) {
            sendByte[i] = i.toByte()
        }
        LoggerUtil.LOGGER.debug("准备要发送的数据${sendByte.size}")
        //循环发送的次数
        for (i in 1..GnssConfig.lora_test_count.value) {
            LoggerUtil.LOGGER.debug("循环发送的次数-${i}")
            //发个数据
            SerialPortUtil.sendData(serialPort, sendByte)
            //发送间隔
            sleep(GnssConfig.lora_test_Intervals.value * 1000L)

        }
        //防止上报数据慢

        sleep(10000)
        val count = GnssConfig.lora_test_count.value * 256
        case.putTestInfo("发:$count-收:${GnssTestData.udp_msg0101!!.loraCounter_rec}")
        if (GnssTestData.udp_msg0101 == null) {
            return false
        }
        if (count == GnssTestData.udp_msg0101!!.loraCounter_rec) {
            return true
        }
        return retureFlag
    }


    //升级文件路径
    val updateReleasePath = System.getProperty("user.dir") + "\\gnss_update\\gnss_release_1.0.2"
    val updateTestPath = System.getProperty("user.dir") + "\\gnss_update\\gnss_Test_1.0.1.fty11"
    /**
     * 升级软件版本
     */
    fun update(controller: CenterController,case:Case,updatePath:String) {
        Api.upgrade(updatePath) {
            LoggerUtil.LOGGER.debug("升级结果!$it")
            if (it) {
                controller.putLogInfo("升级成功,重启设备")
                //升级成功,重启设备
                resystem(controller,case)
            } else {
                controller.putLogInfo("升级失败")
            }
        }

    }
    /**
     * 升级软件版本
     */
    fun resystem(controller: CenterController,case:Case) {
        Api.resystem {
            if (it.isResult) {
//                isResystem.value = it.isResult
                controller.putLogInfo("重启完成")
                case.result = !GnssTestData.versionBsp.value.isNullOrEmpty()
                case.putTestInfo(GnssTestData.versionBsp.value)
//                showSnackbar("升级成功!正在重启设备")
//                SnackbarUtils.show(root,"重启成功${it.isResult}")
            }

        }

    }


    /**
     * 获取基站版本文件的版本号
     *
     * @param filePath 文件路径
     * @return 版本号
     */
    fun getGnssVer(filePath: String): String? {
        var version: String? = null
        var inStream: InputStream? = null
        try {
            inStream = FileInputStream(filePath)
            val fileLen: Int = inStream.available()
            val data = ByteArray(32)
            inStream.skip((fileLen - data.size).toLong())
            inStream.read(data)
            val tempVer = String(data).trim(' ')
            val markIndex = tempVer.lastIndexOf('$')
            if (markIndex != -1) {
                version = tempVer.substring(markIndex + 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inStream != null) {
                try {
                    inStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        inStream?.close()
        return version?.replace("\n", "")
    }

}