package com.example.demo.view

import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.PingUtils
import com.example.demo.utils.showProgressStage
import com.example.demo.view.test.UdpUtlis
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.bean.GnssCase
import com.example.demo.view.test.gnss.*
import javafx.geometry.Pos
import kfoenix.jfxbutton
import kfoenix.jfxtextarea
import kotlinx.coroutines.delay
import tornadofx.*
import java.lang.Thread.sleep

class DebugTestView : View("Debug") {

    val controller: DebugController by inject()
    override val root = vbox {
        //载入样式表`
        stylesheets.add("/css/jfoenix-components.css")
        stylesheets.add("/css/jfoenix-main-demo.css")
        alignment= Pos.CENTER
        prefWidth = 400.0
        prefHeight = 400.0
        spacing = 10.0
        jfxbutton("测试loar接受") {
            action {
                UdpUtlis.clearLoar()
                UdpUtlis.testLoraSend(GnssTestData.serialPort2!!, Case())
            }
        }

        jfxbutton("测试loar发送") {
            action {
                UdpUtlis.clearLoar()
                UdpUtlis.recLoar(1, 50, GnssConfig.lora_test_Intervals.value)
            }
        }

        jfxbutton("升级正式版本") {
            action {
                val showProgressStage = showProgressStage("升级中....")
                showProgressStage.show()
                runAsync {
                    controller.upgradeRelease()
                } ui {
                    showProgressStage.close()
                }

            }
        }

        jfxbutton("升级测试版本") {
            action {
                val showProgressStage = showProgressStage("升级中....")
                showProgressStage.show()
                runAsync {
                    controller.upgradeTest()
                } ui {
                    showProgressStage.close()
                }

            }
        }
        jfxtextarea(controller.taLog) {
            prefWidth = 500.0

        }
    }

}
