package com.example.demo.view.test.bean

import com.example.demo.view.test.gnss.GnssConfig

/**
 * 作者 : lei
 * 时间 : 2020/12/09.
 * 邮箱 :416587959@qq.com
 * 描述 :{"mode"：模式（0-单板；1-整机；2-射频；3-老化；4-打包）,
"hw":"硬件版本号",
"sw":"软件版本号",
"bsp":"镜像版本号",
"bid":单板ID,
"id":整机ID,
"wifi":wifi开关（0-关；1-开），
"key":"按键记录值",
"usb":usb测试结果（0-测试中；1-成功；2-失败）,
"chan":信道,
"power":功率,
"4g1":"imsi,ping通次数",
"4g2":"imsi,ping通次数"
}
 */
open class Case(
    //id
    var id: Int=-1,
    //测试项
    var typeName: String = "",
    //测试明细
    var testInfo: String = "",
    //测试结果
    var testResult: String = "不通过",

    //测试默认超时时间
    val timeOut: Long = GnssConfig.defaut_timeOut.value.toLong()
){
    var result: Boolean = false
        set(value) {
            field = value
            if (value) {
                testResult = "通过"
            } else {
                testResult = "不通过"
            }
        }
    fun putTestInfo(str:String) {
        testInfo+="$str,"
    }
}
