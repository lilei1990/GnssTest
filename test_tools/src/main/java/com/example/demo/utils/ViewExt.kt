package com.example.demo.utils

import com.example.demo.app.MyJFXDecorator
import com.example.demo.view.ProgressStage
import com.jfoenix.controls.JFXSnackbar
import com.jfoenix.controls.JFXSnackbarLayout
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.scene.paint.Paint
import javafx.stage.Stage
import tornadofx.UIComponent
import tornadofx.View
import tornadofx.c

fun View.openNewStage(title: String, view: View): Stage {
    val secondStage = Stage()
    secondStage.title = title
    view.root.stylesheets.addAll("/css/jfoenix-components.css", "/css/jfoenix-main-demo.css", "/css/button.css")
    val jfxDecorator =
        MyJFXDecorator(
            stage = secondStage,
            view = view.root,
            myTitle = title
        )
    val secondScene = Scene(jfxDecorator)
    secondStage.scene = secondScene
    secondStage.show()
    return secondStage
}

/**
 * 显示一个showSnackbar
 */
fun View.shwoInternalWindow(
    view: UIComponent,
    icon: Node? = null,
    modal: Boolean = true,
    owner: Node = root,
    escapeClosesWindow: Boolean = false,
    closeButton: Boolean = false,
    movable: Boolean = true,
    overlayPaint: Paint = c("#000", 0.4)
) {
    view.root.stylesheets.addAll("/css/jfoenix-components.css", "/css/jfoenix-main-demo.css", "/css/button.css")
    openInternalWindow(
        view,
        icon,
        modal,
        owner,
        escapeClosesWindow,
        closeButton,
        movable,
        overlayPaint
    )
}

/**
 * 显示一个showSnackbar
 */
fun UIComponent.showSnackbar(msg: String) {
    JFXSnackbar(root as Pane?).fireEvent(JFXSnackbar.SnackbarEvent(JFXSnackbarLayout(msg)))
}

fun View.showProgressStage(msg: String): ProgressStage {
    return ProgressStage.of(this.currentStage, msg)
}
