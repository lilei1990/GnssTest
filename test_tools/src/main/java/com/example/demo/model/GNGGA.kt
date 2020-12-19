package com.example.demo.model

/**
 * 作者 : lei
 * 时间 : 2020/12/15.
 * 邮箱 :416587959@qq.com
 * 描述 :val latitude = temps[2] //纬度

val longitude = temps[4] //经度

val fixStatus = temps[6] //GNSS状态

val svsCount = temps[7] //解算卫星数

val diffDelay = temps[13] //差分延迟
 */
data class GNGGA(
    var utc: Double = 0.0,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var svsCount: Int = 0,
    var fixStatus: Int = 0,
    var diffDelay: Double = 0.0
)
