package org.sample.mqtt.mqtt;

import org.sample.mqtt.config.UpstreamRouter;
import org.sample.mqtt.service.ResponseMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ResponseHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class.getSimpleName());

    @Autowired
    private ResponseMessageService messageService;

    @ServiceActivator(inputChannel = UpstreamRouter.ResponseChannel, poller = @Poller(fixedDelay = "300", maxMessagesPerPoll = "1"))
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC, String.class);
        String content = String.valueOf(message.getPayload());

        logger.info("Topic {} : {}", topic, content);
        messageService.response(message);
    }

}