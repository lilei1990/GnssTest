package com.example.demo.model

/**
 * 作者 : lei
 * 时间 : 2020/12/15.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
data class CheckCCID(
    val code: Int,
    val rows: List<Row>,
    val total: Int
)

data class Row(
    val ccid: String,
    val description: String,
    val id: String,
    val number: String,
    val opTime: String,
    val purchaseDate: String,
    val purchaseName: String,
    val remark: String
)