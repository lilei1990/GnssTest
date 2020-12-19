package com.example.demo.view.mavlink

import com.example.demo.*
import com.example.demo.rxtx.SerialPortUtil
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.TimeUtil
import com.example.demo.view.mavlink.bean.NavStatus
import com.jfoenix.controls.JFXTextField
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import kfoenix.jfxbutton
import kfoenix.jfxsnackbar
import tornadofx.*

class MavLinkView : View() {
    private val controller: MavLinkController by inject()
    private lateinit var textarea: TextArea
    private lateinit var label: Label
    private lateinit var labelbuff: Label
    private val isPortClose = booleanProperty(true)
    private val comboText = stringProperty("")
    private val texasCities = FXCollections.observableArrayList<String>()
    private var rebootNum = 0
    var mavlinkBeans = observableListOf<NavStatus>()

    init {
        //订阅日志数据
        controller.subscribe<MavLinkStatusEvent> {
            Platform.runLater {
                mavlinkBeans.clear()
                val msg = it.msg
                mavlinkBeans.add(msg)
                LoggerUtil.LOGGER.debug(
                        "isGpsData:${msg.isGpsData}" +
                                "     isImuData:${msg.isImuData}" +
                                "     isImuNormal:${msg.isImuNormal}" +
                                "     isMagData:${msg.isMagData}" +
                                "     isGyroConnect:${msg.isGyroConnect}")

            }
        }
        //缓冲区数据量
        controller.subscribe<MavLinkBuffNumEvent> {
            Platform.runLater {
                labelbuff.text = "缓冲区数据量:${Mav.queue_msg_sys_status.size}"
            }
        }
        //订阅串口状态
        controller.subscribe<PortWorkEvent> {
            if (it.index == 0) {
                isPortClose.value = it.isClose
            }
        }

        controller.subscribe<PortWorkEvent> {
            if (it.index == 0) {

                isPortClose.value = it.isClose
            }
        }
        //重启次数
        controller.subscribe<MavLinkRebootStatusEvent> {
            rebootNum++
            label.text = "重启次数:$rebootNum"
            textarea.appendText("${TimeUtil.getSimpleDateTime()}:" + "发生重启" + "\n")
        }

        //日志信息
        controller.subscribe<MavLinkLogEvent> {
            textarea.appendText("${TimeUtil.getSimpleDateTime()}:" + it.msg + "\n")
            LoggerUtil.LOGGER.debug(it.msg)
        }
        getComList()
    }

    override val root = borderpane {
        stylesheets.addAll("/css/jfoenix-components.css", "/css/jfoenix-main-demo.css")
        center = anchorpane() {
            splitpane(Orientation.VERTICAL) {
                anchorpaneConstraints {
                    rightAnchor = 10
                    leftAnchor = 10
                    topAnchor = 10
                    bottomAnchor = 10
                }
                vbox(10) {
                    //串口打开,关闭
                    hbox(10, Pos.CENTER_LEFT) {
                        paddingTop = 10
                        paddingLeft = 10
                        jfxbutton("刷新串口列表") {
                            action {
                                getComList()
                            }
                        }
                        combobox(comboText, values = texasCities) {
                            setOnAction {
                                //jfxsnackbar(comboText.value)
                            }

                            //selectionModel.selectFirst()
                        }
                        jfxbutton("打开串口") {
                            isPortClose.onChange {
                                if (it) {
                                    text = "打开串口"
                                } else {
                                    text = "关闭串口"
                                }
                            }
                            action {
                                if (comboText.value.isNullOrEmpty()) {
                                    jfxsnackbar("请检查通讯端口")
                                    return@action
                                }
                                if (controller.isTestStart) {
                                    jfxsnackbar("正在测试中")
                                    return@action
                                }
                                if (!isPortClose.value) {
                                    controller.stop(0)
                                    return@action
                                }

                                controller.start(comboText.value, 0)
                            }
                        }
                        label = label("重启次数:$rebootNum")

                    }

                    vbox(10) {
                        fitToParentHeight()
                        controller.subscribe<ToastEvent> {
                            jfxsnackbar(it.msg)
                        }

                        tableview(mavlinkBeans) {

                            fitToParentHeight()
                            readonlyColumn("isGpsData", NavStatus::isGpsData)
                            readonlyColumn("isImuData", NavStatus::isImuData)
                            readonlyColumn("isImuNormal", NavStatus::isImuNormal)
                            readonlyColumn("isMagData", NavStatus::isMagData)
                            readonlyColumn("isGyroConnect", NavStatus::isGyroConnect)

                        }


                    }


                }


            }.apply {
                setDividerPosition(0, 0.9)
            }


        }
        right = splitpane(Orientation.VERTICAL) {

            //底部日志
            anchorpane() {
                prefHeight = maxHeight
                textarea = textarea() {

                    //font= Font.font( 14.0)
                    style = "-fx-font-family: 'FangSong';-fx-font-size: 14;"
                    anchorpaneConstraints {
                        rightAnchor = 10
                        leftAnchor = 10
                        topAnchor = 10
                        bottomAnchor = 10
                    }
                }


            }
            labelbuff = label("缓冲区数据量:$rebootNum")
            jfxbutton("清除日志") {
                action {
                    textarea.text = ""

                }
            }

        }

    }


    override fun onDock() {
        super.onDock()
        fire(MavLinkLogEvent("start_打开端口"))
        controller.stop(0)
    }


    private fun getComList() {
        val serialPortList = SerialPortUtil.getSerialPortList()
        texasCities.clear()
        texasCities.addAll(serialPortList)
        comboText.value = serialPortList.firstOrNull()
    }

}
