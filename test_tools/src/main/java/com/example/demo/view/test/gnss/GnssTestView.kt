package com.example.demo.view.test.gnss

import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.*
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.gnss.GnssConfig.userId
import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*


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


    val btJobNum: Button by fxid("btJobNum")
    val tview: TableView<Case> by fxid("tview")
    val taLog: TextArea by fxid("ta_log")
    val tviewId: TableColumn<Case, Int> by fxid("tview_id")
    val tviewCase: TableColumn<Case, Int> by fxid("tview_case")
    val tviewCaseinfo: TableColumn<Case, Int> by fxid("tview_case_info")
    val tviewCaseResult: TableColumn<Case, String> by fxid("tview_case_result")
    val cbox1: ComboBox<String> by fxid("cbox1")
    val cbox2: ComboBox<String> by fxid("cbox2")

    //设备id
    var id = ""

    init {
        gnssTestView = this
        textinfo.textProperty().bind(GnssTestData.textInfo)


        cbox1.items = controller.texasCities1
        cbox2.items = controller.texasCities2
        cbox1.bind(controller.comboText1)
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
        GnssTestData.reset()
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
//            var regex = "300[0-9a-fA-F]{2}[4-9a-fA-F]{1}[0-9a-fA-F]{2}"
            var regex = "300[0-9a-fA-F]{2}[0-3]{1}[0-9a-fA-F]{2}"
            if (!bid.matches(Regex(regex))) {
                showSnackbar("请录入正确的ID(8位)")
                return@ifPresent
            }

            //设备id检查
            Api.queryHistoryLast().apply {
                if (this != null && this.size > 0) {//查询到数据如果上次测试结果为通过就提示,否则不通过
                    if (this.size >= 3 && !this[0].result && !this[1].result && !this[2].result) {//最近测试的三次都是不通过.让后台删除数据
                        fire(ToastEvent("最近测试的三次都是不通过,拒绝测试!"))
                        return@ifPresent
                    }
                    var res = ""
                    if (this[0].result) {
                        res = "通过"
                    } else {
                        res = "不通过"
                    }
                    Platform.runLater {
                        val alert = Alert(AlertType.CONFIRMATION)
                        alert.title = "检查数据"
                        alert.contentText = "查询到数据如果上次测试结果为$res,是否继续?"
                        val result = alert.showAndWait();
                        alert.showAndWait()
                        //如果点击的是ok按钮就继续,否则什么都不做
                        if (result.get() === ButtonType.OK) {
                            GnssTestData.bid.value = bid
                            scanIMSI()
                            return@runLater
                        }
                    }
                }
            }

        }
    }


    /**
     * 扫描IMSI
     */
    fun scanIMSI() {
        //检查通过输入设备id
        val dialog = TextInputDialog()
        dialog.title = "开始测试";
        dialog.headerText = "请输入SIM卡编号";
        dialog.editor.text = GnssTestData.bid.value
        val result = dialog.showAndWait()
        result.ifPresent { simNum: String ->
//            var regex = "300[0-9a-fA-F]{2}[4-9a-fA-F]{1}[0-9a-fA-F]{2}"
            var regex = "[0-9a-fA-F]{13}"
            if (!simNum.matches(Regex(regex))) {
                showSnackbar("请录入正确的SIM卡编号(13位)")
                return@ifPresent
            }
            //查询CCID不通过不允许进行测试
            val checkCCID = Api.checkCCID(simNum)
            if (checkCCID == null || checkCCID!!.`object`.size == 0) {//返回数据为null终止
                showSnackbar("查询不到SIM卡编号")
                return@ifPresent
            }
            //是否能匹配到sim卡编号
            val ccid = checkCCID!!.`object`.get(0)
            if (ccid.number != simNum) {
                showSnackbar("查询SIM卡编号匹配不正确")
                return@ifPresent
            }
            //所有check都通过开始测试
            GlobalScope.launch {
                controller.test()
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
            controller.stop(0)
            return
        }
        controller.start(controller.comboText1.value, 0)
    }

//    /**
//     * 检查id 如果为true,继续下一步
//     * false拒绝下一步
//     */
//    fun checkId(): Boolean {
//        val last = Api.queryHistoryLast()
//        if (last != null && last.size > 0) {//查询到数据如果上次测试结果为通过就提示,否则不通过
//            var res = ""
//            if (last[0].result) {
//                res = "通过"
//            } else {
//                res = "不通过"
//            }
//
//
//
//            Platform.runLater {
//                val alert = Alert(AlertType.CONFIRMATION)
//                alert.title = "检查数据"
//                alert.contentText = "查询到数据如果上次测试结果为$res,是否继续?"
//                val result = alert.showAndWait();
//                alert.showAndWait()
//            }
//            //如果点击的是ok按钮就继续
//            return result.get() == ButtonType.OK
//        }
//        return true
//    }

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
            controller.stop(1)
            return
        }
        controller.start(controller.comboText2.value, 1)
    }

    override fun onDock() {
        super.onDock()
        LoggerUtil.LOGGER.debug("${javaClass.name}-onDock")
        controller.stop(0)
        controller.stop(1)

    }


}
