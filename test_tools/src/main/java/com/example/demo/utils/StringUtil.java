package com.example.demo.utils;


/**
 * Created by liaozhankun on 2017/11/15.
 */

public class StringUtil {

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * 计算字符串真实长度
     * @param value
     * @return
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 获取固定长度字符串
     * @param value
     * @param sizeSplit 偶数
     * @return
     */
    public static String genSplitStr(String value,int sizeSplit) {
        StringBuffer sbRst = new StringBuffer();
        int start = 0;
        int end = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                end += 2;
            } else {
                end += 1;
            }
            sbRst.append(temp);
            if (end == sizeSplit||end == sizeSplit-1) {

                end = 0;
                sbRst.append("\n");

            }

        }



        return sbRst.toString();
    }
}
