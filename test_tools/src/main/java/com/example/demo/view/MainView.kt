package com.example.demo.view

import com.example.demo.model.TestStatus
import com.example.demo.utils.openNewStage
import com.example.demo.utils.shwoInternalWindow
import com.example.demo.view.mavlink.MavLinkView
import com.example.demo.view.test.*
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

    //根节点
    override val root = vbox {
        menubar {
            menu("设置") {
                item("设置") {
                    action {
                        shwoInternalWindow(gnssConfigView,escapeClosesWindow = true,closeButton = true)
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
            jfxbutton("GNSS参考站 单板测试") {
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
            jfxbutton("MAVLink  测试") {
                defaultConfig()
                action {
                    openNewStage("MAVLink  测试", MavLinkView())
                    close()
                }
            }
        }
    }

    private fun Control.defaultConfig() {
        style = "-fx-font-size: 30;"
        minWidth = 220.0
        minHeight = 70.0

    }
}