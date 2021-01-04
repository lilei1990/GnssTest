package com.example.demo.model

class TestStatus {
    companion object {
        val PASS = "正常"
        val FAIL = "异常"
        val TEST_PRO = "单板测试"
        val TEST_TOTAL = "整机测试"

        /**
         * 1.单板测试
         */
        val TEST_STATUS_PRO = "1"
        /**
         * 2.整机测试
         */
        val TEST_STATUS_TOTAL = "2"
        /**
         * 3为老化测试
         */
        val TEST_STATUS_OLD = "3"
        /**
         * 4为打包测试
         */
        val TEST_STATUS_PACKAGE = "4"
        val TEST_BEGIN = "开始测试"
        val TEST_STOP = "停止测试"
    }
}