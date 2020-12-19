package com.example.demo.model


import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class DevicePackModel : JsonModel {

    /**
     * content	设备ID+“，”
     */
    val contentProperty = SimpleStringProperty()
    var content by contentProperty //设备 id

    val serialIdProperty = SimpleStringProperty()
    var serialId by serialIdProperty //

    /**
     * 操作员	user_id	varchar	32
     */
    val opPeopleProperty = SimpleStringProperty()
    var opPeople by opPeopleProperty //操作员

    /**
     * 测试时间	test_time	datetime	0
     */
    val opTimeProperty = SimpleStringProperty()
    var opTime by opTimeProperty //测试时间

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
            add("content", content)
            add("serialId", serialId)
            add("opPeople", opPeople)
            add("opTime", opTime)
            add("description", description)
            add("testTime", opTime)
            add("remark", remark)
        }
    }

    override fun updateModel(json: JsonObject) {
        super.updateModel(json)
        with(json) {
            content = string("content")
            opPeople = string("opPeople")
            opTime = string("opTime")
            remark = string("remark")
            serialId = string("serialId")
            id = string("id")
            description = string("description")

        }
    }

    override fun toString(): String {
        return "{content=$content, " +
                "opPeople=$opPeople, opTime=$opTime, serialId=$serialId, remark=$remark," +
                " description=$description, id=$id}"
    }


}