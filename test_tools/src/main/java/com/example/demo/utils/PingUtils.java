package com.example.demo.utils;

import java.net.InetAddress;

/**
 * 作者 : lei
 * 时间 : 2020/12/16.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
public class PingUtils {

    public static boolean ping(String ipAddress) throws Exception {
        int timeOut = 3000;  //超时应该在3钞以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;

    }
}
