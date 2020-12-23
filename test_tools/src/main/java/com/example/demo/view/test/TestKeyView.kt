package com.example.demo.view.test

import com.example.demo.utils.showSnackbar
import com.example.demo.view.test.GnssConfig.test_key_num
import com.example.demo.view.test.bean.Case
import javafx.geometry.Pos
import kfoenix.jfxbutton
import tornadofx.*
import java.lang.Thread.sleep

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class TestKeyView(case: Case, call: () -> Unit) : View() {
    val controller: CenterController by inject()
    var index = test_key_num.value.toInt()

    init {
        runAsync {   //进来首先清除按键记录
            UdpUtlis.clearKey()
        }

    }

    override val root = vbox {
        prefHeight = 500.0
        prefWidth = 500.0

        imageview(url = "/img/bg.png") {
            fitWidth = 550.0
            fitHeight = 350.0
        }
        hbox {
            alignment = Pos.CENTER
            text() {
                text = "请按照图中标注的顺序依次按下按键①➡②➡③➡④➡"
            }
            jfxbutton {
                text = "点击验证"
                action {
                    if (GnssTestData.key.value == "1234") {

                        showSnackbar("验证通过")
                        case.result = true

                        runAsync { sleep(2000) } ui {
                            call()
                            close()
                        }
                    } else {
                        index--
                        showSnackbar("验证失败,${index}次后自动判断此项测试不通过")

                        runAsync {
                            UdpUtlis.clearKey()
                        }
                        if (index <= 0) {
                            case.result = false
                            close()
                            call()
                        }
                    }

                }
            }

        }

    }


}
