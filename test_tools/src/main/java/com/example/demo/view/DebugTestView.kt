package com.example.demo.view

import com.example.demo.view.test.UdpUtlis
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.gnss.GnssConfig
import com.example.demo.view.test.gnss.GnssTestData
import kfoenix.jfxbutton
import kotlinx.coroutines.delay
import tornadofx.*

class DebugTestView : View("My View") {
    override val root = vbox {
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
    }
}
