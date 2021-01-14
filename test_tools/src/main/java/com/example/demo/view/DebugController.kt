package com.example.demo.view

import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.PingUtils
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.UdpUtlis
import com.example.demo.view.test.bean.StopTest
import com.example.demo.view.test.gnss.GnssTestData
import javafx.application.Platform
import javafx.scene.control.Alert
import tornadofx.Controller
import tornadofx.stringProperty

class DebugController : Controller() {
    var taLog = stringProperty()
    fun putLogInfo(str: String) {
        Platform.runLater {
            taLog.value = "${TimeUtil.getSimpleDateTime()}-$str\n${taLog.value}"
        }
    }

    fun upgradeRelease() {
        try {
            val releasVer = UdpUtlis.getGnssVer(UdpUtlis.updateReleasePath)
            putLogInfo("测试软件版本:${GnssTestData.versionS.value}")
            putLogInfo("正式软件版本:${releasVer}")
            if (GnssTestData.versionS.value != releasVer) {//需要升级
                putLogInfo("版本不匹配升级中!")
                Api.upgrade(UdpUtlis.updateReleasePath) {
                    putLogInfo("升级结果!$it")
                    if (it) {
                        putLogInfo("升级成功,重启设备")
                        //升级成功,重启设备
                        Api.resystem {
                            if (it.isResult) {
                                putLogInfo("重启完成")
                            }
                        }
                    } else {
                        putLogInfo("升级失败")
                    }
                }

                val startTime = System.currentTimeMillis()
                var ping = PingUtils.ping("192.168.1.252", 1, 1)
                while (!ping) {
                    Thread.sleep(1000)
                    ping = PingUtils.ping("192.168.1.252", 1, 1)

                    if (System.currentTimeMillis() - startTime > 60000) {//超时跳出循环
                        putLogInfo("超时跳出循环")
                        break
                    }
                }
                Api.getConfig1() {
                    var statistic601 = it
                    statistic601.isResult.apply {

                        if (this) {
                            val deviceVersion = it.`object`.deviceVersion
                            if (releasVer == deviceVersion) {
                                putLogInfo("正式版本更换完成!当前软件版本:$deviceVersion")
                                showAlert("成功")
                            } else {
                                putLogInfo("查询版本不一致!当前软件版本:$deviceVersion")
                                showAlert("查询版本不一致")
                            }

                        } else {
                            putLogInfo("版本更换失败!")
                            showAlert("失败")
                        }

                    }
                }

            }
        } catch (e: Exception) {

        }
    }

    private fun showAlert(str:String) {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "$str"
            alert.headerText = null
            alert.contentText = "版本更换$str!"
            alert.showAndWait()

        }
    }
}