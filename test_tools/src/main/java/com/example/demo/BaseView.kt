package com.example.demo

import com.example.demo.view.test.CenterController
import tornadofx.Controller
import tornadofx.View
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * 作者 : lei
 * 时间 : 2020/12/11.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
abstract class BaseView: View() {
    fun <T : Controller> getcontrol(kClass: KClass<T>): T {
        return kClass.createInstance()
    }

}