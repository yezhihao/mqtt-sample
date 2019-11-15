package org.sample.mqtt.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by Alan on 2017/5/23.
 */
@Component
public class UpstreamRouter {

    private static final Logger logger = LoggerFactory.getLogger(UpstreamRouter.class.getSimpleName());

    public static final String ResponseChannel = "responseChannel";
    public static final String NoticeChannel = "noticeChannel";

    @Router(inputChannel = "mqttInboundChannel")
    public String redirect(@Header(MqttHeaders.RECEIVED_TOPIC) String topic, @Payload String payload) {
        if (topic.endsWith("ack") || topic.endsWith("nak"))
            return ResponseChannel;
        return NoticeChannel;
    }
}