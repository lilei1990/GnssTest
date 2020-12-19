package com.example.demo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
    public static final Logger LOGGER = LogManager.getLogger();
    public static void d(String tag,String msg) {
        LOGGER.debug(tag+"-->"+ msg);
    }
    public static void info(String tag,Object obj) {
        LOGGER.info(tag+"-->"+obj);
    }

    public static void e(String tag,Object obj) {
        LOGGER.error(tag, obj);
    }

}
