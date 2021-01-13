package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者 : lei
 * 时间 : 2020/12/16.
 * 邮箱 :416587959@qq.com
 * 描述 :
 */
public class PingUtils {
    /**
     * isReachable方法在Windows系统平台上的实现(native c)并没有使用ICMP，而是全完使用连接echo端口7 的方法
     * @param ipAddress
     * @return
     */
    public static boolean ping(String ipAddress){
        try {
            int timeOut = 3000;  //超时应该在3钞以上
            boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
            // 当返回值是true时，说明host是可用的，false则不可。
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     *
     * @param ipAddress ip地址
     * @param pingTimes 次数
     * @param timeOut 超时时间 秒
     * @return
     */
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        // Linux命令如下
        // String pingCommand = "ping" -c " + pingTimes + " -w " + timeOut + ipAddress;
        try {
            if (LoggerUtil.LOGGER.isDebugEnabled()) {
                LoggerUtil.LOGGER.debug(pingCommand);
            }
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line;
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception e) {
            LoggerUtil.LOGGER.error(e);
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LoggerUtil.LOGGER.error(e);
            }
        }
    }
    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)",    Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }


}
