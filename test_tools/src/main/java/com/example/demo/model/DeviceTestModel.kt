package com.example.demo.model


import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class DeviceTestModel : JsonModel {

    /**
     * 设备ID	equipment_id	varchar	32
     */
    val equipmentIdProperty = SimpleStringProperty()
    var equipmentId by equipmentIdProperty //设备 id
    /**
     * 测试类型	status	char	4	1.单板测试，2.整机测试，3为老化测试
     */
    val statusProperty = stringProperty()
    var status by statusProperty //测试类型

    /**
     * 测试结果	result	bit	1	1.通过，0.不通过
     */
    val resultProperty = booleanProperty()
    var result by resultProperty //测试结果


    /**
     * 操作员	user_id	varchar	32
     */
    val userIdProperty = SimpleStringProperty()
    var userId by userIdProperty //操作员

    /**
     * 测试时间	test_time	datetime	0
     */
    val testTimeProperty = SimpleStringProperty()
    var testTime by testTimeProperty //测试时间

    /**
     * 备注	remark	varchar	256
     */
    val remarkProperty = SimpleStringProperty()
    var remark by remarkProperty //备注

    /**
     * 描述	description	varchar	256
     */
    val descriptionProperty = SimpleStringProperty()
    var description by descriptionProperty //

    /**
     * 表单id	id	varchar	32
     */
    val idProperty = SimpleStringProperty()
    var id by idProperty //
    

    override fun toJSON(json: JsonBuilder) {
        super.toJSON(json)
        with(json) {
            add("equipmentId", equipmentId)
            add("status", status)
            add("result", result)
            add("description", description)
            add("user_id", userId)
            add("testTime", testTime)
            add("remark", remark)
        }
    }

    override fun updateModel(json: JsonObject) {
        super.updateModel(json)
        with(json) {
            equipmentId = string("equipmentId")
            status = string("status")
            result = boolean("result")?:false
            userId = string("userId")
            testTime = string("testTime")
            remark = string("remark")
            id = string("id")
            description = string("description")

        }
    }

    override fun toString(): String {
        return "{equipmentId=$equipmentId, status=$status, result=$result, " +
                "userId=$userId, testTime=$testTime, remark=$remark," +
                " description=$description, id=$id}"
    }


}