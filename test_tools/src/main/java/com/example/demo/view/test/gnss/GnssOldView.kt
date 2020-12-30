package com.example.demo.view.test.gnss

import com.example.demo.ToastEvent
import com.example.demo.net.Api
import com.example.demo.utils.PingUtils
import com.example.demo.utils.showProgressStage
import com.example.demo.utils.showSnackbar
import com.example.demo.utils.sqlite.SqliteHelper
import com.example.demo.utils.view.DialogBuilder
import com.example.demo.view.test.bean.OldCase
import com.jfoenix.controls.JFXButton
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.chart.XYChart
import javafx.scene.control.TableCell
import javafx.scene.control.ToolBar
import javafx.scene.paint.Color
import kfoenix.jfxbutton
import kfoenix.jfxsnackbar
import kotlinx.coroutines.flow.*
import tornadofx.*
import java.lang.Thread.sleep

/**
 * 作者 : lei
 * 时间 : 2020/12/21.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
//data class GGA(var ip: String, var gga: String, var result: String)
class GnssOldView : View("老化测试") {
    val items = observableListOf<XYChart.Data<String, Number>>()
    var h = SqliteHelper("testHelper.db")
    val controller: GnssOldController by inject()


    var test_timeOut = 1


    init {
        runAsync {
            while (true) {
                sleep(1000)
            }
        }
        for (i in 1..24) {
            controller.texasCities.add("${i}小时")
        }

        controller.subscribe<ToastEvent> {
            Platform.runLater {
                showSnackbar(it.msg)
            }
        }
    }


//每收到一个udp数据解析包ip地址用来标识设备
// 解析数据放到相应的数据库下面
//独立的线程定时做数据校验，如果卫星数量低于某个数量就测试不通过


    override val root = vbox {
        paddingAll = 10.0
        spacing = 10.0
        ToolBar().add(RestProgressBar::class)
        hbox {

            vbox {
                alignment = Pos.CENTER
                label("测试正常的数据")
                tableview(controller.datalist) {
                    prefWidth = 400.0
                    readonlyColumn("ip", OldCase::ip)
                    readonlyColumn("卫星颗数", OldCase::satelliteCount)
                    readonlyColumn("id", OldCase::id)
                    readonlyColumn("波动时间", OldCase::time)
                    readonlyColumn("结果", OldCase::testResult).cellFormat {
                        text = it
                        itemStyle(it)
                    }
                }
            }
            vbox {
                alignment = Pos.CENTER
                label("上传失败的数据")
                tableview(controller.uploaderr) {
                    prefWidth = 400.0
                    readonlyColumn("ip", OldCase::ip)
                    readonlyColumn("卫星颗数", OldCase::satelliteCount)
                    readonlyColumn("id", OldCase::id)
                    readonlyColumn("波动时间", OldCase::time)
                    readonlyColumn("结果", OldCase::testResult).cellFormat {
                        text = it
                        itemStyle(it)
                    }
                }
            }
            vbox {
                alignment = Pos.TOP_CENTER
                label("日志")
                textarea(controller.taLog) {
                    prefWidth = 150.0
                    prefHeight=300.0
                }
            }
        }



        hbox {
            spacing = 10.0

            label("工号:   ${GnssConfig.userId.value}")
            text {
                prefWidth(50.0)
                textProperty().bind(controller.udpnum)
            }
            alignment = Pos.CENTER

            text("  设置测试时间 :")
            combobox(values = controller.texasCities) {
                disableProperty().bind(controller.isStart)
                selectionModel.selectFirst()
                selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
                    test_timeOut = controller.texasCities.indexOf(newValue) + 1
                    jfxsnackbar("设置测试时间为:$test_timeOut")
                }
            }
            jfxbutton {
                text = "开始测试"
                disableProperty().bind(controller.isStart)
                action {

                    controller.startTest(test_timeOut)
                }
            }
            jfxbutton {
                text = "停止测试"
                action {
                    controller.stop()
                }
            }
            jfxbutton {
                text = "上传测试结果"
                disableProperty().bind(controller.isStart)
                action {
                    val showProgressStage = showProgressStage("正在上传中")
                    if (controller.datalist.isEmpty()) {
                        showSnackbar("未检测到要提交的数据")
                        return@action
                    }
                    showProgressStage.show()
                    runAsync {
                        controller.upload()
                    } ui {
                        showProgressStage.close()
                    }
                }
            }
            jfxbutton {
                text = "再次提交上传失败的数据"
                disableProperty().bind(controller.isStart)
                action {
                    reUpload()
                }
            }

        }

    }

    private fun JFXButton.reUpload() {
        val showProgressStage = showProgressStage("正在上传中")
        if (controller.uploaderr.isEmpty()) {
            showSnackbar("未检测到要提交的数据")
            return
        }
        showProgressStage.show()
        runAsync {
            controller.reUpload()
        } ui {
            showProgressStage.close()
            if (controller.uploaderr.isEmpty()) {
                showSnackbar("上传成功")
            } else {
                DialogBuilder(this)
                        .setTitle("发现未上传的数据")
                        .setMessage("是否再次上传")
                        .setPositiveBtn("确定", {
                            reUpload()
                        }, "#00ff00")
                        .setNegativeBtn("取消") {

                        }
                        .create();
            }
        }
    }

    private fun TableCell<OldCase, String>.itemStyle(it: String) {
        style {
            if (it.equals("通过")) {
                backgroundColor += Color.GREEN
                textFill = Color.WHITE
            } else if (it.equals("不通过")) {
                backgroundColor += Color.RED
                textFill = Color.BLACK
            }
        }
    }

    /**
     * 初始化数据库
     */
    private fun initSql() {
        //如果数据表存在就删除
        h.executeUpdate("drop table if exists GGA;");
        //创建表
        h.executeUpdate("create table GGA(ip varchar(20),satelliteCount varchar(20),str varchar(200),time TIMESTAMP);")
    }


}
