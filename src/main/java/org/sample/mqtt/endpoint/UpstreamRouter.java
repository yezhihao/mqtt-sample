package org.sample.mqtt.endpoint;

import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by Alan.ye on 18/5/23.
 */
@Component
public class UpstreamRouter {

    public static final String ResponseChannel = "responseChannel";
    public static final String NoticeChannel = "noticeChannel";

    @Router(inputChannel = "mqttInboundChannel")
    public String redirect(@Header(MqttHeaders.TOPIC) String topic, @Payload String payload) {
        if (topic.endsWith("ack") || payload.endsWith("nak"))
            return ResponseChannel;
        return NoticeChannel;
    }
}