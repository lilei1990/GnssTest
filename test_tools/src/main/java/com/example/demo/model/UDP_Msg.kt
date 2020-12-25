package com.example.demo.model

import com.alibaba.fastjson.JSON

/**
 * 作者 : lei
 * 时间 : 2020/12/15.
 * 邮箱 :416587959@qq.com
 * 描述 :{
{"mode"：模式（0-电脑测试；1-老化测试；255-未设置）,
"hw":"硬件版本号",
"sw":"软件版本号",
"bsp":"镜像版本号",
"bid":单板ID,
"id":整机ID,
"lcd":lcd显示模式, (0-白；1-黑；255-未设置)
"wifi":wifi开关（0-关；1-开），
"key":"按键记录值","chan":信道,    （0-83可配）
"power":功率, （0-低；1-高）
"4g1":"开关,imsi,ccid,ping通次数", （开关：0-关；1-开）
"4g2":"开关,imsi,ccid,ping通次数"  （开关：0-关；1-开）}
例：{"4g1":"1,460044023303381,89860420011990833381,1834",
"4g2":"1,,,0","bid":806355968,"bsp":"i601s-gnss-imx6ull-v1.2.1",
"chan":9,"hw":"","id":805306385,"key":"","lcd":255,"mode":0,"power":0,
"sw":"1.0.1.Fty01","wifi":1}
 */
//0x0001	单板ID烧录请求
//0x0002	单板ID烧录响应
//0x0003	整机ID烧录请求
//0x0004	整机ID烧录响应
//0x0005	WIFI开关请求
//0x0006	WIFI开关响应
//0x0007	LORA配置请求
//0x0008	LORA配置响应
//0x0009	KEY清空请求
//0x000a	KEY清空响应
//0x000b	LCD显示请求
//0x000c	LCD显示响应
//0x000d	4G开关请求
//0x000e	4G开关响应
//0x0101	状态上报
//0x0102	板卡数据透传

data class UDP_Msg(val id: Short, val len: Short, val str: String) {
//    init {
//        val ob = JSON.parseObject(str, UDP_Msg::class.java)
//    }
}

data class UDP_Msg0101(
        val net4g1: String,
        val net4g2: String,
        val bid: Int,
        val bsp: String,
        val chan: Int,
        val hw: String,
        val loraCounter: String,
        val id: Int,
        val key: String,
        val lcd: Int,
        val mode: Int,
        val power: Int,
        val sw: String,
        val wifi: Int
) {
    lateinit var net4g1_open: String
    lateinit var net4g1_imsi: String
    lateinit var net4g1_ccid: String
    lateinit var net4g1_ping: String
    lateinit var net4g2_open: String
    lateinit var net4g2_imsi: String
    lateinit var net4g2_ccid: String
    lateinit var net4g2_ping: String
    var loraCounter_rec: Int = 0
    var loraCounter_send: Int = 0
    //"开关,imsi,ccid,ping通次数", （开关：0-关；1-开）
// "4g1":"1,460044023303381,89860420011990833381,1834",
//    val _4g1_open: Boolean =true

    init {
        val split1 = net4g1.split(",")
        val split2 = net4g2.split(",")
        val lora = loraCounter.split(",")
        net4g1_open = split1[0]
        net4g1_imsi = split1[1]
        net4g1_ccid = split1[2]
        net4g1_ping = split1[3]

        net4g2_open = split2[0]
        net4g2_imsi = split2[1]
        net4g2_ccid = split2[2]
        net4g2_ping = split2[3]
        loraCounter_rec = lora[0].toInt()
        loraCounter_send = lora[1].toInt()
    }


}
