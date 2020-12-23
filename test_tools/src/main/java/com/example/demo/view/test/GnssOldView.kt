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
data class GGA(var ip: String, var gga: String, var result: String)
class GnssOldView : View("老化测试") {
    val items = observableListOf<XYChart.Data<String, Number>>()
    var h = SqliteHelper("testHelper.db")
    val controller: GnssOldController by inject()



    var test_timeOut = 1


    init {
//        h.executeUpdate("drop table if exists GGA;");
//        //如果表存在就删除
////      h.executeUpdate("drop table if exists test;");
//        //创建表
//        h.executeUpdate("create table GGA(str varchar(200),ip varchar(20),time TIMESTAMP);")
        //周期性去check数据
        for (i in 1..24) {
            controller.   texasCities.add("${i}小时")
        }


    }



//每收到一个udp数据解析包ip地址用来标识设备
// 解析数据放到相应的数据库下面
//独立的线程定时做数据校验，如果卫星数量低于某个数量就测试不通过


    override val root = vbox {

        hbox {

            tableview(controller.pass) {
                prefWidth = 300.0
                readonlyColumn("ip", GGA::ip)
                readonlyColumn("卫星颗数", GGA::gga)
                readonlyColumn("结果", GGA::result).cellFormat {
                    text = it
                    itemStyle(it)
                }
            }
            tableview(controller.buff) {
                prefWidth = 300.0
                readonlyColumn("ip", GGA::ip)
                readonlyColumn("波动时间", GGA::gga)
                readonlyColumn("结果", GGA::result).cellFormat {
                    text = it
                    itemStyle(it)
                }
            }
            tableview(controller.fail) {
                prefWidth = 300.0
                readonlyColumn("ip", GGA::ip)
                readonlyColumn("id", GGA::gga)
                readonlyColumn("结果", GGA::result).cellFormat {
                    text = it
                    itemStyle(it)
                }
            }
        }



        hbox {
            text {
                prefWidth(50.0)
                textProperty().bind(controller. udpnum)
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
                text = "上传测试结果"
                disableProperty().bind(controller.isStart)
                action {
                    for (gga in controller.fail) {

                    }
                    controller.testUpload()
                }
            }
        }

    }

    private fun TableCell<GGA, String>.itemStyle(it: String) {
        style {
            if (it.equals("通过")) {
                backgroundColor += Color.GREEN
                textFill = Color.WHITE
            } else if (it.equals("不通过")) {
                backgroundColor += Color.RED
                textFill = Color.BLACK
            } else {
                backgroundColor += Color.YELLOW
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
