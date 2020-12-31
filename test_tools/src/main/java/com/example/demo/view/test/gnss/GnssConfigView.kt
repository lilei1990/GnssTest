package com.example.demo.view.test.gnss

import com.example.demo.utils.showSnackbar
import com.example.demo.view.test.gnss.GnssConfig.comboText1
import com.example.demo.view.test.gnss.GnssConfig.comboText2
import com.example.demo.view.test.gnss.GnssConfig.config_path
import com.example.demo.view.test.gnss.GnssConfig.debug
import com.example.demo.view.test.gnss.GnssConfig.defaut_timeOut
import com.example.demo.view.test.gnss.GnssConfig.eth_test_ip
import com.example.demo.view.test.gnss.GnssConfig.gga_timeout
import com.example.demo.view.test.gnss.GnssConfig.gps_test_min_noise
import com.example.demo.view.test.gnss.GnssConfig.gps_test_min_num
import com.example.demo.view.test.gnss.GnssConfig.gps_test_min_satellite_Count
import com.example.demo.view.test.gnss.GnssConfig.userId
import com.example.demo.view.test.gnss.GnssConfig.lora_test_Intervals
import com.example.demo.view.test.gnss.GnssConfig.lora_test_chen
import com.example.demo.view.test.gnss.GnssConfig.lora_test_count
import com.example.demo.view.test.gnss.GnssConfig.lora_test_strength
import com.example.demo.view.test.gnss.GnssConfig.test_key_num
import com.example.demo.view.test.gnss.GnssConfig.udp_broadcast_port
import com.example.demo.view.test.gnss.GnssConfig.wifi_test_ip
import com.example.demo.view.test.gnss.GnssConfig.wifi_test_pwd
import com.jfoenix.controls.JFXCheckBox
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import tornadofx.*

