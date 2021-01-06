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
import kotlinx.coroutines.runBlocking
import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.GSVSentence
import tornadofx.intProperty
import tornadofx.observableListOf
import tornadofx.runAsync
import tornadofx.stringProperty
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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

    var textInfo = stringProperty("")

    //单板id
    var bid = stringProperty("")


    //按钮测试key
    var key = stringProperty("")
    var chan = intProperty(-1)


    //缓冲数据区域,用于临时存放不通过的,如果持续不通过就会被放入fail
    var ggamap_buff = ConcurrentHashMap<String, OldCase>()


    //gga数据
    var udp_msg0101: UDP_Msg0101? = null

    //数据更新的时间
    var udpUpdataTime = System.currentTimeMillis()

    init {
        runAsync {

            //定时检测数据是否一直持续收到上报数据
            while (true) {
                sleep(1000)
                val currentTimeMillis = System.currentTimeMillis()
                //检测0x0101数据
                if (currentTimeMillis - udpUpdataTime > 2000) {
                    reset()
                }
                //检测0x0102数据
                ggamap_buff.forEach { (key, data) ->//超过5秒没有获取到数据就卫星数值为零
//                    println("设备id-${data.toString()}-${data.equipmentId}-${data.satelliteCount}")
                    if (currentTimeMillis - data.time > GnssConfig.gga_timeout.value) {//掉线或者卫星数不达标
                        data.testInfo = "掉线超时"
                        data.status = 0
                        data.result = false
                    } else if (data.satelliteCount > GnssConfig.gps_test_min_satellite_Count.value) {////卫星数量大于设定值,数据是最新数据
                        data.result = true
                        data.status = 2
                        data.testInfo = "达标!"
                    } else {
                        data.status = 1
                        data.testInfo = "卫星波动!"
                    }
                }
            }
        }
    }

    //卫星数据udp广播监听
    val broadcast: UDPBroadcast =
            UDPBroadcast(GnssConfig.udp_broadcast_port.value.toInt()).run().setListener(object : CallBacks {
                override fun send(it: UDP_Msg, address: String) {
                    udpUpdataTime = System.currentTimeMillis()
                    when (it.id.toInt()) {
                        0x0101 -> {
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
                                var star = 0
                                ggamap_buff[address]?.apply {
                                    star = this.satelliteCount
                                }
                                textInfo.value = "硬件版本号:${msg0101.hw}\n" +
                                        "镜像版本号:${msg0101.bsp}\n" +
                                        "软件版本号:${msg0101.sw}\n" +
                                        "id:${msg0101.getIdHex()}\n" +
                                        "bid:${msg0101.getBidHex()}\n" +
                                        "信道:${msg0101.chan}\n" +
                                        "卫星:${star}\n" +
                                        "lora收:${msg0101.loraCounter_rec}\n" +
                                        "lora发:${msg0101.loraCounter_send}\n" +
                                        "4g1:ping通${msg0101.net4g1_ping}次\n" +
                                        "4g2:ping通${msg0101.net4g2_ping}次\n" +
                                        "4g1imsi:${msg0101.net4g1_imsi}\n" +
                                        "4g2imsi:${msg0101.net4g2_imsi}\n"


                                //老化测试做数据匹配
                                ggamap_buff.get(address)?.apply {
                                    this.equipmentId = msg0101.getIdHex()
                                    this.bid = msg0101.getBidHex()
                                }

                            }
                        }
                        0x0102 -> {
                            val sf = SentenceFactory.getInstance()
                            for (line in it.str.lines()) {
                                try {
                                    val createParser = sf.createParser(line)
                                    if (createParser is GGASentence) {
//                                        println("${it.str}---")
                                        val gsvSentence = createParser as GGASentence
                                        val satelliteCount = gsvSentence.satelliteCount//卫星数量
                                        val currentTimeMillis = System.currentTimeMillis()
                                        if (!ggamap_buff.containsKey(address)) {//如果第一次进来,直接添加到数据到buff
                                            val oldCase = OldCase()
                                            oldCase.ip = address
                                            oldCase.time = currentTimeMillis
                                            oldCase.satelliteCount = satelliteCount
                                            ggamap_buff.put(address, oldCase)
                                            return
                                        } else {//不是第一次进来
                                            ggamap_buff.get(address)?.apply {//更新数据
                                                if (this.status != 0) {//如果彻底失败就不再更新数据
                                                    this.satelliteCount = satelliteCount
                                                    this.time = currentTimeMillis
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


        textInfo.value = ""

        //id
        bid.value = ""


        //按钮测试key
        key.value = ""



        udp_msg0101 = null
    }
}
