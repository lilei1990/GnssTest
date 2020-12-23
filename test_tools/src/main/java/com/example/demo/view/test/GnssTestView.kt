package com.example.demo.view.test

import com.example.demo.BaseView
import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.*
import com.example.demo.view.test.GnssConfig.userId
import com.example.demo.view.test.bean.Case
import javafx.application.Platform
import javafx.scene.control.*
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
class GnssTestView  : BaseView() {
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

    var versionVlue = "v.12.323"
    val progress = showProgressStage(
        "升级中请勿操作..."
    )

    /**
     * 升级软件版本
     */
    fun update() {
//        progress.show()
//        controller.update()
    }

    /**
     * 获取单板ID
     */
    fun setSingleCard() {


    }


    /**
     * 测试lcd
     */
    fun btTestLcd() {

//        openNewStage("屏幕测试",TestLcdView())

    }

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
     * 开始测试
     * "基站
    00、屏幕选择进入单板测试模式
     */
    fun btStartTest() {
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
            try {
                //查询CCID不通过不允许进行测试
                Api.checkCCID(bid) {
                    LoggerUtil.LOGGER.debug("查询结果:${it.`object`.size}")
                    if (it.`object`.size == 0) {
                        GnssTestData.bid.value = bid
                        GlobalScope.launch {
                            controller.test()
                        }
                    }

                }
            } catch (e: java.lang.Exception) {
                showSnackbar("查询异常")
            }
        }

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
