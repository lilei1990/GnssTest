package com.example.demo.view.test.gnss

import com.example.demo.ToastEvent
import com.example.demo.model.*
import com.example.demo.net.Api
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.rxtx.SerialPortUtilTest
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.bean.GnssCase
import com.example.demo.view.test.bean.GnssType

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.paint.Color.GREEN
import javafx.scene.paint.Color.RED
import kotlinx.coroutines.flow.asFlow
import net.sf.marineapi.nmea.util.SatelliteInfo
import tornadofx.*
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.firstOrNull
import kotlin.collections.lastOrNull


class DeviceVersionEvent(val msg: String) : FXEvent()
class CenterController : Controller() {
    var udpStaus = objectProperty(RED)
    val disableSerialport1 = booleanProperty(false)
    val disableSerialport2 = booleanProperty(false)
    var StopTest = booleanProperty(false)

    //重启设备
    val isResystem = booleanProperty(false)
    val taLog = stringProperty("")

    val texasCities1 = FXCollections.observableArrayList<String>().asObservable()
    val texasCities2 = FXCollections.observableArrayList<String>().asObservable()
    val comboText1 = stringProperty("")
    val comboText2 = stringProperty("")

    //升级文件路径
    val updatePath = System.getProperty("user.dir") + "\\gnss_update\\gnss_1.0.1.fty11"

    //缓冲区数量
    var queue_gsv: HashMap<String, SatelliteInfo> = HashMap()

    var gnssTestView: GnssTestView? = null
    lateinit var udpMsg: UDP_Msg

    init {
        getComList()

    }

