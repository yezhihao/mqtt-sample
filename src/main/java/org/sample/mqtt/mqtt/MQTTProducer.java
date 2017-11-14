package org.sample.mqtt.mqtt;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Created by alan on 2017/2/14.
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MQTTProducer {

    @Gateway(replyTimeout = 2, requestTimeout = 200)
    void sendTo(@Header(MqttHeaders.TOPIC) String topic, String data);

}