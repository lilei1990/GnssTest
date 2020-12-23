package com.example.demo.view.test

import com.example.demo.model.UDP_Msg

/**
 * 作者 : lei
 * 时间 : 2020/12/16.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
interface CallBacks {
    fun send(msg: UDP_Msg,address:String)
}