package com.example.demo.view.test

import com.alibaba.fastjson.JSON
import com.example.demo.model.TestStatus
import com.example.demo.model.UDP_Msg
import com.example.demo.model.UDP_Msg0101
import com.example.demo.view.test.center.Case
import gnu.io.SerialPort
import javafx.application.Platform
import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.GSVSentence
import tornadofx.intProperty
import tornadofx.observableListOf
import tornadofx.stringProperty

/**
 * 作者 : lei
 * 时间 : 2020/12/17.
 * 邮箱 :416587959@qq.com
 * 描述 :测试过程中的所有数据
 */
object GnssTestData {


    var testStatus = ""
    var serialPort1: SerialPort? = null
    var serialPort2: SerialPort? = null
    var hd100Case = observableListOf<Case>()

    //获取硬件版本号
    val versionH = stringProperty("")

    //获取软件版本号
    val versionS = stringProperty("")

    //获取镜像版本号
    val versionBsp = stringProperty("")

    //获取镜像版本号
    val sim1ping = intProperty(0)
    val sim2ping = intProperty(0)

    //用于统计卫星达到标准的数量
    // K卫星编号 V 达到要求次数
    val satelliteMap = mutableMapOf<String, Int>()
    val textInfo = stringProperty("")

    //单板id
    var bid = stringProperty("")

    //整机
    var id = stringProperty("")

    //按钮测试key
    var key = stringProperty("")

    //gga数据
    var ggamap_pass = mutableMapOf<String, GGASentence>()

    //缓冲数据区域,用于临时存放不通过的,如果持续不通过就会被放入fail
    var ggamap_buff = mutableMapOf<String, Long>()

    //gga数据
    var ggamap_fail = mutableMapOf<String, Int>()

    //卫星数据udp广播监听
    val broadcast: UDPBroadcast =
            UDPBroadcast(GnssConfig.udp_broadcast_port.value.toInt()).run().setListener(object : CallBacks {
                override fun send(it: UDP_Msg, address: String) {
                    when (it.id.toInt()) {
                        0x0101 -> {
                            val msg0101 = JSON.parseObject<UDP_Msg0101>(it.str, UDP_Msg0101::class.java)
                            Platform.runLater {
                                versionH.value = msg0101.hw
                                versionS.value = msg0101.sw
                                versionBsp.value = msg0101.bsp
                                sim1ping.value = msg0101.net4g1_ping.toInt()
                                sim2ping.value = msg0101.net4g2_ping.toInt()
                                key.value = msg0101.key
                                var star = ""
                                satelliteMap.forEach { t, u ->
                                    if (u > 0) {
                                        star += "\n$t-$u"
                                    }
                                }
                                textInfo.value = "硬件版本号:${msg0101.hw}\n" +
                                        "镜像版本号:${msg0101.bsp}\n" +
                                        "软件版本号:${msg0101.sw}\n" +
                                        "id:${msg0101.id}\n" +
                                        "bid:${msg0101.bid}\n" +
                                        "信道:${msg0101.chan}\n" +
                                        "卫星:${star}\n" +
                                        "4g1:ping通${msg0101.net4g1_ping}次\n" +
                                        "4g2:ping通${msg0101.net4g2_ping}次\n"
                                if (ggamap_fail.contains(address)) {
                                    ggamap_fail[address]=msg0101.id
                                }
                            }
                        }
                        0x0102 -> {

                            val sf = SentenceFactory.getInstance()
                            for (line in it.str.lines()) {

                                try {
                                    val createParser = sf.createParser(line)
                                    if (createParser is GSVSentence) {
                                        val gsvSentence = createParser as GSVSentence
                                        val satelliteInfos = gsvSentence.satelliteInfo//卫星信息
                                        gsvSentence.satelliteCount//卫星数量
                                        for (satelliteInfo in satelliteInfos) {
                                            var vlue = satelliteMap.get(satelliteInfo.id) ?: 0

                                            if (satelliteInfo.noise > GnssConfig.gps_test_min_noise.value.toInt()) {
                                                vlue++
                                            }
                                            satelliteMap.put(satelliteInfo.id, vlue)
//                                    satelliteInfo.azimuth//方位角
//                                    satelliteInfo.elevation//海拔
//                                    satelliteInfo.noise//信噪比
//                                    satelliteInfo.id//编号

                                        }
                                    } else if (createParser is GGASentence) {
                                        val sf = SentenceFactory.getInstance()
                                        for (line in it.str.lines()) {
                                            val createParser = sf.createParser(line)
                                            if (createParser is GGASentence) {
                                                val gsvSentence = createParser as GGASentence
                                                val satelliteCount = gsvSentence.satelliteCount//卫星数量
                                                if (ggamap_fail.contains(address)) {//这个设备一旦被打入冷宫就永远不处理
                                                    ggamap_buff.remove(address)
                                                    return
                                                }
                                                if (satelliteCount < GnssConfig.gps_test_min_satellite_Count.value) {//卫星数量小于设定值
                                                    val currentTimeMillis = System.currentTimeMillis()
                                                    if (ggamap_buff.contains(address)) {//如果掉星缓冲区包含这个数据
                                                        //在缓冲区的时间超时还是达不到要求就不通过
                                                        if ((currentTimeMillis - ggamap_buff[address]!!) > GnssConfig.gga_timeout.value) {
                                                            ggamap_fail[address] = -1
                                                        }
                                                    } else {
                                                        ggamap_buff[address] = currentTimeMillis
                                                        ggamap_pass.remove(address)
                                                    }
                                                } else {//卫星数量大于设定值
                                                    ggamap_pass[address] = gsvSentence
                                                    ggamap_buff.remove(address)
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
            })

}
