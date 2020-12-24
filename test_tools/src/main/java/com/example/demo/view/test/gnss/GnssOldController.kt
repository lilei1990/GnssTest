package com.example.demo.view.test.gnss

import com.example.demo.model.DeviceTestModel
import com.example.demo.net.Api
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.UDPBroadcast
import com.example.demo.view.test.bean.OldCase
import com.google.gson.JsonArray
import javafx.collections.FXCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
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

    val buff = observableListOf<OldCase>()
    val fail = observableListOf<OldCase>()
    val uploaderr = observableListOf<OldCase>()

    val texasCities = FXCollections.observableArrayList<String>()
    val isStart = booleanProperty(false)
    var isStop = false
    fun startTest(test_timeOut: Int) {
        //超时毫秒数
        val timeOut = test_timeOut * 60 * 60 * 1000
        isStop = false
//        val timeOut = 10000
        val f1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val startTime = System.currentTimeMillis()
        var endTime = System.currentTimeMillis()
        runAsync {
            isStart.value = true
            while ((endTime - startTime) <= timeOut && !isStop) {
                val ggamap_buff = GnssTestData.ggamap_buff
                val ggamap_fail = GnssTestData.ggamap_fail

                //清空列表数据
                buff.clear()
                fail.clear()
                ggamap_fail.forEach { (key, vlue) ->

                    fail.add(vlue)
                }
                ggamap_buff.forEach { (key, vlue) ->
                    buff.add(vlue)
                }

                //接收到的udp数
                udpnum.value = "已经接受的数据包:${UDPBroadcast.num}"
                sleep(1000)
                endTime = System.currentTimeMillis()
            }
            isStart.value = false
        }
    }

    fun upload() {
        runBlocking {
            channelFlow<OldCase> {
                for (oldCase in fail) {
                    send(oldCase)
                }
                for (oldCase in buff) {
                    send(oldCase)
                }
            }.map {
                it.apply {
                    if (!testUpload(this)) {//上传失败.添加到上传失败的容器
                        uploaderr.add(it)
                    }
                }
            }.flowOn(Dispatchers.IO).collect {
            }
        }
    }

    /**
     * 重新上传失败的数据
     */
    fun reUpload() {
        runBlocking {
            channelFlow<OldCase> {
                for (oldCase in uploaderr) {
                    send(oldCase)
                }
            }.map {
                it.apply {
                    if (testUpload(this)) {//上传失败.添加到上传失败的容器
                        uploaderr.remove(it)
                    }
                }
            }.flowOn(Dispatchers.IO).collect {

            }
        }
    }

    /**
     * 上传测试结果
     */
    fun testUpload(oldCase: OldCase): Boolean {
        val deviceTestModel = DeviceTestModel()
        deviceTestModel.testTime = TimeUtil.getSimpleDateTime()
        deviceTestModel.status = GnssTestData.testStatus
        deviceTestModel.equipmentId = "${oldCase.id}"

        var jsr = JsonArray()

        jsr.add("${oldCase.id},${oldCase.satelliteCount},${oldCase.testResult}")

        deviceTestModel.remark = jsr.toString()
        deviceTestModel.userId = GnssConfig.userId.value
        var rst = Api.uploadTestRest(deviceTestModel)
//        if (it) {
//            TestStatus.PASS
//        } else {
//            TestStatus.FAIL
//        }
        return rst
    }

    fun stop() {
        isStop = true
        isStart.value = false
        GnssTestData.reset()
    }
}
