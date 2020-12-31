package com.example.demo.view.test.gnss

import com.example.demo.utils.LoggerUtil
import com.example.demo.view.Ini4jUtils
import com.example.demo.view.test.bean.IniFileEntity
import tornadofx.booleanProperty
import tornadofx.intProperty
import tornadofx.longProperty
import tornadofx.stringProperty
import java.io.File
import java.util.*

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object GnssConfig {
    //常规配置
    var config_path = System.getProperty("user.dir") + "\\config\\"
    var defaut_timeOut = longProperty(10000)
    var debug = booleanProperty(true)

    //工号
    var userId = stringProperty("00001")

    //测试设备的ip
    var eth_test_ip = stringProperty("192.168.1.252")
    //udp

    var udp_broadcast_port = intProperty(50250)

    //wifi
    var wifi_test_ip = stringProperty("172.30.3.1")
    var wifi_test_pwd = stringProperty("1234567890")

    //lora测试间隔频率
    var lora_test_Intervals = intProperty(1)

    //lora 信道
    var lora_test_chen = intProperty(65)

    //lora 信号强度
    var lora_test_strength = intProperty(20)

    //lora测试次数
    var lora_test_count = intProperty(10)

    //gps
    //信噪测试最低阈值
    var gps_test_min_noise = intProperty(40)

    //达标的最少卫星数量
    var gps_test_min_satellite_Count = intProperty(20)

    //达标的最少数值,出现满足信噪合格的次数
    var gps_test_min_num = intProperty(20)

    //按键测试最多测试多少次
    var test_key_num = intProperty(5)

    //gga/ 卫星数掉下来达到次时间认定失败
    var gga_timeout = intProperty(10000)

    //打开的串口
    val comboText1 = stringProperty("")
    val comboText2 = stringProperty("")
    fun save() {
        val list = listOf(
                IniFileEntity("ldap", "config_path", "$config_path"),
                IniFileEntity("ldap", "debug", "${debug.value}"),
                IniFileEntity("ldap", "defaut_timeOut", "${defaut_timeOut.value}"),
                IniFileEntity("ldap", "userId", "${userId.value}"),
                IniFileEntity("ldap", "eth_test_ip", "${eth_test_ip.value}"),
                IniFileEntity("ldap", "udp_broadcast_port", "${udp_broadcast_port.value}"),
                IniFileEntity("ldap", "wifi_test_ip", "${wifi_test_ip.value}"),
                IniFileEntity("ldap", "wifi_test_pwd", "${wifi_test_pwd.value}"),
                IniFileEntity("ldap", "lora_test_Intervals", "${lora_test_Intervals.value}"),
                IniFileEntity("ldap", "lora_test_chen", "${lora_test_chen.value}"),
                IniFileEntity("ldap", "lora_test_strength", "${lora_test_strength.value}"),
                IniFileEntity("ldap", "lora_test_count", "${lora_test_count.value}"),
                IniFileEntity("ldap", "gps_test_min_noise", "${gps_test_min_noise.value}"),
                IniFileEntity("ldap", "gps_test_min_satellite_Count", "${gps_test_min_satellite_Count.value}"),
                IniFileEntity("ldap", "gps_test_min_num", "${gps_test_min_num.value}"),
                IniFileEntity("ldap", "test_key_num", "${test_key_num.value}"),
                IniFileEntity("ldap", "gga_timeout", "${gga_timeout.value}"),
                IniFileEntity("ldap", "comboText1", "${comboText1.value}"),
                IniFileEntity("ldap", "comboText2", "${comboText2.value}")

        )
        Ini4jUtils.updateIniFile("${config_path}config.ini", list)
    }

    init {
        try {
            read()
        } catch (e: Exception) {
            e.printStackTrace()
            LoggerUtil.LOGGER.debug("读取配置错误")
        }
    }

    fun read() {

        val fileContent: MutableMap<String, List<String>> = HashMap()
        fileContent["ldap"] = listOf(
                "config_path",
                "debug",
                "defaut_timeOut",
                "userId",
                "eth_test_ip",
                "udp_broadcast_port",
                "wifi_test_ip",
                "wifi_test_pwd",
                "lora_test_Intervals",
                "lora_test_chen",
                "lora_test_strength",
                "lora_test_count",
                "gps_test_min_noise",
                "gps_test_min_satellite_Count",
                "gps_test_min_num",
                "test_key_num",
                "gga_timeout",
                "comboText1",
                "comboText2"
        )
        val file = File("${config_path}config.ini")
        val readIniFile = Ini4jUtils.readIniFile(file, fileContent)
        val getldap = readIniFile.get("ldap")
        config_path = getldap!!.get("config_path")!!
        debug.value = getldap!!.get("debug")!!.toBoolean()
        defaut_timeOut.value = getldap!!.get("defaut_timeOut")!!.toLong()
        userId.value = getldap!!.get("userId")
        eth_test_ip.value = getldap!!.get("eth_test_ip")
        udp_broadcast_port.value = getldap!!.get("udp_broadcast_port")!!.toInt()
        wifi_test_ip.value = getldap!!.get("wifi_test_ip")
        wifi_test_pwd.value = getldap!!.get("wifi_test_pwd")
        lora_test_Intervals.value = getldap!!.get("lora_test_Intervals")!!.toInt()
        lora_test_chen.value = getldap!!.get("lora_test_chen")!!.toInt()
        lora_test_strength.value = getldap!!.get("lora_test_strength")!!.toInt()
        lora_test_count.value = getldap!!.get("lora_test_count")!!.toInt()
        gps_test_min_noise.value = getldap!!.get("gps_test_min_noise")!!.toInt()
        gps_test_min_satellite_Count.value = getldap!!.get("gps_test_min_satellite_Count")!!.toInt()
        gps_test_min_num.value = getldap!!.get("gps_test_min_num")!!.toInt()
        test_key_num.value = getldap!!.get("test_key_num")!!.toInt()
        gga_timeout.value = getldap!!.get("gga_timeout")!!.toInt()
        comboText1.value = getldap!!.get("comboText1")!!
        comboText2.value = getldap!!.get("comboText2")!!
    }
}