package com.example.demo.view.test

import com.example.demo.model.DeviceTestModel
import com.example.demo.model.EnumProperties
import com.example.demo.model.TestStatus
import com.example.demo.net.Api
import com.example.demo.utils.PropertiesLocalUtil
import com.example.demo.utils.TimeUtil
import com.example.demo.utils.sqlite.SqliteHelper
import com.google.gson.JsonArray
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.chart.XYChart
import javafx.scene.control.TableCell
import javafx.scene.paint.Color
import kfoenix.jfxbutton
import kfoenix.jfxsnackbar
import tornadofx.*
import java.lang.Thread.sleep
import java.text.SimpleDateFormat

/**
 * 作者 : lei
 * 时间 : 2020/12/21.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */

class GnssOldController : Controller() {
    //udp包数量
    val udpnum = stringProperty("")
    val pass = observableListOf<GGA>()
    val buff = observableListOf<GGA>()
    val fail = observableListOf<GGA>()

    val texasCities = FXCollections.observableArrayList<String>()
    val isStart = booleanProperty(false)
    fun startTest(test_timeOut: Int) {
        //超时毫秒数
        val timeOut = test_timeOut * 60 * 60 * 1000
//        val timeOut = 10000
        val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val startTime = System.currentTimeMillis()
        var endTime = System.currentTimeMillis()
        runAsync {
            isStart.value = true
            while ((endTime - startTime) <= timeOut) {
                val ggamap_pass = GnssTestData.ggamap_pass
                val ggamap_buff = GnssTestData.ggamap_buff
                val ggamap_fail = GnssTestData.ggamap_fail

                //清空列表数据
                pass.clear()
                buff.clear()
                fail.clear()
                ggamap_fail.forEach { (key, vlue) ->
                    fail.add(GGA(key, "${vlue}", "不通过"))
                }
                ggamap_buff.forEach { (key, vlue) ->
                    buff.add(GGA(key, "${f1.format(vlue)}", "波动"))
                }
                ggamap_pass.forEach { (key, vlue) ->
                    pass.add(GGA(key, "${vlue.satelliteCount}", "通过"))
                }
                //接收到的udp数
                udpnum.value = "已经接受的数据包:${UDPBroadcast.num}"
                sleep(1000)
                endTime = System.currentTimeMillis()
            }
            isStart.value = false
        }
    }

    /**
     * 上传测试结果
     */
    fun testUpload() {
        val deviceTestModel = DeviceTestModel()
        deviceTestModel.testTime = TimeUtil.getSimpleDateTime()
        deviceTestModel.status = GnssTestData.testStatus
        if (GnssTestData.testStatus == TestStatus.TEST_STATUS_PRO) {
            deviceTestModel.equipmentId = GnssTestData.bid.value
        } else {
            deviceTestModel.equipmentId = GnssTestData.id.value
        }

        var jsr = JsonArray()
        //默认通过
        deviceTestModel.result = true
        for (case in GnssTestData.hd100Case) {
            jsr.add("${case.id},${case.testInfo}")
            if (!case.result) {//如果有一项不通过就整体不通过
                deviceTestModel.result = false
            }
        }


        deviceTestModel.remark = jsr.toString()
        deviceTestModel.userId = PropertiesLocalUtil.read(EnumProperties.USER_ID)
        var rst = Api.uploadTestRest(deviceTestModel).let {
            if (it) {
                TestStatus.PASS
            } else {
                TestStatus.FAIL
            }
        }

    }
}
