package org.sample.mqtt.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

/**
 * Created by Alan on 2017/2/22.
 */
@Service
public class NoticeHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(NoticeHandler.class.getSimpleName());

    @ServiceActivator(inputChannel = UpstreamRouter.NoticeChannel, poller = @Poller(fixedDelay = "300", maxMessagesPerPoll = "1"))
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC, String.class);
        String content = String.valueOf(message.getPayload());

        logger.info(topic + " : " + content);
    }
}