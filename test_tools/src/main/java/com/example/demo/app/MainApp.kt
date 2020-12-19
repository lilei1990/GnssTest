package com.example.demo.app

import com.example.demo.view.MainView
import javafx.scene.Scene
import javafx.stage.Stage
import tornadofx.App
import tornadofx.find
import tornadofx.launch

fun main() {
    try {
        launch<MainApp>()
    } catch (t: Throwable) {
        System.out.println("系统发生异常"+t.message)
    }

}

/**
 * app的入口
 */
class MainApp : App() {



    override fun start(stage: Stage) {
        super.start(stage)

        val jfxDecorator =
            MyJFXDecorator(
                stage = stage,
                view = find(MainView::class).root,
                myTitle = "程序入口"
            )
        val scene = Scene(jfxDecorator)
        //载入样式表`
        scene.stylesheets.add("/css/jfoenix-components.css")
        scene.stylesheets.add("/css/jfoenix-main-demo.css")
        //添加场景
        stage.scene = scene

        //置顶
        stage.isAlwaysOnTop = true
        //调出stage
        stage.show()
    }
}


//class MainApp: App(MainView::class, Styles::class)