    /**
     * 关闭串口
     */
    fun stopSerialPort(indexSerial: Int) {
        try {
            if (indexSerial == 0) {
                runAsync { SerialPortUtil.closeSerialPort(GnssTestData.serialPort1) } ui {
                    GnssTestData.serialPort1 = null
                    disableSerialport1.value = false
                }
            }
            if (indexSerial == 1) {
                runAsync { SerialPortUtil.closeSerialPort(GnssTestData.serialPort2) } ui {
                    GnssTestData.serialPort2 = null
                    disableSerialport2.value = false
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 打开端口
     */
    fun start(nameCom: String, serialPortIndex: Int = 0) {
        try {
            if (serialPortIndex == 0) {
                if (GnssTestData.serialPort1 == null) {
                    fire(ToastEvent("打开端口"))
                    disableSerialport1.value = true
                    GnssTestData.serialPort1 = SerialPortUtil.openSerialPort(nameCom, SerialPortUtilTest.BAUD_RATE)
//                    startWhitch(serialPort, nameCom, serialPortIndex)
                }
            }
            if (serialPortIndex == 1) {
                if (GnssTestData.serialPort2 == null) {
                    fire(ToastEvent("打开端口"))
                    disableSerialport2.value = true
                    GnssTestData.serialPort2 = SerialPortUtil.openSerialPort(nameCom, SerialPortUtilTest.BAUD_RATE)
//                    startWhitch(serialPort, nameCom, serialPortIndex)
                }
            }
        } catch (e: Exception) {
            fire(ToastEvent("打开端口异常:${e.message}"))
            e.printStackTrace()
        }

    }


    /**
     * 获取DeviceVersion
     */
    fun getDeviceVersion() {

        Api.getConfig1() {
            var statistic601 = it
            statistic601.isResult.apply {
                if (this) {
                    //获取新的版本号后去完成打印上送操作
                    fire(DeviceVersionEvent(it.`object`.deviceVersion))
                }

            }
        }
    }

    /**
     * 升级软件版本
     */
    fun update() {
        gnssTestView?.progress?.show()
        Api.upgrade(updatePath) {
            LoggerUtil.LOGGER.debug("升级结果!$it")
            if (it) {
                fire(ToastEvent("升级成功,重启设备"))
                //升级成功,重启设备
                resystem()
            } else {
                fire(ToastEvent("升级失败"))
            }
            Platform.runLater {
                gnssTestView?.progress?.close()
            }

        }

    }

    /**
     * 升级软件版本
     */
    fun resystem() {
        Api.resystem {
            if (it.isResult) {
                isResystem.value = it.isResult
                fire(ToastEvent("重启完成"))
//                showSnackbar("升级成功!正在重启设备")
//                SnackbarUtils.show(root,"重启成功${it.isResult}")
            }

            Platform.runLater {
                gnssTestView?.progress?.close()
            }
        }

    }

    /**
     * 获取串口列表
     */
    fun getComList() {
        val serialPortList = SerialPortUtil.getSerialPortList()
        texasCities1.clear()
        texasCities1.addAll(serialPortList)
        texasCities2.clear()
        texasCities2.addAll(serialPortList)
        comboText1.value = serialPortList.firstOrNull()
        comboText2.value = serialPortList.lastOrNull()
    }


    fun checkUpdate(): Boolean {
        //检查系统版本
        if (GnssTestData.versionS.value.isNullOrEmpty() || updatePath.isNullOrEmpty()) {//需要升级
            fire(ToastEvent("未检测到软件版本"))
            return false
        } else if (GnssTestData.versionS.value != getGnssVer(updatePath)) {//需要升级
            Platform.runLater {
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "升级"
                alert.headerText = "发现系统版本需要升级"
                alert.contentText = "是否升级?"

                val result: Optional<ButtonType> = alert.showAndWait()
                if (result.get() === ButtonType.OK) {
                    update()
                } else {
                    //todo不升级
                }
            }
            fire(ToastEvent("发现系统版本需要升级"))
            return false
        }
        return true
    }

    /**
     * 开始测试
     */
    suspend fun test() {
        val caselist = arrayListOf<Case>()
        caselist.add(Case(GnssType.VHW.id, GnssType.VHW.testName))
        caselist.add(Case(GnssType.VBSP.id, GnssType.VBSP.testName))
        caselist.add(Case(GnssType.VSW.id, GnssType.VSW.testName))
        if (GnssTestData.testStatus == TestStatus.TEST_STATUS_PRO) {//单板测试
            caselist.add(Case(GnssType.BID.id, GnssType.BID.testName))
        } else {//整机
            caselist.add(Case(GnssType.ID.id, GnssType.ID.testName))
        }
        caselist.add(Case(GnssType.SER.id, GnssType.SER.testName))
        caselist.add(Case(GnssType.SIM1.id, GnssType.SIM1.testName))
        caselist.add(Case(GnssType.SIM2.id, GnssType.SIM2.testName))
        caselist.add(Case(GnssType.WIFI.id, GnssType.WIFI.testName, timeOut = 25000))
        caselist.add(Case(GnssType.LORAREC.id, GnssType.LORAREC.testName))
        caselist.add(Case(GnssType.LORASEED.id, GnssType.LORASEED.testName))
        caselist.add(Case(GnssType.GPS.id, GnssType.GPS.testName))
        caselist.add(Case(GnssType.ETH.id, GnssType.ETH.testName))
        caselist.add(Case(GnssType.KEY.id, GnssType.KEY.testName))
        caselist.add(Case(GnssType.LCD.id, GnssType.LCD.testName))
        caselist.add(Case(GnssType.LED.id, GnssType.LED.testName))
        caselist.add(Case(GnssType.POWR.id, GnssType.POWR.testName))
        caselist.add(Case(GnssType.IMSI1.id, GnssType.IMSI1.testName))
        caselist.add(Case(GnssType.IMSI2.id, GnssType.IMSI2.testName))
        val asflow = caselist.asFlow()
        GnssCase(this).run(asflow)
        udpStaus.value = GREEN

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

    fun bind(gnssTestView: GnssTestView) {
        this.gnssTestView = gnssTestView
    }


    fun putLogInfo(str: String) {
        Platform.runLater {
            taLog.value = "${TimeUtil.getSimpleDateTime()}-$str\n${taLog.value}"
        }
    }
}



