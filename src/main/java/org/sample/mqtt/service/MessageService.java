package org.sample.mqtt.service;

import org.sample.mqtt.endpoint.MqttProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alan on 2017/5/24.
 */
@Service
public class MessageService {

    private Map<String, RendezvousChannel> topicSubscribers = new ConcurrentHashMap<>();

    @Autowired
    private MqttProducer mqttProducer;

    public void sendNotice(String topic, Object payload) {
        mqttProducer.sendTo(topic, payload);
    }

    public void sendNotice(String deviceId, String action, Object payload) {
        mqttProducer.sendTo(deviceId + "/" + action, payload);
    }

    public Message sendMessage(String deviceId, String action, Object payload) {
        return sendMessage(deviceId, action, payload, 5000);
    }

    public Message sendMessage(String deviceId, String action, Object payload, long timeout) {
        if (payload == null)
            payload = "";

        RendezvousChannel responseChannel = subscribeTopic(deviceId, action);
        if (responseChannel == null)
            return null;

        Message response;
        try {
            mqttProducer.sendTo(deviceId + "/" + action, payload);
            response = responseChannel.receive(timeout);
        } finally {
            unSubscribeTopic(deviceId, action);
        }
        return response;
    }

    public RendezvousChannel subscribeTopic(String deviceId, String action) {
        String key = getKey(deviceId, action);
        RendezvousChannel result = null;
        if (!topicSubscribers.containsKey(key))
            topicSubscribers.put(key, result = new RendezvousChannel());
        return result;
    }

    public void unSubscribeTopic(String deviceId, String action) {
        String key = getKey(deviceId, action);
        topicSubscribers.remove(key);
    }

    public boolean response(Message message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC, String.class);
        RendezvousChannel responseChannel = topicSubscribers.get(topic);
        if (responseChannel != null)
            return responseChannel.send(message);
        return false;
    }

    private String getKey(String deviceId, String action) {
        return action + "/" + deviceId;
    }
}