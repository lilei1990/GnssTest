package com.example.demo.view.mavlink

import com.MAVLink.MAVLink.common.msg_heartbeat
import com.MAVLink.MAVLink.common.msg_sys_status
import com.example.demo.*
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.utils.LoggerUtil
import com.example.demo.view.mavlink.bean.NavStatus
import gnu.io.SerialPort
import gnu.io.SerialPortEvent
import tornadofx.Controller
import tornadofx.FXEvent
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*

/**
 * MavLink
 */
class MavLinkStatusEvent(val msg: NavStatus) : FXEvent()
class MavLinkRebootStatusEvent() : FXEvent()
class MavLinkLogEvent(val msg: String) : FXEvent()
class MavLinkBuffNumEvent(val msg: Int) : FXEvent()
class MavLinkController : Controller() {
    var serialPort: SerialPort? = null
    var isTestStart: Boolean = false
    var isStop = false
    private var currentTimeMillis = System.currentTimeMillis()
    fun stop(indexSerial: Int) {
        isStop = true
        try {
            if (indexSerial == 0) {
                fire(MavLinkLogEvent("stop_关闭端口"))
                SerialPortUtil.closeSerialPort(serialPort)
                serialPort = null
            }

            fire(PortWorkEvent(true, indexSerial))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
        }

    }


    /**
     * 开始测试
     */
    fun start(nameCom: String, serialPortIndex: Int = 0) {
        isStop = false
        if (serialPortIndex == 0) {
            if (serialPort == null) {
                fire(MavLinkLogEvent("start_打开端口"))
                serialPort = SerialPortUtil.openSerialPort(nameCom, 115200)
                startWhitch(serialPort, nameCom, serialPortIndex)
            }
        }
    }

    /**
     * 更新ui数据
     */
    private fun updata() {

        fire(MavLinkBuffNumEvent(Mav.queue_msg_sys_status.size))
        if (System.currentTimeMillis() - currentTimeMillis > 3000) {//超过3秒没有获取数据发一条空数据
            val navStatus = NavStatus()
            fire(MavLinkStatusEvent(navStatus))
            fire(MavLinkLogEvent("超过3秒没有获取数据发一条空数据"))
            //更新时间
            currentTimeMillis = System.currentTimeMillis()
        }
        runAsync {
            var poll = Mav.queue_msg_sys_status.poll()
            while (poll != null) {
                currentTimeMillis = System.currentTimeMillis()
                //只存自己需要的数据
                if (poll is msg_sys_status) {
                    val navStatus = NavStatus()
                    navStatus.setValue(poll)
                    fire(MavLinkStatusEvent(navStatus))
                } else if (poll is msg_heartbeat) {
                    if (poll.system_status.equals(1)) { //接收机重启
                        fire(MavLinkRebootStatusEvent())
                    }

                }
                poll = Mav.queue_msg_sys_status.poll()
            }
            sleep(500)
            if (!isStop) {
                updata()
            }

        }
    }


    /**
     * 开始测试
     */
    fun startWhitch(serialPort: SerialPort?, nameCom: String, serialPortIndex: Int = 0) {
        currentTimeMillis = System.currentTimeMillis()
        updata()
        runAsync {
            try {
                if (serialPort != null) {
                    fire(MavLinkLogEvent("start_打开端口"))
                    //serialPort = SerialPortUtil.openSerialPort(nameCom, SerialPortUtilTest.BAUD_RATE)
                    //设置串口的listener

                    SerialPortUtil.setListenerToSerialPort(serialPort) { event: SerialPortEvent ->
                        //数据通知
                        if (event.eventType == SerialPortEvent.DATA_AVAILABLE) {
                            try {
                                var bytes = SerialPortUtil.readData(serialPort)
                                Mav.parser(bytes)
                            } catch (e: IOException) {
                                e.printStackTrace()
                                stop(serialPortIndex)


                            } catch (e: NullPointerException) {
                                e.printStackTrace()
                                stop(serialPortIndex)

                            } catch (e: Exception) {
                                e.printStackTrace()
                                stop(serialPortIndex)

                            }

                        }
                    }
                }
                fire(PortWorkEvent(false, serialPortIndex))

                // sleep 一段时间保证线程可以执行完
                Thread.sleep(3 * 30 * 1000.toLong())

            } catch (e: Exception) {
                e.printStackTrace()
                if (serialPortIndex == 0) {
                    this@MavLinkController.serialPort = null
                }


                fire(TextAreaMsgEvent(e.toString()))
            }
        }


    }


}