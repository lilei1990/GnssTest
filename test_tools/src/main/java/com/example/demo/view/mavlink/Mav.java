package com.example.demo.view.mavlink;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Parser;
import com.example.demo.utils.LoggerUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Mav {
    public static BlockingQueue<MAVLinkMessage> queue_msg_sys_status = new LinkedBlockingQueue<MAVLinkMessage>();
    private static Parser parser = new Parser(Parser.TYPE_SERIAL);

    public static void parser(byte[] data) {
        for (byte b : data) {
            MAVLinkPacket packet = parser.mavlink_parse_char(b & 0xff);
            if (packet != null) {
                MAVLinkMessage msg = packet.unpack();
                if (msg != null) {
                    try {
                        if (!queue_msg_sys_status.offer(msg, 500, TimeUnit.SECONDS)) {
                            LoggerUtil.LOGGER.debug("BlockingQueue-msg_sys_status-队列存储超时");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        LoggerUtil.LOGGER.debug("BlockingQueue-队列存储异常==" + e.getMessage());
                    }
                }
            }
        }
    }
}
