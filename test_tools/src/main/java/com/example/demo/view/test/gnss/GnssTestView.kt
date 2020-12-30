package com.example.demo.view.test.gnss

import com.example.demo.ToastEvent
import com.example.demo.model.TestStatus
import com.example.demo.net.Api
import com.example.demo.utils.*
import com.example.demo.view.test.UdpUtlis
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.bean.StopTest
import com.example.demo.view.test.gnss.GnssConfig.userId
import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import kotlinx.coroutines.*
import tornadofx.*
import java.lang.Thread.sleep


/**
 * 作者 : lei
 * 时间 : 2020/12/10.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class GnssTestView : View() {
    companion object {
        lateinit var gnssTestView: GnssTestView
    }

    override val root: BorderPane by fxml("/xml/gnss_test_view.fxml")
    val controller: CenterController by inject()


    val btHw: Button by fxid("btHw")
    val textinfo: Text by fxid("textinfo")

    val udpStaus: Circle by fxid("udpStaus")
    val btRefresh: Button by fxid("btRefresh")
    val btOpenPort1: Button by fxid("btOpenPort1")
    val btOpenPort2: Button by fxid("btOpenPort2")

    //
    val btJobNum: Button by fxid("btJobNum")
    val btStartTest: Button by fxid("btStartTest")
    val btStopTest: Button by fxid("btStopTest")

    val tview: TableView<Case> by fxid("tview")
    val taLog: TextArea by fxid("ta_log")
    val tviewId: TableColumn<Case, Int> by fxid("tview_id")
    val tviewCase: TableColumn<Case, Int> by fxid("tview_case")
    val tviewCaseinfo: TableColumn<Case, Int> by fxid("tview_case_info")
    val tviewCaseResult: TableColumn<Case, String> by fxid("tview_case_result")
    val cbox1: ComboBox<String> by fxid("cbox1")
    val cbox2: ComboBox<String> by fxid("cbox2")
    var job: Job? = null


    init {
        gnssTestView = this
        textinfo.textProperty().bind(GnssTestData.textInfo)

        cbox1.items = controller.texasCities1
        cbox2.items = controller.texasCities2
        cbox1.bind(controller.comboText1)
        btStopTest.visibleProperty()
        cbox1.disableProperty().bind(controller.disableSerialport1)
        cbox1.disableProperty().addListener { observable, oldValue, newValue ->
            if (newValue) {
                Platform.runLater {
                    btOpenPort1.text = "关闭串口"
                }
            } else {
                btOpenPort1.text = "打开串口"
            }
        }
        cbox2.bind(controller.comboText2)
        cbox2.disableProperty().addListener { observable, oldValue, newValue ->
            if (newValue) {
                Platform.runLater {
                    btOpenPort2.text = "关闭串口"
                }
            } else {
                btOpenPort2.text = "打开串口"
            }

        }
        cbox2.disableProperty().bind(controller.disableSerialport2)
        taLog.textProperty().bind(controller.taLog)
        taLog.textProperty().addListener { observable, oldValue, newValue ->
            //滚动到底部
            taLog.positionCaret(controller.taLog.value.length)
        }
        btRefresh.disableProperty().bind(controller.disableSerialport1)

        udpStaus.fillProperty().bind(controller.udpStaus)
        tviewId.cellValueFactory = PropertyValueFactory("id")
        tviewCase.cellValueFactory = PropertyValueFactory("typeName")
        tviewCaseinfo.cellValueFactory = PropertyValueFactory("testInfo")
        tviewCaseResult.cellValueFactory = PropertyValueFactory("testResult")
        tview.items = GnssTestData.hd100Case
        tviewCaseResult.cellFormat {
            text = it.toString()
            style {
                if (it.equals("通过")) {
                    backgroundColor += Color.GREEN
                    textFill = Color.WHITE
                } else {
                    backgroundColor += Color.RED
                    textFill = Color.BLACK
                }
            }
        }

        controller.subscribe<ToastEvent> {
            Platform.runLater {
                showSnackbar(it.msg)
            }
        }

        btJobNum.text = "工号:   ${userId.value}"
        controller.bind(this)

    }


    val progress = showProgressStage(
            "升级中请勿操作..."
    )


    /**
     * 刷新串口被点击
     */
    fun btRefreshAction() = controller.getComList()

    /**
     * 清除日志
     */
    fun btClearLog() {
        controller.taLog.value = ""
    }

    /**
     * 停止测试
     */
    fun btStopTest() {
        val showProgressStage = showProgressStage("终止任务中....!")
        showProgressStage.show()
        runAsync {
            StopTest.value = true
            sleep(10000)

            GlobalScope.launch {
                if (job != null) {
                    job!!.cancelAndJoin()
                }
            }
        } ui {
            GnssTestData.hd100Case.clear()
            showProgressStage.close()
        }
        //清除loar
//        UdpUtlis.clearLoar()
//        UdpUtlis.recLoar(10,300,1)
//        if (job != null) {
//            job!!.cancel()
//        }
    }

    /**
     * 开始测试
     * "基站
    00、屏幕选择进入单板测试模式
     */


    fun btStartTest() {
        val checkprogress = showProgressStage(
                "校验数据中..."
        )
        checkprogress.show()
        runAsync {
            //检查网络
            if (GnssTestData.bid.value.isNullOrEmpty()) {
                val ping = PingUtils.ping(GnssConfig.eth_test_ip.value)
                if (!ping) {
                    fire(ToastEvent("网络不通,请检查网线连接1"))
                    return@runAsync
                }
            }
            //检查升级
            if (!controller.checkUpdate()) {
                return@runAsync
            }
            //检查串口1
            if (GnssTestData.serialPort1 == null) {
                fire(ToastEvent("检查串口1是否打开"))
                return@runAsync
            }
            //检查串口2
            if (GnssTestData.serialPort2 == null) {
                fire(ToastEvent("检查串口2是否打开"))
                return@runAsync
            }
            //检信道是否匹配
            if (GnssConfig.lora_test_chen.value != GnssTestData.chan.value) {
                fire(ToastEvent("信道不匹配,已经重新配置,稍后重试"))
                //配置信道
                UdpUtlis.loraCfg(GnssConfig.lora_test_chen.value)
                return@runAsync
            }

//        //检查设备id
//        if (GnssTestData.bid.value.isNullOrEmpty()) {
//            fire(ToastEvent("未获得上报设备id,请检查网线连接"))
//            return
//        }
            //所有检查项都通过就开始测试
            Platform.runLater {
                scanID()
            }
        } ui {
            checkprogress.close()
        }


    }

    /**
     * 扫描设备id
     */
    fun scanID() {

        //检查通过输入设备id
        val dialog = TextInputDialog()
        dialog.title = "开始测试";
        dialog.headerText = "请输入测试id";
        dialog.editor.text = GnssTestData.bid.value
        val result = dialog.showAndWait()


        result.ifPresent { bid: String ->
            var regex = "300[0-9a-fA-F]{2}[0-3]{1}[0-9a-fA-F]{2}"
            when (GnssTestData.testStatus) {
                TestStatus.TEST_STATUS_PRO -> {//单板测试
                    regex = "301[0-9a-fA-F]{2}[0-3]{1}[0-9a-fA-F]{2}"
//                    regex = "301[0-9a-fA-F]{2}[4-f]{1}[0-9a-fA-F]{2}"
                    if (!bid.matches(Regex(regex))) {
                        showSnackbar("请录入正确的ID(8位)")
                        return@ifPresent
                    }
                    checkBid(bid)
                }
                TestStatus.TEST_STATUS_TOTAL -> {//整机测试
                    regex = "300[0-9a-fA-F]{2}[0-3]{1}[0-9a-fA-F]{2}"
//                    regex = "300[0-9a-fA-F]{2}[4-f]{1}[0-9a-fA-F]{2}"
                    if (!bid.matches(Regex(regex))) {
                        showSnackbar("请录入正确的ID(8位)")
                        return@ifPresent
                    }
                    checkid(bid)
                }

            }
        }
    }

    /**
     * 整机测试的时候
     */
    private fun checkid(devid: String) {
        if (GnssTestData.udp_msg0101 == null) {
            fire(ToastEvent("未获取上报的数据"))
            return
        }
        //检查上报的单板id
        if (GnssTestData.udp_msg0101!!.bid == 0) {
            fire(ToastEvent("未获取上报的单板id-${GnssTestData.udp_msg0101!!.bid}"))
            return
        }
        //查询单板测试
        val checkBidlist = Api.queryHistoryLast(GnssTestData.udp_msg0101!!.bid.toString())
        if (checkBidlist == null) {
            fire(ToastEvent("未查询到单板测试数据"))
            return
        }
        //查询到数据如果上次测试结果为通过就提示,否则不通过
        if (checkBidlist.size >= 1 && !checkBidlist[0].result) {//最近测试的三次都是不通过.让后台删除数据
            fire(ToastEvent("最近一次单板测试不通过,拒绝测试!"))
            return
        }
        //检查整机id
        runAsync {
            val queryHistoryLast = Api.queryHistoryLast(devid)
            if (queryHistoryLast == null) {
                fire(ToastEvent("设备id查询错误"))
                return@runAsync
            }
            //查询到数据如果上次测试结果为通过就提示,否则不通过
            if (queryHistoryLast.size >= 3 && !queryHistoryLast[0].result && !queryHistoryLast[1].result && !queryHistoryLast[2].result) {//最近测试的三次都是不通过.让后台删除数据
                fire(ToastEvent("最近测试的三次都是不通过,拒绝测试!"))
                return@runAsync
            }
            //数据查询为0就一次都没有测试过,直接进行下一步
            if (queryHistoryLast.size == 0) {
                Platform.runLater {
                    GnssTestData.bid.value = devid
                    //所有check都通过开始测试
                    job = GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            controller.test()
                        }
                    }
                }
                return@runAsync
            }
            var res = ""
            if (queryHistoryLast[0].result) {
                res = "通过"
            } else {
                res = "不通过"
            }
            Platform.runLater {
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "检查数据"
                alert.headerText = null
                alert.contentText = "查询到数据${queryHistoryLast.size}条,\n上次测试结果为:$res,是否继续?"
                val result = alert.showAndWait();
                //如果点击的是ok按钮就继续,否则什么都不做
                if (result.get() === ButtonType.OK) {
                    GnssTestData.bid.value = devid
                    //所有check都通过开始测试
                    GlobalScope.launch {
                        controller.test()
                    }
                    return@runLater
                }
            }
        }
    }

    private fun checkBid(bid: String) {
        runAsync {
            val queryHistoryLast = Api.queryHistoryLast(bid)
            if (queryHistoryLast == null) {
                fire(ToastEvent("设备id查询错误"))
                return@runAsync
            }
            //查询到数据如果上次测试结果为通过就提示,否则不通过
            if (queryHistoryLast.size >= 3 && !queryHistoryLast[0].result && !queryHistoryLast[1].result && !queryHistoryLast[2].result) {//最近测试的三次都是不通过.让后台删除数据
                fire(ToastEvent("最近测试的三次都是不通过,拒绝测试!"))
                return@runAsync
            }
            //数据查询为0就一次都没有测试过,直接进行下一步
            if (queryHistoryLast.size == 0) {
                Platform.runLater {
                    GnssTestData.bid.value = bid
                    //所有check都通过开始测试
                    job = GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            controller.test()
                        }
                    }
                }
                return@runAsync
            }
            var res = ""
            if (queryHistoryLast[0].result) {
                res = "通过"
            } else {
                res = "不通过"
            }
            Platform.runLater {
                val alert = Alert(AlertType.CONFIRMATION)
                alert.title = "检查数据"
                alert.headerText = null
                alert.contentText = "查询到数据${queryHistoryLast.size}条,\n上次测试结果为:$res,是否继续?"
                val result = alert.showAndWait();
                //如果点击的是ok按钮就继续,否则什么都不做
                if (result.get() === ButtonType.OK) {
                    GnssTestData.bid.value = bid
                    //所有check都通过开始测试
                    GlobalScope.launch {
                        controller.test()
                    }
                    return@runLater
                }
            }
        }
    }


    /**
     * 测试前的数据检查
     */

    fun check(): Boolean {
        //检查升级
        if (!controller.checkUpdate()) {
            return false
        }
        //检查串口
        if (GnssTestData.serialPort1 == null) {
            fire(ToastEvent("检查串口1是否打开"))
            return false
        }
        //检查串口
        if (GnssTestData.serialPort2 == null) {
            fire(ToastEvent("检查串口2是否打开"))
            return false
        }
        //检查设备id
        if (GnssTestData.bid.value.isNullOrEmpty()) {
            fire(ToastEvent("未获取设备id"))
            return false
        }
        //检查网络
        if (GnssTestData.bid.value.isNullOrEmpty()) {
            val ping = PingUtils.ping(GnssConfig.eth_test_ip.value)
            if (!ping) {
                fire(ToastEvent("网络不通"))
            }
            return ping
        }

        //检查版本
        //检查软件版本

        return true
    }


    /**
     * 打开串口1
     */
    fun btOpenPortAction1() {
        if (controller.comboText1.value.isNullOrEmpty()) {
            showSnackbar("请检查通讯端口")
            return
        }
//        if (controller.isTestStart) {
//            showSnackbar("正在测试中")
//            return
//        }
        if (controller.disableSerialport1.value) {
            controller.stopSerialPort(0)
            return
        }
        controller.start(controller.comboText1.value, 0)
    }

    /**
     * 打开串口2
     */
    fun btOpenPortAction2() {
        if (controller.comboText2.value.isNullOrEmpty()) {
            showSnackbar("请检查通讯端口")
            return
        }
//        if (controller.isTestStart) {
//            showSnackbar("正在测试中")
//            return
//        }
        if (controller.disableSerialport2.value) {
            controller.stopSerialPort(1)
            return
        }
        controller.start(controller.comboText2.value, 1)
    }

    override fun onDock() {
        super.onDock()
        LoggerUtil.LOGGER.debug("${javaClass.name}-onDock")
        controller.stopSerialPort(0)
        controller.stopSerialPort(1)

    }


}
