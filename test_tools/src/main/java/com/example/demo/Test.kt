package com.example.demo

import com.example.demo.model.DeviceTestModel
import com.example.demo.net.Api
import com.example.demo.utils.HexUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import net.sf.marineapi.nmea.util.SatelliteInfo
import tornadofx.runAsync
import tornadofx.toModel
import tornadofx.ui

private val flow = flowOf(1, 2).map {
    it.apply {
        flowOf(3, 4).collect {
            delay(3000)
            println(it)
        }
    }
}

suspend fun main(args: Array<String>) {
    runBlocking {
        flow {
            emit(1)
//            throw RuntimeException()
        }.flowOn(Dispatchers.IO)
                .catch{
                    println("catch exception") }
                .onCompletion { cause ->
                    if (cause != null)
                        println("Flow completed exceptionally")
                    else
                        println("Done")
                }
                .collect { println(it) }
    }
//    val satelliteMap = mutableMapOf<Int, Int>()
//    for (i in 1..10) {
//        satelliteMap.put(i, i)
//    }
//    satelliteMap.put(1, 12)
//    val byteArray = ByteArray(256)
//    val byteArrayOf = byteArrayOf()
//    for (i in 0..255) {
//        byteArray[i]=i.toByte()
//    }
//    println(HexUtil.toHexString(byteArrayOf))
//    val num = 128
//    val num2Byte: Byte = num.toByte()
//    println("mytest num $num num2Byte $num2Byte")
//    var id = 0x0001
//    var str = "{\"bid\":806355968}"
////    0x0001	单板ID烧录请求	bid+单板ID，例：{"bid":806355968}
//    id = 0x0001
//    str = "{\"bid\":806355968}"
//    pack(id,str)
//    val nmea = "\$BDGSV,1,1,02,209,64,354,40,214,05,318,40*69"
//    val sf = SentenceFactory.getInstance()
//
//    val gsa = sf.createParser(nmea) as GSVSentence
//    val satelliteCount = gsa.satelliteCount//当前卫星数量
//    val satelliteInfo = gsa.satelliteInfo//卫星信息
//    gsa.sentenceIndex
//    for (satelliteInfo in satelliteInfo) {
//        satelliteInfo.azimuth//方位角
//        satelliteInfo.elevation//海拔
//        satelliteInfo.noise//信噪比
//        satelliteInfo.id//编号
//
//    }
//    println(JSON.toJSON(gsa))
////    0x0002	单板ID烧录响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
//    id = 0x0002
//    println(unPack(id,"{\"ret\":0}".toByteArray()))
////    0x0003	整机ID烧录请求	id+整机ID，例：{"id":805307392}
//    id = 0x0003
//    str = "{\"id\":805307392}"
//    pack(id,str)
////    0x0004	整机ID烧录响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
//    id = 0x0004
//    println(unPack(id,"{\"ret\":0}".toByteArray()))
////    0x0005	WIFI开关请求	wifi+开关（0-关；1-开），例：{"wifi":0}
//    id = 0x0005
//    str = "{\"wifi\":0}"
//    pack(id,str)
////    0x0006	WIFI开关响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
//    id = 0x0006
//    println(unPack(id,"{\"ret\":0}".toByteArray()))
////    0x0007	LORA配置请求	chan+信道，power+功率（0-低功率；1-高功率），例：{"chan":9,"power":0}
////    0x0008	LORA配置响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
////    0x0009	KEY清空请求	空
////    0x0010	KEY清空响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
////    0x0011	LCD显示请求	lcd+显示值（0-白；1-黑），例：{“lcd":0}
////        0x0012	LCD显示响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}
////        0x0013	4G开关请求	4g1+开关（0-关；1-开）或4g2+开关，例：{"4g1":0}或{"4g2":0}
////        0x0014	4G开关响应	ret+返回值（0-成功；其他-失败），例：{"ret":0}


}

///**
// * 0x0001
// * 单板ID烧录请求
// * bid+单板ID，例：{"bid":806355968}
// */
//fun pack(id: Int, str: String): ByteArray {
//    val data = str.toByteArray()
//    val len = data.size
//    val byteBuffer = ByteBuffer.allocate(32 + len)
//    byteBuffer.putShort(id.toShort())
//    byteBuffer.putShort(len.toShort())
//    byteBuffer.position(32)
//    byteBuffer.put(data)
//    val array = byteBuffer.array()
//    println(HexUtil.toHexString(array, true))
//    return array
//}
//
////nio
//fun unPack(id: Int, data: ByteArray): String {
//    val byteBuffer = ByteBuffer.wrap(data)
////    val _id = byteBuffer.short
//    val len = byteBuffer.short
//    byteBuffer.position(32)
//    val dataArray = ByteArray(len.toInt())
//    byteBuffer.get(dataArray)
//
//    println("id=$id,len=$len")
//    return String(dataArray)
//}
