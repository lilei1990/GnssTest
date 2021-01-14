package com.example.demo.view

import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.PingUtils
import com.example.demo.view.test.UdpUtlis
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.bean.GnssCase
import com.example.demo.view.test.gnss.*
import kfoenix.jfxbutton
import kotlinx.coroutines.delay
import tornadofx.*
import java.lang.Thread.sleep

class DebugTestView : View("My View") {
    override val root = vbox {
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
                try {
                    val releasVer = UdpUtlis.getGnssVer(UdpUtlis.updateReleasePath)
                    var controller = GnssTestView.gnssTestView.controller
                    controller.putLogInfo("测试软件版本:${GnssTestData.versionS.value}")
                    controller.putLogInfo("正式软件版本:${releasVer}")
                    if (GnssTestData.versionS.value != releasVer) {//需要升级
                        controller.putLogInfo("版本不匹配升级中!")
                        UdpUtlis.update(controller, Case(),UdpUtlis.updateReleasePath)
                    }
                } catch (e: Exception) {

                }

            }
        }

        jfxbutton("升级测试版本") {
            action {
                try {
                    val testVer = UdpUtlis.getGnssVer(UdpUtlis.updateTestPath)
                    var controller = GnssTestView.gnssTestView.controller
                    controller.putLogInfo("当前软件版本:${GnssTestData.versionS.value}")
                    controller.putLogInfo("要升级的版本:${testVer}")
                    if (GnssTestData.versionS.value != testVer) {//需要升级
                        controller.putLogInfo("版本不匹配升级中!")
                        UdpUtlis.update(controller, Case(),UdpUtlis.updateTestPath)
                    }

                    var ping = PingUtils.ping("192.168.1.252", 1, 1)
                    if (!ping) {
                        sleep(1000)
                        ping = PingUtils.ping("192.168.1.252", 1, 1)
                    }
                    Api.getConfig1() {
                        var statistic601 = it
                        statistic601.isResult.apply {
                            if (this) {
                                val deviceVersion = it.`object`.deviceVersion
                                if (testVer == deviceVersion) {
                                    controller.putLogInfo("正式版本更换完成!")
                                    controller.putLogInfo("当前软件版本:$deviceVersion")
                                    //获取新的版本号后去完成打印上送操作
                                    controller.fire(ToastEvent(it.`object`.deviceVersion))
                                } else {
                                    controller.putLogInfo("正式版本更换完成!")
                                    controller.putLogInfo("当前软件版本:$deviceVersion")
                                }

                            }

                        }
                    }
                } catch (e: Exception) {

                }


            }
        }
    }

}
