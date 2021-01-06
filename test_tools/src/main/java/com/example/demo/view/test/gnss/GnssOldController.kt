package com.example.demo.view.test.gnss

import com.example.demo.model.DeviceTestModel
import com.example.demo.net.Api
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.UDPBroadcast
import com.example.demo.view.test.bean.OldCase
import com.google.gson.JsonArray
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import tornadofx.*
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*

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
    val taLog = stringProperty("")
    val tabTitleBuff = stringProperty("测试正常的数据")

    //持续时间
    val continueTime = stringProperty()


    fun startTest(test_timeOut: Int) {
        //开始测试之前初始化数据
        //缓冲数据区域,用于临时存放不通过的,如果持续不通过就会被放入fail
        GnssTestData.ggamap_buff.clear()
        //清空日志
        taLog.value = ""
        //超时毫秒数
        val timeOut = test_timeOut * 60 * 60 * 1000
        isStop = false
        val startTime = System.currentTimeMillis()
        var endTime = System.currentTimeMillis()
        isStart.value = true
        val simpleDateFormat1 = SimpleDateFormat("HH:mm:ss")
        simpleDateFormat1.timeZone = TimeZone.getTimeZone("etc/UTC")
        runAsync {
            while ((endTime - startTime) <= timeOut && !isStop) {
                val ggamap_buff = GnssTestData.ggamap_buff

                //清空列表数据
                datalist.clear()
                runBlocking {
                    channelFlow<OldCase> {

                        runBlocking {
                            ggamap_buff.forEach { (key, vlue) ->
                                send(vlue)
                            }
                        }
                    }.map {
                        it.apply {
                            if (this.equipmentId.isNullOrEmpty() || this.bid.isNullOrEmpty()) {
                                putLogInfo("设备id为空")
                                return@apply
                            }
                            //如果没有check过数据,就检查数据
                            if (!it.isCheck) {
                                it.checkId = checkId(it)
                            }
                        }

                    }.collect {
                        datalist.add(it)
                    }
                }

                Platform.runLater {
                    if (taLog.value.length > 1000) {//清除日志
                        taLog.value = ""
                    }
                    continueTime.value = "${simpleDateFormat1.format(endTime - startTime)}"
                    tabTitleBuff.value = "在线数(${ggamap_buff.size})"
                    //接收到的udp数
                    udpnum.value = "数据包:${UDPBroadcast.num}"
                }

                sleep(2000)
                endTime = System.currentTimeMillis()
            }
            isStart.value = false
        } ui {
            var alert = Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("测试完成");
            alert.setHeaderText("是否提交数据");
            alert.setContentText("确认会自动提交,取消手动提交?");
            var result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                upload()
            } else {
                // ... user chose CANCEL or closed the dialog

            }
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
        deviceTestModel.result = oldCase.status == 2
        var jsr = JsonArray()

        jsr.add("${oldCase.testInfo}")

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
    }

    fun putLogInfo(str: String) {
        Platform.runLater {
            taLog.value = "${TimeUtil.getSimpleDateTime()}-$str\n${taLog.value}"
        }
    }
}
