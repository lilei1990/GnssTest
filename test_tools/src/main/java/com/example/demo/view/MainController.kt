package com.example.demo.view

import tornadofx.Controller
import tornadofx.FXEvent

class MainController : Controller() {
    class MainEvent(val customers: String) : FXEvent()

    init {
//        subscribe<MainEvent> { event ->
//
////            text = "sssssssssssssss"
//        }
    }

    fun action() {
        fire(MainEvent("ssss"))
    }
    fun writeToDb(inputValue: String) {
        println("Writing $inputValue to database!")
    }

    fun loadText(): String {
        println("Writing $this ")
       return "sssssssssss"
    }
}