package com.example.demo.view.test.gnss

import com.alibaba.fastjson.JSON
import com.example.demo.ToastEvent
import com.example.demo.model.DeviceTestModel
import com.example.demo.model.UDP_Msg
import com.example.demo.model.UDP_Msg0101
import com.example.demo.net.Api
import com.example.demo.utils.TimeUtil
import com.example.demo.view.test.CallBacks
import com.example.demo.view.test.UDPBroadcast
import com.example.demo.view.test.bean.OldCase
import com.google.gson.JsonArray
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import net.sf.marineapi.nmea.parser.SentenceFactory
import net.sf.marineapi.nmea.sentence.GGASentence
import net.sf.marineapi.nmea.sentence.GSVSentence
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
    val taLog = stringProperty("")
    val tabTitleBuff = stringProperty("测试正常的数据")

    fun startTest(test_timeOut: Int) {
        //开始测试之前初始化数据
        GnssTestData.reset()
        //超时毫秒数
        val timeOut = test_timeOut * 60 * 60 * 1000
        isStop = false
        val startTime = System.currentTimeMillis()
        var endTime = System.currentTimeMillis()
        isStart.value = true
        runAsync {
            while ((endTime - startTime) <= timeOut && !isStop) {
                val ggamap_buff = GnssTestData.ggamap_buff
                val ggamap_fail = GnssTestData.ggamap_fail

                //清空列表数据
                datalist.clear()
                runBlocking {
                    channelFlow<OldCase> {
                        runBlocking {
                            ggamap_fail.forEach { (key, vlue) ->
                                send(vlue)
                            }
                        }
                        runBlocking {
                            ggamap_buff.forEach { (key, vlue) ->
                                send(vlue)
                            }
                        }
                    }.map {
                        it.apply {
                            if (this.equipmentId.isNullOrEmpty() || this.bid.isNullOrEmpty()) {
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
                    tabTitleBuff.value = "通过(${ggamap_buff.size})不通过(${ggamap_fail.size})"
                    //接收到的udp数
                    udpnum.value = "已经接受的数据包:${UDPBroadcast.num}"
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

            }}
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

            var jsr = JsonArray()

            jsr.add("${oldCase.id},${oldCase.satelliteCount}  ,${oldCase.testResult}")

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
