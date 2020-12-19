package com.example.demo.view.test

import com.example.demo.model.GNGGA
import com.example.demo.model.GPGSV
import com.example.demo.model.UDP_Msg
import java.nio.ByteBuffer

/**
 * 作者 : lei
 * 时间 : 2020/12/11.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
object Pack {

    /**
     * 0x0001
     * 单板ID烧录请求
     * bid+单板ID，例：{"bid":806355968}
     */
    fun pack(id: Int, str: String): ByteArray {
        val data = str.toByteArray()
        val len = data.size
        val byteBuffer = ByteBuffer.allocate(32 + len)
        byteBuffer.putShort(id.toShort())
        byteBuffer.putShort(len.toShort())
        byteBuffer.position(32)
        byteBuffer.put(data)
        val array = byteBuffer.array()
//        println(HexUtil.toHexString(array, true))
        return array
    }

    //nio
    fun unPack(data: ByteArray): UDP_Msg {
        val byteBuffer = ByteBuffer.wrap(data)
        val id = byteBuffer.short
        val len = byteBuffer.short
        byteBuffer.position(32)
        val dataArray = ByteArray(data.size - 36)
        byteBuffer.get(dataArray)
//        println("id=$id,len=$len ${String(dataArray)} ===\n")
        return UDP_Msg(id, len, String(dataArray))
    }

    /**
     * 解析GGA
     * $GPGGA,085355.20,3113.7902268,N,12116.3646563,E,2,25,0.6,6.626,M,11.144,M,2.7,0137*4B
     * $GPGGA,<1>,<2>,<3>,<4>,<5>,<6>,<7>,<8>,<9>,M,<11>,M,<13>,<14>*hh
     * <1> UTC 时间，hhmmss(时分秒)格式
     * <2> 纬度 ddmm.mmmm(度分)格式(前面的 0 也将被传输)
     * <3> 纬度半球 N(北半球)或 S(南半球)
     * <4> 经度 dddmm.mmmm(度分)格式(前面的 0 也将被传输)
     * <5> 经度半球 E(东经)或 W(西经)
     * <6> GNSS 状态标识:0 = invalid(未定位)
     * 1 = GPS fix (SPS)(单点定位)
     * 2 = DGPS fix(伪距差分)
     * 3 = PPS fix
     * 4 = Real Time Kinematic(RTK 固定)
     * 5 = Float RTK(RTK 浮动)
     * 6 = estimated (dead reckoning) (2.3 feature) (正在估算) 7 = Manual input mode(固定坐标输出)
     * 8 = Simulation mode
     * <7> 正在使用解算位置的卫星数(前面的 0 也将被传输)
     * <8> HDOP 水平精度因子(0.5~99.9)
     * <9> 海拔高度(-9999.9~99999.9)
     * <11> 地球椭球面相对大地水准面的高度
     * <13> 差分时间(从最近一次接收到差分信号开始的秒数，如果不是差分定位默 认值 99)
     * <14> 差分站 ID 号 0000~1023(前面的 0 也将被传输，如果不是差分定位默认 值 AAAA)
     *标准格式：

     * @param gga
     */
    fun parseGNGGA(str: String): GNGGA {
        val gga = GNGGA()
        val temps: Array<String> = str.split(",").toTypedArray()
        if (temps.size < 15) return gga
        val utc = temps[1]
        val latitude = temps[2] //纬度

        val longitude = temps[4] //经度

        val fixStatus = temps[6] //GNSS状态

        val svsCount = temps[7] //解算卫星数

        val diffDelay = temps[13] //差分延迟

        if (!utc.isNullOrEmpty()) try {
            gga.utc = utc.toDouble()
        } catch (e: Exception) {
        }

        var lat = 0.0
        var lng = 0.0
        if (!latitude.isNullOrEmpty()) {
            try {
                gga.lat = latitude.toDouble()
            } catch (e: Exception) {
            }
        }
        if (!longitude.isNullOrEmpty()) {
            try {
                gga.lng = longitude.toDouble()
            } catch (e: Exception) {
            }
        }

        if (lat != 0.0 && lng != 0.0) {
            var deg = lat as Int / 100
            var mis = lat - deg * 100.0
            gga.latitude = deg + mis / 60.0
            deg = lng as Int / 100
            mis = lng - deg * 100.0
            gga.longitude = deg + mis / 60.0
        }

        if (!fixStatus.isNullOrEmpty()) {
            try {
                gga.fixStatus = fixStatus.toDouble() as Int
            } catch (ignored: Exception) {
            }
        }
        if (!svsCount.isNullOrEmpty()) {
            try {
                gga.svsCount = svsCount.toDouble() as Int
            } catch (ignored: Exception) {
            }
        }
        if (!diffDelay.isNullOrEmpty()) {
            try {
                gga.diffDelay = diffDelay.toDouble()
            } catch (ignored: Exception) {
            }
        }
        return gga
    }

    /**
     *
    $GPGSV，(1)，(2)，(3)，(4)，(5)，(6)，(7)，…(4),(5)，(6)，(7)*hh(CR)(LF)

    各部分含义为：

    (1)总的GSV语句电文数；2;

    (2)当前GSV语句号:1;

    (3)可视卫星总数:08;

    (4)PRN码（伪随机噪声码）　也可以认为是卫星编号

    (5)仰角(00～90度):33度;

    (6)方位角(000～359度):240度;
    $GBGSV,5,3,17,11,54,086,42,12,17,040,,16,59,188,42,23,53,332,,3*75
    (7)信噪比(00～99dB):45dB(后面依次为第10，16，17号卫星的信息); 　　*总和校验域；　 　　hh 总和校验数:78; 　　(CR)(LF)回车，换行。

    注：每条语句最多包括四颗卫星的信息，每颗卫星的信息有四个数据项，即：

    (4)－卫星号，(5)－仰角，(6)－方位角，(7)－信噪比。
     */
    fun parseGPGSV(str: String): GPGSV {
        val gsv = GPGSV()
        val temps: Array<String> = str.split(",").toTypedArray()
        if (temps.size < 15) return gsv
        val dB = temps[7] //信噪比
        if (dB.isNotEmpty()) {
            gsv.dB = temps[7].toInt()
        }
        return gsv
    }
}