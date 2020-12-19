package com.example.demo.utils.cmd;

import java.util.concurrent.Callable;

/**
 * @author zhangwei
 * date 2018/10/22
 * time 14:47
 */
public class CheckTask implements Callable<Boolean> {
    public static void main(String[] args) {
        new CheckTask("hdwork","work.1234").call();
    }

    private String ssid;
    private String password;


    public CheckTask(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    public Boolean call() {
        WlanExecute execute = new WlanExecute();
        boolean checked = execute.check(ssid, password);
        return checked;
    }
}
