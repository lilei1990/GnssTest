package com.example.demo.view.test

import com.example.demo.utils.showSnackbar
import com.example.demo.utils.view.DialogBuilder
import com.example.demo.view.test.center.Case
import javafx.geometry.Pos
import kfoenix.jfxbutton
import tornadofx.*

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class TestLcdView(case: Case, call: () -> Unit) : View("屏幕测试") {
    var open4G1: Boolean = false
    var open4G2: Boolean = false
    var resultBlack = ""
    var resultWhite = ""
    val callback=call
    override fun onDock() {
        super.onDock()
        callback()
    }

    override var root = vbox {
        alignment = Pos.CENTER
        prefWidth = 500.0
        prefHeight = 500.0
        jfxbutton {

            text = "LCD黑"
            action {
                runAsync {
                    UdpUtlis.showLcd(1)
                }
                DialogBuilder(this)
                    .setTitle("提示")
                    .setMessage("是否测试成功")
                    .setPositiveBtn("确定", {
                        isDisable = true
                        resultBlack = "通过"
                        case.putTestInfo("LCD黑=通过")
                        showSnackbar("通过")
                        checkResult(case)
                    }, "#00ff00")
                    .setNegativeBtn("不通过") {
                        resultBlack = "不通过"
                        case.putTestInfo("LCD黑=不通过")
                        showSnackbar("不通过")
                        checkResult(case)
                    }
                    .create();
            }
        }
        text() {
            text = "逐\n个\n测\n试\n⬇\n⬇"
        }
        jfxbutton {
            text = "LCD白"
            action {
                runAsync {
                    UdpUtlis.showLcd(0)
                }
                //tfOutPath是一个控件（controller）
                DialogBuilder(this)
                    .setTitle("提示")
                    .setMessage("是否测试成功")
                    .setPositiveBtn("确定", {
                        isDisable = true
                        resultWhite = "通过"
                        case.putTestInfo("LCD白=通过")
                        showSnackbar("通过")
                        checkResult(case)
                    }, "#00ff00")
                    .setNegativeBtn("不通过") {
                        resultWhite = "不通过"
                        case.putTestInfo("LCD白=不通过")
                        showSnackbar("不通过")
                        checkResult(case)
                    }
                    .create();
            }
        }


//        jfxbutton {
//            text = "4G1关"
//            action {
//
//                UdpUtlis.enable4G1(false)
//            }
//        }
//        jfxbutton {
//            text = "4G1开"
//            action {
//
//                UdpUtlis.enable4G1(true)
//            }
//        }
//        jfxbutton {
//            text = "4G2关"
//            action {//4g1+开关（0-关；1-开）或4g2+开关，例：{"4g1":0}或{"4g2":0}
//                UdpUtlis.enable4G1(false)
//            }
//        }
//        jfxbutton {
//            text = "4G2开"
//            action {//4g1+开关（0-关；1-开）或4g2+开关，例：{"4g1":0}或{"4g2":0}
//                UdpUtlis.enable4G1(true)
//            }
//        }
    }
    fun checkResult(case: Case) {
        if (resultBlack == "通过" && resultWhite == "通过") {
            case.result = true
            close()
            callback()
        } else {//有一个不通过就直接关闭不通过
            if (resultBlack == "不通过"|| resultWhite == "不通过") {
                case.result = false
                close()
                callback()
            }
        }
    }
}
