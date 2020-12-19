package com.example.demo.view.test

import com.example.demo.utils.openNewStage
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class JobNumView : View() {
    val input = SimpleStringProperty()

    init {
        input.value = "00001"
    }


    override val root = form {
        fieldset {
            field("输入操作员") {
                textfield(input) {
                    Platform.runLater {
                        requestFocus()
                    }
                }
            }

            button("确认") {
                textProperty()
                action {
                    openNewStage("GNSS参考站", GnssTestView(input.value))
                    close()
                }
            }
            togglebutton {
                val stateText = selectedProperty().stringBinding {
                    return@stringBinding "ssss"
                }
                textProperty()
                textProperty().bind(stateText)
            }
        }
    }
}
