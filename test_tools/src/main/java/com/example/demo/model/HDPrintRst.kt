package com.example.demo.model

import com.example.demo.utils.PropertiesLocalUtil
import com.example.demo.utils.TimeUtil
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class HDPrintRst(

        deviceId: String,//主机ID

        serialId: String,//箱号
        title: String = "",
        value: String = "",
        packFlag: Int = 0,//1为大包装，0为小包装
        opTime: String = TimeUtil.getSimpleDateTime(),
        opPeople: String = PropertiesLocalUtil.read(EnumProperties.USER_ID),
        index: Int = 0
) : JsonModel {
//测试项	测试方法	环境准备 测试结果
//    2	CAN接口（1个）	接CAN口机具识别，看能否读到机具识别ID	引出CAN接口（1个）

    val indexProperty = SimpleIntegerProperty(index)
    var index by indexProperty

    val deviceIdProperty = SimpleStringProperty(deviceId)
    var deviceId by deviceIdProperty
    val titleProperty = SimpleStringProperty(title)
    var title by titleProperty

    /**
     * 需要处理的值
     */
    val valueProperty = SimpleStringProperty(value)
    var value by valueProperty


    val serialIdProperty = SimpleStringProperty(serialId)
    var serialId by serialIdProperty

    val opTimeProperty = SimpleStringProperty()
    var opTime by opTimeProperty //
    val descriptionProperty = SimpleStringProperty("")
    var description by descriptionProperty //

    val opPeopleProperty = SimpleStringProperty()
    var opPeople by opPeopleProperty //
    val packFlagProperty = SimpleIntegerProperty(packFlag)
    var packFlag by packFlagProperty //

    override fun toJSON(json: JsonBuilder) {
        super.toJSON(json)
        with(json) {
            add("serialId", serialId)
            add("content", "$deviceId")
            add("description", description)
            add("packFlag", packFlag)
            add("opPeople", PropertiesLocalUtil.read(EnumProperties.USER_ID))
            add("opTime", opTime)
        }
    }

    override fun updateModel(json: JsonObject) {
        super.updateModel(json)
        with(json) {
            serialId = string("serialId")
            deviceId = string("content")
            opTime = string("opTime")
            opPeople = string("opPeople")
            description = string("description")

            packFlag = int("packFlag")?:0
            opTime = string("opTime")
            //id = string("id")
            //description = string("description")

        }
    }

    override fun toString() = "TestCase {index=$index;deviceId=$deviceId;packFlag=$packFlag;serialId=$serialId;title=$title; value=$value}"
}
