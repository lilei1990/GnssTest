package com.example.demo.view.test.gnss

import com.alibaba.fastjson.JSON
import com.example.demo.model.UDP_Msg
import com.example.demo.model.UDP_Msg0101
import com.example.demo.view.test.CallBacks
import com.example.demo.view.test.UDPBroadcast
import com.example.demo.view.test.bean.Case
import com.example.demo.view.test.bean.OldCase
import gnu.io.SerialPort
import javafx.application.Platform
import kotlinx.coroutines.delay
import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.GSVSentence
import tornadofx.intProperty
import tornadofx.observableListOf
import tornadofx.runAsync
import tornadofx.stringProperty
import java.lang.Thread.sleep

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
    var versionH = stringProperty("")

    //获取软件版本号
    var versionS = stringProperty("")

    //获取镜像版本号
    var versionBsp = stringProperty("")

    //获取镜像版本号
    var sim1ping = intProperty(0)
    var sim2ping = intProperty(0)

    //获取镜像版本号
    var net4g1_imsi = stringProperty("")
    var net4g2_imsi = stringProperty("")

    //用于统计卫星达到标准的数量
    // K卫星编号 V 达到要求次数
    var satelliteMap = mutableMapOf<String, Int>()
    var textInfo = stringProperty("")

    //单板id
    var bid = stringProperty("")


    //按钮测试key
    var key = stringProperty("")
    var chan = intProperty(-1)


    //缓冲数据区域,用于临时存放不通过的,如果持续不通过就会被放入fail
    var ggamap_buff = mutableMapOf<String, OldCase>()

    //不通过的数据
    var ggamap_fail = mutableMapOf<String, OldCase>()

    //gga数据
    var udp_msg0101: UDP_Msg0101? = null
    //数据更新的时间
    var udpUpdataTime = System.currentTimeMillis()
    init {
        runAsync {
            //定时检测数据是否一直持续收到上报数据
            while (true) {
                if (System.currentTimeMillis() - udpUpdataTime > 2000) {
                    reset()
                    sleep(2000)
                }
            }
        }
    }

    //卫星数据udp广播监听
    val broadcast: UDPBroadcast =
            UDPBroadcast(GnssConfig.udp_broadcast_port.value.toInt()).run().setListener(object : CallBacks {
                override fun send(it: UDP_Msg, address: String) {
                    when (it.id.toInt()) {
                        0x0101 -> {
                            udpUpdataTime = System.currentTimeMillis()
//                            println(it.str)
                            val msg0101 = JSON.parseObject<UDP_Msg0101>(it.str, UDP_Msg0101::class.java)
                            Platform.runLater {
                                udp_msg0101 = msg0101
                                versionH.value = msg0101.hw
                                versionS.value = msg0101.sw
                                versionBsp.value = msg0101.bsp
                                sim1ping.value = msg0101.net4g1_ping.toInt()
                                sim2ping.value = msg0101.net4g2_ping.toInt()
                                net4g1_imsi.value = msg0101.net4g1_imsi
                                net4g2_imsi.value = msg0101.net4g2_imsi
                                key.value = msg0101.key
                                chan.value = msg0101.chan
                                var star = ""
                                satelliteMap.forEach { t, u ->
                                    if (u > 0) {
                                        star += "\n$t-$u"
                                    }
                                }
                                textInfo.value = "硬件版本号:${msg0101.hw}\n" +
                                        "镜像版本号:${msg0101.bsp}\n" +
                                        "软件版本号:${msg0101.sw}\n" +
                                        "id:${msg0101.getIdHex()}\n" +
                                        "bid:${msg0101.getBidHex()}\n" +
                                        "信道:${msg0101.chan}\n" +
//                                        "卫星:${star}\n" +
                                        "lora收:${msg0101.loraCounter_rec}\n" +
                                        "lora发:${msg0101.loraCounter_send}\n" +
                                        "4g1:ping通${msg0101.net4g1_ping}次\n" +
                                        "4g2:ping通${msg0101.net4g2_ping}次\n" +
                                        "4g1imsi:${msg0101.net4g1_imsi}\n" +
                                        "4g2imsi:${msg0101.net4g2_imsi}\n"
                                //老化测试做数据匹配
                                if (ggamap_fail.contains(address)) {
                                    ggamap_fail[address]?.equipmentId = msg0101.getIdHex()
                                    ggamap_fail[address]?.bid = msg0101.getBidHex()
                                }
                                //老化测试做数据匹配
                                if (ggamap_buff.contains(address)) {
                                    ggamap_buff[address]?.equipmentId = msg0101.getIdHex()
                                    ggamap_buff[address]?.bid = msg0101.getBidHex()
                                }
                            }
                        }
                        0x0102 -> {
                            val sf = SentenceFactory.getInstance()
                            for (line in it.str.lines()) {
                                try {
                                    val createParser = sf.createParser(line)
                                    if (createParser is GGASentence) {
                                        println("${it.str}---")
                                        val gsvSentence = createParser as GGASentence
                                        val satelliteCount = gsvSentence.satelliteCount//卫星数量
                                        val currentTimeMillis = System.currentTimeMillis()
                                        if (ggamap_fail.contains(address)) {//这个设备一旦被打入冷宫就永远不处理
                                            return
                                        }
                                        if (!ggamap_buff.contains(address)) {//如果第一次进来,直接添加到数据到buff
                                            val oldCase = OldCase()
                                            oldCase.ip = address
                                            oldCase.time = currentTimeMillis
                                            oldCase.satelliteCount = satelliteCount
                                            ggamap_buff[address] = oldCase
                                            return
                                        } else if (satelliteCount < GnssConfig.gps_test_min_satellite_Count.value) {//卫星数量小于设定值
                                            ggamap_buff[address]?.result = false
                                            ggamap_buff[address]?.satelliteCount = satelliteCount
                                            //如果上一次是不通过状态,就彻底不通过
                                            //且时间超过一定的时间
                                            if ((currentTimeMillis - ggamap_buff[address]?.time!!) > GnssConfig.gga_timeout.value) {
                                                ggamap_buff[address]?.result = false
                                                ggamap_fail[address] = ggamap_buff[address]!!
                                                ggamap_buff.remove(address)
                                            }
                                        } else {//卫星数量大于设定值
                                            ggamap_buff[address]?.result = true
                                            ggamap_buff[address]?.time = currentTimeMillis
                                            ggamap_buff[address]?.satelliteCount = satelliteCount

                                        }
                                    }
                                } catch (e: Exception) {

                                }
                            }

                        }
                    }
                }
            })

    fun reset() {

        hd100Case.clear()

        //获取硬件版本号
        versionH.value = ""

        //获取软件版本号
        versionS.value = ""

        //获取镜像版本号
        versionBsp.value = ""

        //获取镜像版本号
        sim1ping.value = 0
        sim2ping.value = 0

        //用于统计卫星达到标准的数量
        // K卫星编号 V 达到要求次数
        satelliteMap = mutableMapOf<String, Int>()
        textInfo.value = ""

        //id
        bid.value = ""


        //按钮测试key
        key.value = ""


        //缓冲数据区域,用于临时存放不通过的,如果持续不通过就会被放入fail
        ggamap_buff.clear()

        //gga数据
        ggamap_fail.clear()
        udp_msg0101 = null
    }
}
