package com.example.demo.view.test.bean

import com.example.demo.model.DeviceTestModel
import com.example.demo.model.TestStatus
import com.example.demo.net.Api
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.utils.*
import com.example.demo.utils.cmd.*
import com.example.demo.view.test.*
import com.example.demo.view.test.gnss.GnssConfig.wifi_test_ip
import com.example.demo.view.test.gnss.GnssConfig.wifi_test_pwd
import com.example.demo.view.test.gnss.*
import com.google.gson.JsonArray
import javafx.application.Platform
import javafx.scene.control.TextInputDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

enum class GnssType(val id: Int, val testName: String) {
    VHW(1, "硬件版本号"),
    VBSP(2, "镜像版本号"),
    VSW(3, "软件版本号"),
    BID(4, "单板ID"),
    ID(5, "整机ID"),
    USB(6, "USB"),
    SER(7, "调试串口"),
    SIM1(8, "4G1（SIM）"),
    SIM2(9, "4G2（SIM）"),
    WIFI(10, "WIFI"),
    LORA(11, "LORA"),
    GPS(12, "GPS"),
    ETH(13, "ETH"),
    KEY(14, "KEY"),
    LCD(15, "LCD"),
    LED(16, "LED检测"),
    POWR(17, "掉电保护"),
    IMSI1(18, "4G1（IMSI）"),
    IMSI2(19, "4G2（IMSI）")
}

open class GnssCase() {

    suspend fun run() {
        val caselist = arrayListOf<Case>()
//        caselist.add(Case(GnssType.VHW.id, GnssType.VHW.testName))
//        caselist.add(Case(GnssType.VBSP.id, GnssType.VBSP.testName))
//        caselist.add(Case(GnssType.VSW.id, GnssType.VSW.testName))
//        caselist.add(Case(GnssType.BID.id, GnssType.BID.testName))
////        caselist.add(Case(GnssType.ID.id, GnssType.ID.testName))
//        caselist.add(Case(GnssType.USB.id, GnssType.USB.testName))
//        caselist.add(Case(GnssType.SER.id, GnssType.SER.testName))
//        caselist.add(Case(GnssType.SIM1.id, GnssType.SIM1.testName))
//        caselist.add(Case(GnssType.SIM2.id, GnssType.SIM2.testName))
//        caselist.add(Case(GnssType.WIFI.id, GnssType.WIFI.testName))
//        caselist.add(Case(GnssType.LORA.id, GnssType.LORA.testName))
//        caselist.add(Case(GnssType.GPS.id, GnssType.GPS.testName))
//        caselist.add(Case(GnssType.ETH.id, GnssType.ETH.testName))
//        caselist.add(Case(GnssType.KEY.id, GnssType.KEY.testName))
//        caselist.add(Case(GnssType.LCD.id, GnssType.LCD.testName))
//        caselist.add(Case(GnssType.LED.id, GnssType.LED.testName))
//        caselist.add(Case(GnssType.POWR.id, GnssType.POWR.testName))
        caselist.add(Case(GnssType.IMSI1.id, GnssType.IMSI1.testName))
        caselist.add(Case(GnssType.IMSI2.id, GnssType.IMSI2.testName))
        caselist.asFlow().map {
            it.apply {
                whenID(it)
            }
        }.flowOn(Dispatchers.IO).onCompletion {
            testUpload()
            println("测试完成")
        }.collect {
            GnssTestData.hd100Case.add(it)
            println(it.typeName)
        }
//        channelFlow<Case> {
//            for (case in caselist) {
//                async {
//                    whenID(case)
//                    send(case)
//                }
//            }
//
//        }.flowOn(Dispatchers.IO).onCompletion {
//            println("测试完成")
//        }
//            .collect {
//                GnssTestData.hd100Case.add(it)
//                println(it.typeName)
//            }
//

    }

