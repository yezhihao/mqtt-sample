package org.sample.mqtt.config;

import org.springframework.integration.annotation.Router;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by ICQ on 16/5/23.
 */
@Component
public class UpstreamRouter {

    public static final String ResponseChannel = "responseChannel";
    public static final String NoticeChannel = "noticeChannel";

    @Router(inputChannel = "mqttInboundChannel")
    public String redirect(@Payload String payload) {
        if (payload.contains("ACK") || payload.contains("NAK")) {
            return ResponseChannel;
        } else {
            return NoticeChannel;
        }
    }
}