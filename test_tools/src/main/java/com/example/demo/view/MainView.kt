package com.example.demo.view

import com.example.demo.Version
import com.example.demo.model.TestStatus
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.openNewStage
import com.example.demo.utils.shwoInternalWindow
import com.example.demo.view.test.gnss.GnssConfigView
import com.example.demo.view.test.gnss.GnssTestData
import com.example.demo.view.test.gnss.JobNumView
import com.jfoenix.controls.JFXButton
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Control
import kfoenix.jfxbutton
import tornadofx.*

/**
 * 主视窗
 */
class MainView : View() {


    val gnssConfigView: GnssConfigView by inject()
    val input = SimpleStringProperty()
    lateinit var btn: JFXButton

    //根节点
    override val root = vbox {
        prefWidth = 600.0
        prefHeight = 600.0
        menubar {
            menu("设置") {
                item("设置") {
                    action {
                        shwoInternalWindow(gnssConfigView, escapeClosesWindow = true, closeButton = true)
                    }
                }
                item("测试") {
                    action {
                        val debugTestView = DebugTestView()
                        openNewStage("GNSS参考站", debugTestView)
                        currentStage?.isIconified = true
//                        debugTestView.currentStage?.isAlwaysOnTop = true
                    }
                }
            }


        }

        vbox(20, Pos.CENTER) {
            paddingAll = 20.0

            label("测试工具") {
                style = "-fx-font-size: 50;-fx-font-weight: BOLD;"
                alignment = Pos.CENTER
            }

            label("${Version.name}") {
                alignment = Pos.CENTER
            }
            btn = jfxbutton("GNSS参考站 单板测试") {
                defaultConfig()
                action {
                    GnssTestData.testStatus = TestStatus.TEST_STATUS_PRO
                    openNewStage("GNSS参考站", JobNumView())
                    close()
                }
            }
            jfxbutton("GNSS参考站 整机测试") {
                defaultConfig()
                action {
                    GnssTestData.testStatus = TestStatus.TEST_STATUS_TOTAL
                    openNewStage("GNSS参考站", JobNumView())
                    close()
                }
            }
            jfxbutton("GNSS参考站 老化测试") {
                defaultConfig()
                action {
                    GnssTestData.testStatus = TestStatus.TEST_STATUS_OLD
                    openNewStage("GNSS参考站", JobNumView())
                    close()
                }
            }
            jfxbutton("GNSS参考站 打包测试") {
                defaultConfig()
                action {
                    GnssTestData.testStatus = TestStatus.TEST_STATUS_PACKAGE
                    openNewStage("GNSS参考站", JobNumView())
                    close()
                }
            }
//            jfxbutton("MAVLink  测试") {
//                defaultConfig()
//                action {
//                    openNewStage("MAVLink  测试", MavLinkView())
//                    close()
//                }
//            }

        }
    }

    private fun Control.defaultConfig() {
        style = "-fx-font-size: 30;"
        minWidth = 220.0
        minHeight = 70.0

    }
}