package org.sample.mqtt.mqtt;

import org.sample.mqtt.commons.constant.Topics;
import org.sample.mqtt.config.UpstreamRouter;
import org.sample.mqtt.service.NoticeMessageService;
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
public class NoticeHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(NoticeHandler.class.getSimpleName());

    @Autowired
    private NoticeMessageService messageService;

    @ServiceActivator(inputChannel = UpstreamRouter.NoticeChannel, poller = @Poller(fixedDelay = "300", maxMessagesPerPoll = "1"))
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC, String.class);
        String content = String.valueOf(message.getPayload());
        logger.info("Topic {} : {}", topic, content);

        String[] topicLine = topic.split("/");
        String id = topicLine[2];
        String target = topicLine[3];

        if (Topics.accumulated.equals(target)) {
            messageService.saveAccumulated(id, content);

        } else if (Topics.current.equals(target)) {
            messageService.saveCurrent(id, content);

        } else if (Topics.event.equals(target)) {
            messageService.saveEvent(id, content);

        } else if (Topics.setting.equals(target)) {
            messageService.saveSetting(id, content);

        } else {
            logger.warn("Unknown Topic {} : {}", topic, content);

        }
    }

}