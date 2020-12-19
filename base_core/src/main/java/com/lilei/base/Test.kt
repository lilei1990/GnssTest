package com.lilei.base

/**
 * 作者 : lei
 * 时间 : 2020/12/12.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
class Test {
}

fun main() {
    B()
}

open class A {
    var a = 123
    constructor() : super(){
        println("constructora")
    }
    init {
        println("A_init")
    }
}
//72 6f 6f 74 0a 69 35 30 31 2e 31 32 33 34 0a
class B : A {
    var b = 456

    constructor() : super(){
        println("constructorb")
    }

    init {
        println("B_init")
    }
}