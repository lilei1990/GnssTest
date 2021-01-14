package com.example.demo.view.test.gnss

import com.example.demo.model.TestStatus
import com.example.demo.utils.openNewStage
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class JobNumView : View() {
    val input = SimpleStringProperty()

    init {
        input.value = GnssConfig.userId.value
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
                    //设置用户id
                    GnssConfig.userId.value= input.value
                    when (GnssTestData.testStatus) {
                        TestStatus.TEST_STATUS_PRO -> {//单板测试
                            openNewStage("参考站单板测试", GnssTestView())
                        }
                        TestStatus.TEST_STATUS_TOTAL -> {//整机测试
                            openNewStage("参考站整机测试", GnssTestView())
                        }
                        TestStatus.TEST_STATUS_OLD -> {//老化测试
                            openNewStage("参考站老化测试", GnssOldView())
                        }
                        TestStatus.TEST_STATUS_PACKAGE -> {//打包测试
                            openNewStage("参考站打包测试", GnssTestView())
                        }
                    }

                    close()
                }
            }

        }
    }
}
