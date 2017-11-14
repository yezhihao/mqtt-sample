package org.sample.mqtt.service.impl;

import org.sample.mqtt.service.ResponseMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ICQ on 16/5/24.
 */
@Service
public class ResponseMessageServiceImpl implements ResponseMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ResponseMessageServiceImpl.class.getSimpleName());

    private Map<String, MessageChannel> topicSubscribers;

    private Lock subscribersLock;

    public ResponseMessageServiceImpl() {
        topicSubscribers = new HashMap<>();
        subscribersLock = new ReentrantLock();
    }

    @Override
    public RendezvousChannel subscribeTopic(String topic) {
        topic = getKey(topic);
        RendezvousChannel result = null;
        subscribersLock.lock();
        try {
            if (!topicSubscribers.containsKey(topic)) {
                result = new RendezvousChannel();
                topicSubscribers.put(topic, result);
            }
        } finally {
            subscribersLock.unlock();
        }
        return result;
    }

    @Override
    public void unSubscribeTopic(String topic) {
        topic = getKey(topic);
        subscribersLock.lock();
        try {
            topicSubscribers.remove(topic);
        } finally {
            subscribersLock.unlock();
        }
    }

    @Override
    public void response(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC, String.class);
        topic = getKey(topic);
        MessageChannel responseChannel;
        subscribersLock.lock();
        try {
            responseChannel = topicSubscribers.get(topic);
        } finally {
            subscribersLock.unlock();
        }
        if (responseChannel != null) {
            responseChannel.send(message);
        }
    }

    private String getKey(String topic) {
        String[] topics = topic.split("/");
        return topics[topics.length - 2] + topics[topics.length - 1];
    }

}