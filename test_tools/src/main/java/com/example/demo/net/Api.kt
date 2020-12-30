package com.example.demo.net

import com.alibaba.fastjson.JSON
import com.example.demo.model.Config1For601
import com.example.demo.DialogCenterMsgWarn
import com.example.demo.model.*
import com.example.demo.utils.LoggerUtil
import com.example.demo.utils.openNewStage
import com.example.demo.view.test.gnss.GnssOldView
import com.example.demo.view.test.gnss.GnssTestData
import com.example.demo.view.test.gnss.GnssTestView
import com.google.gson.Gson
import javafx.collections.ObservableList
import tornadofx.*


object Api {
    private const val DevicesHost = "http://192.168.1.252/"
    const val url = "http://120.132.12.76:8090/hd_product"
    val api = Rest()
    fun getConfig1(fun1: (statistic: Config1For601) -> Unit = {}) {
        runAsync {

            try {
                var rsp = api.get("${DevicesHost}cgi-bin/rpc_client?getconfig&1").text()
                val toJson = Gson().fromJson(rsp, Config1For601::class.java)

                println(toJson)
                fun1(toJson)
            } catch (e: Exception) {
                FX.eventbus.fire(DialogCenterMsgWarn(e.message!!))
            }
        }


    }

    /**
     * 查询校准结果 间隔1秒，每5秒查询一次校准结果，成功后提示成功，最多查询12次一直失败则提示校准失败
     */
    fun upgrade(url: String, fun1: (isSuccess: Boolean) -> Unit = {}) {
        runAsync {

            try {
                val formUpload =
                        HttpPostUploadUtil.formUpload("${DevicesHost}cgi-bin/upgrade", null, mutableMapOf("file" to url))
                println(formUpload)
                fun1(formUpload.contains("成功"))
            } catch (e: Exception) {
                e.printStackTrace()

            }

        }


    }

    /**
     * 重启软件 间隔1秒，重启软件
     */
    fun resystem(fun1: (rspBean: RspBean601) -> Unit = {}) {
        //间隔1秒，重启软件
        runAsync {

            try {
                Thread.sleep(500)
                val text = api.get("${DevicesHost}cgi-bin/rpc_client?resystem").text()
                println(text)
                val toJson = Gson().fromJson(text, RspBean601::class.java)

                //println(toJson)
                fun1(toJson)
            } catch (e: Exception) {

                e.printStackTrace()
            }

        }

    }


    /**
     * 查询ccid
     *
     */
    fun checkCCID(ccid: String, fun1: (rspBean: CheckCCID) -> Unit = {}): CheckCCID? {
        try {

            val result = api.get("$url/sim/findByPageCondition?queryParams=$ccid")
            return JSON.parseObject(result.text(), CheckCCID::class.java)


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 上报测试结果
     */
    fun uploadTestRest(deviceTestModel: DeviceTestModel): Boolean {
        try {
            LoggerUtil.LOGGER.debug(deviceTestModel.toJSON())
            LoggerUtil.LOGGER.debug(deviceTestModel.toString())
            var rsp = api.post("$url/machineTest/save", deviceTestModel).one().let {
                it.getInt("code") == 200 && it.getString("message") == "添加成功！"
            }
            println("uploadTestRest-->是否上传成功：$rsp")
            return rsp
        } catch (e: Exception) {
            LoggerUtil.LOGGER.error(e.message, e);
//            FX.eventbus.fire(DialogCenterMsgWarn(e.message!!))
            return false
        }
    }


    /**
    #---根据id查询
    GET/machineTest/queryById?id='表单id'
    ResponseExample{
    "code":"200/500",
    "object":{一条数据},
    "message":""
    }
    //测试类型	status	char	4	1.单板测试，2.整机测试，3为老化测试
     */
    fun queryHistoryLast(equipmentId:String,status:String): ObservableList<DeviceTestModel>? {

//
//        var status = ""
//        when (GnssTestData.testStatus) {
//            TestStatus.TEST_STATUS_PRO -> {//单板测试
//                status = "1"
//            }
//            TestStatus.TEST_STATUS_TOTAL -> {//整机测试
//                status = "2"
//            }
//            TestStatus.TEST_STATUS_OLD -> {//老化测试
//                status = "3"
//            }
//        }
        //result=1成功的数据
        try {
            var rsp =
                    api.get("$url/machineTest/findByPageCondition?equipmentId=$equipmentId&status=$status").one()
                            .let {

                                it.getJsonArray("rows")
                            }?.toModel<DeviceTestModel>()
            println("queryHistoryLast-->$rsp")

            return rsp
        } catch (e: Exception) {
            //LoggerUtil.LOGGER.error(e.message, e);
            println("queryHistoryLast-->null")
            //FX.eventbus.fire(DialogCenterMsgWarn(e.message!!))
            return null
        }
    }

}
