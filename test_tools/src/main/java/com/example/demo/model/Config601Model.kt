package com.example.demo.model


import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class Config601Model(paramstype:Int=7,hostType:Int=4) : JsonModel {


    val paramstypeProperty = SimpleIntegerProperty(paramstype)
    var paramstype by paramstypeProperty //

    /**
     * 功能类型：默认为0
    1-湖北版本
    2-农信通版本
    3-黑龙江版本
    4-播种版本
     */
    val hostTypeProperty = SimpleIntegerProperty(hostType)
    var hostType by hostTypeProperty //



    override fun toJSON(json: JsonBuilder) {
        super.toJSON(json)
        with(json) {
            add("hostType", hostType)
            add("paramstype", paramstype)

        }
    }

    override fun updateModel(json: JsonObject) {
        super.updateModel(json)
        with(json) {
            hostType = int("hostType")?:0
            paramstype = int("paramstype")?:0


        }
    }

    override fun toString(): String {
        return "Config601Model(paramstype=$paramstype, hostType=$hostType)"
    }


}