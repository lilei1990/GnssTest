package com.example.demo.view.test.gnss

import com.example.demo.utils.showSnackbar
import com.example.demo.view.test.bean.Case
import javafx.geometry.Pos
import kfoenix.jfxbutton
import tornadofx.*

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class TestLedView(case: Case, call: () -> Unit) : View("Led测试") {

    override var root = vbox {
        paddingAll = 10.0
        imageview(url = "/img/led.png") {
            fitWidth = 400.0
            fitHeight = 200.0
        }
        alignment = Pos.CENTER
        hbox {
            jfxbutton {
                text = "通过"
                action {
                    showSnackbar("通过")
                    case.result = true
                    close()
                    call()
                }
            }
            text("电源灯红色常亮\n网口灯绿色常亮\n电台灯绿色闪烁\n屏幕亮白\n")
            jfxbutton {
                text = "不通过"
                action {
                    showSnackbar("不通过")
                    case.result = false
                    close()
                    call()
                }
            }
        }
    }

}
