package com.example.demo.view.test.gnss

import com.example.demo.ToastEvent
import com.example.demo.model.DeviceTestModel
import com.example.demo.net.Api
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.UDPBroadcast
import com.example.demo.view.test.bean.OldCase
import com.google.gson.JsonArray
import javafx.application.Platform
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
    val datalist = observableListOf<OldCase>()
    val uploaderr = observableListOf<OldCase>()

    val texasCities = FXCollections.observableArrayList<String>()
    val isStart = booleanProperty(false)
    var isStop = false
    val taLog = stringProperty("123232")
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
                datalist.clear()

                ggamap_fail.forEach { (key, vlue) ->
                    //如果没有check过数据,就检查数据
                    if (!vlue.isCheck) {
                        vlue.checkId = checkId(vlue)
                    }
                    datalist.add(vlue)
                }
                ggamap_buff.forEach { (key, vlue) ->
                    //如果没有check过数据,就检查数据
                    if (!vlue.isCheck) {
                        checkId(vlue)
                    }
                    datalist.add(vlue)
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
                for (oldCase in datalist) {
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
     * 检查数据
     */
    private fun checkId(oldCase: OldCase): Boolean {
        oldCase.isCheck = true
        if (oldCase.equipmentId.isNullOrEmpty() || oldCase.bid.isNullOrEmpty()) {
            putLogInfo("${oldCase.ip}-未获取单板id-${oldCase.equipmentId}-整机id-${oldCase.bid}")
            return false
        }
        //查询单板测试
        val checkBidlist = Api.queryHistoryLast(oldCase.bid, "1")
        if (checkBidlist == null) {
            oldCase.isCheck = false
            putLogInfo("${oldCase.ip}-未查询到单板测试数据null")
            return false
        }
        if (checkBidlist.size == 0) {
            putLogInfo("${oldCase.ip}-未查询到单板测试数据${GnssTestData}")
            return false
        }
        //查询到数据如果上次测试结果为通过就提示,否则不通过
        if (checkBidlist.size >= 1 && !checkBidlist[0].result) {
            putLogInfo("${oldCase.ip}-最近一次单板测试不通过,拒绝测试!")
            return false
        }
        //查询整机测试
        val checkidlist = Api.queryHistoryLast(oldCase.equipmentId, "2")
        if (checkidlist == null) {
            oldCase.isCheck = false
            putLogInfo("${oldCase.ip}-未查询到整机测试数据!")
            return false
        }
        //查询到数据如果上次测试结果为通过就提示,否则不通过
        if (checkidlist.size >= 1 && !checkidlist[0].result) {
            putLogInfo("${oldCase.ip}-最近一次整机测试不通过,拒绝测试!")
            return false
        }

        return true
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
        deviceTestModel.equipmentId = oldCase.equipmentId

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

    fun putLogInfo(str: String) {
        Platform.runLater {
            taLog.value = "${TimeUtil.getSimpleDateTime()}-$str\n${taLog.value}"
        }
    }
}