/**
 * 作者 : lei
 * 时间 : 2020/12/19.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class GnssConfigView : View("设置") {
    override val root = tabpane {
//        tab<Screen1>()
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab<Config>() {
        }
    }
}


class Config : Fragment("设置") {
    lateinit var config_path_View: TextField
    lateinit var debug_View: JFXCheckBox
    lateinit var defaut_timeOut_View: TextField
    lateinit var userId_View: TextField
    lateinit var eth_test_ip_View: TextField
    lateinit var udp_broadcast_port_View: TextField
    lateinit var wifi_test_ip_View: TextField
    lateinit var wifi_test_pwd_View: TextField
    lateinit var lora_test_Intervals_View: TextField
    lateinit var lora_test_chen_View: TextField
    lateinit var lora_test_strength_View: TextField
    lateinit var lora_test_count_View: TextField
    lateinit var gps_test_min_noise_View: TextField
    lateinit var gps_test_min_satellite_Count_View: TextField
    lateinit var gps_test_min_num_View: TextField
    lateinit var test_key_num_View: TextField
    lateinit var gga_timeout_View: TextField
    lateinit var comboText1_View: TextField
    lateinit var comboText2_View: TextField
    override val root = vbox {
        paddingAll = 15
        prefWidth = 500.0
        prefHeight = 500.0
        label("常规")
        hbox {
            alignment = Pos.CENTER_LEFT
            label("配置文件路径:  ") {
                pubPor()
            }
            debug_View = jfxcheckbox(GnssConfig.debug) {
                selectedProperty().addListener { observable, oldValue, newValue ->
                    println(newValue)
                    GnssConfig.save()
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("配置文件路径:  ") {
                pubPor()
            }
            config_path_View = textfield("${GnssConfig.config_path}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT

            label("默认超时时间:  ") {
                pubPor()
            }
            defaut_timeOut_View = textfield("${defaut_timeOut.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }

        hbox {

            alignment = Pos.CENTER_LEFT
            label("eth测试ip地址:  ") {
                pubPor()
            }
            eth_test_ip_View = textfield("${eth_test_ip.value}") {

                textProperty().addListener { obs, old, new ->

                }
            }
        }
        hbox {

            alignment = Pos.CENTER_LEFT
            label("工号:  ") {
                pubPor()
            }
            userId_View = textfield("${userId.value}") {

                textProperty().addListener { obs, old, new ->

                }
            }
        }
        label("udp")
        hbox {
            alignment = Pos.CENTER_LEFT
            label("udp测试端口:  ") {
                pubPor()
            }
            udp_broadcast_port_View = textfield("${udp_broadcast_port.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        label("wifi")
        hbox {
            alignment = Pos.CENTER_LEFT
            label("wifi_ping测试IP地址:  ") {
                pubPor()
            }
            wifi_test_ip_View = textfield("${wifi_test_ip.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("wifi_ping测试密码:  ") {
                pubPor()
            }
            wifi_test_pwd_View = textfield("${wifi_test_pwd.value}") {
                textProperty().addListener { obs, old, new ->

                }
            }
        }
        label("lora")
        hbox {
            alignment = Pos.CENTER_LEFT
            label("lora测试间隔频率秒:  ") {
                pubPor()
            }
            lora_test_Intervals_View = textfield("${lora_test_Intervals.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("lora 信道:  ") {
                pubPor()
            }
            lora_test_chen_View = textfield("${lora_test_chen.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("lora 信号强度:  ") {
                pubPor()
            }
            lora_test_strength_View = textfield("${lora_test_strength.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }

        hbox {
            alignment = Pos.CENTER_LEFT
            label("测试次数:  ") {
                pubPor()
            }
            lora_test_count_View = textfield("${lora_test_count.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        label("gps")
        hbox {
            alignment = Pos.CENTER_LEFT
            label("信噪测试最低阈值:  ") {
                pubPor()
            }
            gps_test_min_noise_View = textfield("${gps_test_min_noise.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("达标的最少卫星数量:  ") {
                pubPor()
            }
            gps_test_min_satellite_Count_View = textfield("${gps_test_min_satellite_Count.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("满足信噪合格的最低次数:  ") {
                pubPor()
            }
            gps_test_min_num_View = textfield("${gps_test_min_num.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("按键测试最多测试多少次:  ") {
                pubPor()
            }
            test_key_num_View = textfield("${test_key_num.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("卫星数掉下认定失败时间:  ") {
                pubPor()
            }
            gga_timeout_View = textfield("${gga_timeout.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("串口1:  ") {
                pubPor()
            }
            comboText1_View = textfield("${comboText1.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        hbox {
            alignment = Pos.CENTER_LEFT
            label("串口2:  ") {
                pubPor()
            }
            comboText2_View = textfield("${comboText2.value}") {
                textProperty().addListener { obs, old, new ->
                }
            }
        }
        jfxbutton {
            text = "保存"
            action {

                if (onlyNum(defaut_timeOut_View.text)) return@action
                defaut_timeOut.value = defaut_timeOut_View.text.toLong()
                userId.value = userId_View.text
                debug.value = debug_View.isSelected
                config_path = config_path_View.text
                if (onlyIp(eth_test_ip_View.text)) return@action
                eth_test_ip.value = eth_test_ip_View.text
                if (onlyNum(udp_broadcast_port_View.text)) return@action
                udp_broadcast_port.value = udp_broadcast_port_View.text.toInt()
                if (onlyIp(wifi_test_ip_View.text)) return@action
                wifi_test_ip.value = wifi_test_ip_View.text
                wifi_test_pwd.value = wifi_test_pwd_View.text
                if (onlyNum(lora_test_Intervals_View.text)) return@action
                lora_test_Intervals.value = lora_test_Intervals_View.text.toInt()
                if (onlyNum(lora_test_chen_View.text)) return@action
                lora_test_chen.value = lora_test_chen_View.text.toInt()
                if (onlyNum(lora_test_strength_View.text)) return@action
                lora_test_strength.value = lora_test_strength_View.text.toInt()
                if (onlyNum(lora_test_count_View.text)) return@action
                lora_test_count.value = lora_test_count_View.text.toInt()
                if (onlyNum(gps_test_min_noise_View.text)) return@action
                gps_test_min_noise.value = gps_test_min_noise_View.text.toInt()
                if (onlyNum(gps_test_min_satellite_Count_View.text)) return@action
                gps_test_min_satellite_Count.value = gps_test_min_satellite_Count_View.text.toInt()
                if (onlyNum(gps_test_min_num_View.text)) return@action
                gps_test_min_num.value = gps_test_min_num_View.text.toInt()
                if (onlyNum(test_key_num_View.text)) return@action
                test_key_num.value = test_key_num_View.text.toInt()
                if (onlyNum(gga_timeout_View.text)) return@action
                gga_timeout.value = gga_timeout_View.text.toInt()
                comboText1.value = comboText1_View.text
                comboText2.value = comboText2_View.text
                GnssConfig.save()
                GnssConfig.read()

                close()
            }
        }
    }

    private fun onlyNum(new: String): Boolean {
        var regex = "[1-9]\\d*"
        if (!new.matches(Regex(regex))) {
            showSnackbar("${new}只能键入数字")
            return true
        }
        return false
    }


    private fun onlyIp(new: String): Boolean {
        var regex =
                "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"
        if (!new.matches(Regex(regex))) {
            showSnackbar("${new}值的格式不正确")
            return true
        }
        return false
    }

    private fun Label.pubPor() {
        textFill = Color.BLUE
    }
}