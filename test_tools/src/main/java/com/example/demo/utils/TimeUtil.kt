package com.example.demo.utils

import java.text.SimpleDateFormat

object TimeUtil {
    val dayStartFormat = SimpleDateFormat("yyyy-MM-dd 00:00:00")
    val dayEndFormat = SimpleDateFormat("yyyy-MM-dd 23:59:59")
    val simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd_hh-mm-ss")
    val simpleDateFormatCN = SimpleDateFormat("yyyy年MM月dd日")

    val simpleDateFormatYMD = SimpleDateFormat("yyyyMMdd")
    fun getDayStartTime(timeMillis: Long = System.currentTimeMillis()): String {
        return dayStartFormat.format(timeMillis)
    }

    fun getDayEndTime(timeMillis: Long = System.currentTimeMillis()): String {
        return dayEndFormat.format(timeMillis)
    }
    fun getYMD(): String {
        return simpleDateFormatCN.format(System.currentTimeMillis())
    }

    fun getSimpleDateTime(timeMillis: Long = System.currentTimeMillis()): String {
        return simpleDateFormat1.format(timeMillis)
    }
    fun getSimpleDayTime(timeMillis: Long = System.currentTimeMillis()): String {
        return simpleDateFormat2.format(timeMillis)
    }
    fun getYYYYMMDD(): String {
        return simpleDateFormatYMD.format(System.currentTimeMillis())
    }

}