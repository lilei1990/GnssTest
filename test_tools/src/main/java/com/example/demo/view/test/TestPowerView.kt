package com.example.demo.view.test

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
class TestPowerView(case: Case, call: () -> Unit) : View("掉电保护") {

    override var root = vbox {
       alignment=Pos.CENTER
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
            text("掉\n电\n保\n护\n是\n否\n通\n过\n测\n试")
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
