package com.example.demo.app

import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val pass by cssclass()
        val labelprint by cssclass()
        val fail by cssclass()
        val testcenter by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        labelprint  {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#4CAF50", .5)
        }
        testcenter{
            textAlignment=TextAlignment.CENTER
        }
        pass{
            padding = box(3.px)

            fontWeight = FontWeight.BOLD
            backgroundColor += c("#4CAF50", .5)
            and(selected){
                backgroundColor += c("#0096C9", .5)
            }
        }
        fail{
            padding = box(3.px)
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#FF5722", .5)
            and(selected){
                backgroundColor += c("#0096C9", .5)
            }
        }
    }
}