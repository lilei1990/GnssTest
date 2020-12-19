package com.example.demo.app

import com.example.demo.controller.GlobalDecoratorController
import com.example.demo.model.SVG_logo
import com.example.demo.view.MainView
import com.jfoenix.controls.JFXDecorator
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.stage.Stage
import tornadofx.SVGIcon

fun MyJFXDecorator(stage: Stage,
                   view: Node,
                   myTitle: String = "标题",
                   myFullScreen: Boolean = false): JFXDecorator {
    return JFXDecorator(stage, view, false, true, true).apply dec@{
        this.setOnCloseButtonAction {
            //拦截退出事件
            GlobalDecoratorController(stage).exitModal(myTitle).show()
        }
        //设置Logo
        setGraphic(SVGIcon(color = Color.WHITE, svgShape = SVG_logo).apply {
            this.rotate = -180.0
        })

        //设置标题
        title = myTitle

    }

}