    private suspend fun whenID(case: Case) {
        when (case.id) {
            GnssType.VHW.id -> {
                case.result = !GnssTestData.versionH.value.isNullOrEmpty()
                case.putTestInfo(GnssTestData.versionH.value)
            }
            GnssType.VSW.id -> {
                case.result = !GnssTestData.versionS.value.isNullOrEmpty()
                case.putTestInfo(GnssTestData.versionS.value)
            }
            GnssType.VBSP.id -> {
                case.result = !GnssTestData.versionBsp.value.isNullOrEmpty()
                case.putTestInfo(GnssTestData.versionBsp.value)
            }
            GnssType.BID.id -> {//写单板id

                case.result = UdpUtlis.writeBID(GnssTestData.bid.value)
                //重启wifi
                UdpUtlis.enableWifi(false)
                delay(5000)
                UdpUtlis.enableWifi(true)
            }
            GnssType.ID.id -> {//写整机id

            }
            GnssType.USB.id -> {//USB测试

            }
            GnssType.SER.id -> {//测试串口
                case.result = UdpUtlis.testSerialPort(GnssTestData.serialPort1!!)

            }
            GnssType.SIM1.id -> {//4G1（SIM）
                case.result = GnssTestData.sim1ping.value > 0
                case.putTestInfo("${GnssTestData.sim1ping.value}")
            }
            GnssType.SIM2.id -> {//4G2（SIM）
                case.result = GnssTestData.sim2ping.value > 0
                case.putTestInfo("${GnssTestData.sim2ping.value}")
            }
            GnssType.WIFI.id -> {//测试wifi

                var profileContent = Profile.PROFILE.replace(Profile.WIFI_NAME, "${GnssTestData.bid.value}")
                profileContent = profileContent.replace(Profile.WIFI_PASSWORD, wifi_test_pwd.value)
                FileUtilsJava.writeToFile(Connector.PROFILE_TEMP_PATH + "1234567890.xml", profileContent)
                //        genProfile("hdwork");
                var task = check("${GnssTestData.bid.value}", wifi_test_pwd.value)
                while (!task) {
                    delay(1000)
                    task = check("${GnssTestData.bid.value}", wifi_test_pwd.value)
                }
                //延迟一段时间测试ping，防止未连接
                delay(case.timeOut)
                case.result = PingUtils.ping(wifi_test_ip.value)
                //todo 断开测试WiFi 连接之前的WiFi
            }
            GnssType.LORA.id -> {//测试loar
                case.result = UdpUtlis.loraCfg(GnssConfig.lora_test_chen.value)
                case.result = UdpUtlis.testLora(GnssTestData.serialPort2!!)

            }
            GnssType.GPS.id -> {//测试Gps
                delay(case.timeOut)
                var num = 0
                for (mutableEntry in GnssTestData.satelliteMap) {
                    if (mutableEntry.value > GnssConfig.gps_test_min_num.value) {
                        num++
                    }
                }
                case.result = num > GnssConfig.gps_test_min_satellite_Count.value
                case.putTestInfo("合格卫星数量:$num")
            }
            GnssType.ETH.id -> {//测试eth
                case.result = PingUtils.ping("192.168.1.252")
            }
            GnssType.KEY.id -> {//测试key
                var isNext = false
                Platform.runLater {
                    GnssTestView.gnssTestView.shwoInternalWindow(TestKeyView(case) {
                        isNext = true
                    })
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    delay(1000)
                }

            }
            GnssType.LCD.id -> {//lcd测试
                var isNext = false
                Platform.runLater {
                    GnssTestView.gnssTestView.shwoInternalWindow(TestLcdView(case) {
                        isNext = true
                    })
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    delay(1000)
                }

            }
            GnssType.LED.id -> {//led
                var isNext = false

                Platform.runLater {
                    GnssTestView.gnssTestView.shwoInternalWindow(TestLedView(case) {
                        isNext = true
                    })
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    SerialPortUtil.sendData(GnssTestData.serialPort2, ByteArray(256))
                    delay(1000)
                }
            }

            GnssType.POWR.id -> {//掉电保护
                var isNext = false
                Platform.runLater {
                    GnssTestView.gnssTestView.shwoInternalWindow(TestPowerView(case) {
                        isNext = true
                    })
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    delay(1000)
                }

            }
            GnssType.IMSI1.id -> {//掉电保护
                var isNext = false
                Platform.runLater {
                    //检查通过输入设备id
                    val dialog = TextInputDialog()
                    dialog.title = "SIM卡编号1";
                    dialog.headerText = "请输入SIM卡编号1";
                    dialog.editor.text = ""
                    var result = dialog.showAndWait()
                    val net4g1 = GnssTestData.net4g1_imsi.value

                    if (net4g1.isNullOrEmpty()) {
                        case.putTestInfo("net4g1:$net4g1")
                        case.result = false
                        isNext = true
                    }
                    result.ifPresent { ccid: String ->

//            var regex = "300[0-9a-fA-F]{2}[4-9a-fA-F]{1}[0-9a-fA-F]{2}"
                        var regex = "[0-9a-fA-F]{13}"
                        if (!ccid.matches(Regex(regex))) {
                            GnssTestView.gnssTestView.showSnackbar("请录入正确的SIM卡编号(13位)")
                            case.putTestInfo("请录入正确的SIM卡编号(13位)")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        //查询CCID
                        val checkCCID = Api.checkCCID(net4g1)
                        if (checkCCID == null || checkCCID!!.rows.size == 0) {//返回数据为null终止
                            GnssTestView.gnssTestView.showSnackbar("查询不到SIM卡编号")
                            case.putTestInfo("查询不到SIM卡编号,ccid:$ccid")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        //是否能匹配到sim卡编号
                        val sim = checkCCID!!.rows.get(0)
                        if (sim.number != ccid) {
                            GnssTestView.gnssTestView.showSnackbar("查询SIM卡编号匹配不正确")
                            case.putTestInfo("查询SIM卡编号匹配不正确,ccid:$ccid")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        case.result = true
                        isNext = true
                    }
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    delay(1000)
                }

            }
            GnssType.IMSI2.id -> {//掉电保护
                var isNext = false
                Platform.runLater {
                    //检查通过输入设备id
                    val dialog = TextInputDialog()
                    dialog.title = "SIM卡编号2";
                    dialog.headerText = "请输入SIM卡编号2";
                    dialog.editor.text = ""
                    var result = dialog.showAndWait()
                    val net4g2 = GnssTestData.net4g2_imsi.value

                    if (net4g2.isNullOrEmpty()) {
                        case.putTestInfo("net4g2:$net4g2")
                        case.result = false
                        isNext = true
                    }
                    result.ifPresent { ccid: String ->

//            var regex = "300[0-9a-fA-F]{2}[4-9a-fA-F]{1}[0-9a-fA-F]{2}"
                        var regex = "[0-9a-fA-F]{13}"
                        if (!ccid.matches(Regex(regex))) {
                            GnssTestView.gnssTestView.showSnackbar("请录入正确的SIM卡编号(13位)")
                            case.putTestInfo("请录入正确的SIM卡编号(13位)")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        //查询CCID不通过不允许进行测试
                        val checkCCID = Api.checkCCID(net4g2)
                        if (checkCCID == null || checkCCID!!.rows.size == 0) {//返回数据为null终止
                            GnssTestView.gnssTestView.showSnackbar("查询不到SIM卡编号")
                            case.putTestInfo("查询不到SIM卡编号,ccid:$ccid")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        //是否能匹配到sim卡编号
                        val sim = checkCCID!!.rows.get(0)
                        if (sim.number != ccid) {
                            GnssTestView.gnssTestView.showSnackbar("查询SIM卡编号匹配不正确")
                            case.putTestInfo("查询SIM卡编号匹配不正确,ccid:$ccid")
                            case.result = false
                            isNext = true
                            return@ifPresent
                        }
                        case.result = true
                        isNext = true
                    }
                }
                //定时检测是否收到下一步的消息
                while (!isNext) {
                    delay(1000)
                }

            }
        }
    }


    /**
     * 上传测试结果
     */
    fun testUpload() {
        val deviceTestModel = DeviceTestModel()
        deviceTestModel.testTime = TimeUtil.getSimpleDateTime()
        deviceTestModel.status = GnssTestData.testStatus
        if (GnssTestData.testStatus == TestStatus.TEST_STATUS_PRO) {
            deviceTestModel.equipmentId = GnssTestData.bid.value
        } else {
            deviceTestModel.equipmentId = GnssTestData.id.value
        }

        var jsr = JsonArray()
        //默认通过
        deviceTestModel.result = true
        for (case in GnssTestData.hd100Case) {
            jsr.add("${case.id},${case.testInfo}")
            if (!case.result) {//如果有一项不通过就整体不通过
                deviceTestModel.result = false
            }
        }


        deviceTestModel.remark = jsr.toString()
        deviceTestModel.userId = GnssConfig.userId.value
        var rst = Api.uploadTestRest(deviceTestModel).let {
            if (it) {
                TestStatus.PASS
            } else {
                TestStatus.FAIL
            }
        }

    }


    /**
     * 添加配置文件
     *
     * @param profileName 添加配置文件
     */
    fun addProfile(profileName: String): Boolean {
        val cmd = Command.ADD_PROFILE.replace("FILE_NAME", profileName)
        val result = execute(cmd, Connector.PROFILE_TEMP_PATH)
        if (result != null && result.size > 0) {
            if (result[0]!!.contains("添加到接口")) {
                return true
            }
        }
        return false
    }

    /**
     * 连接wifi
     *
     * @param ssid 添加配置文件
     */
    fun connect(ssid: String): Boolean {
        var connected = false
        val cmd = Command.CONNECT.replace("SSID_NAME", ssid)
        val result = execute(cmd, null)
        if (result != null && result.size > 0) {
            if (result[0]!!.contains("已成功完成")) {
                connected = true
            }
        }
        return connected
    }

    /**
     * 执行器
     *
     * @param cmd      CMD命令
     * @param filePath 需要在哪个目录下执行
     */
    fun execute(cmd: String?, filePath: String?): List<String?>? {
        var process: Process? = null
        val result: MutableList<String?> = ArrayList()
        try {
            process = if (filePath != null) {
                Runtime.getRuntime().exec(cmd, null, File(filePath))
            } else {
                Runtime.getRuntime().exec(cmd)
            }
            val bReader = BufferedReader(InputStreamReader(process.inputStream, "gbk"))
            var line: String? = null
            while (bReader.readLine().also { line = it } != null) {
                result.add(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 校验WLAN配置文件是否正确
     *
     *
     * 校验步骤为：
     * ---step1 添加配置文件
     * ---step3 连接wifi
     * ---step3 ping校验
     */
    @Synchronized
    open fun check(ssid: String, password: String): Boolean {
        println("check : $ssid--$password")
        try {
            val profileName = "$password.xml"
            if (addProfile(profileName)) {
                if (connect(ssid)) {
                    Thread.sleep(50)
                    return true
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 结果
     */
    fun result() {
    }


}
