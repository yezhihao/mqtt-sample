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

    public void sendNotice(String topic, String payload) {
        if (payload == null)
            payload = "";
        mqttProducer.sendTo(topic, payload);
    }

    public String sendMessage(String deviceId, String action, String payload) {
        if (payload == null)
            payload = "";
        RendezvousChannel responseChannel = subscribeTopic(deviceId, action);
        if (responseChannel == null)
            return "REJ";

        String result = "NAK";
        try {
            mqttProducer.sendTo(deviceId + "/" + action, payload);
            Message<?> response = responseChannel.receive(20000);
            if (response != null)
                result = (String) response.getPayload();
        } finally {
            unSubscribeTopic(deviceId, action);
        }
        return result;
    }

    public RendezvousChannel subscribeTopic(String deviceId, String action) {
        String key = getKey(deviceId, action);
        RendezvousChannel result = null;
        if (!topicSubscribers.containsKey(key))
            topicSubscribers.put(key, result = new RendezvousChannel());
        return result;
    }

    public void unSubscribeTopic(String deviceId, String action) {
        topicSubscribers.remove(getKey(deviceId, action));
    }

    public void response(Message<?> message) throws MessagingException {
        String[] split = message.getHeaders().get(MqttHeaders.TOPIC, String.class).split("/");
        String key = getKey(split[2], split[1]);
        RendezvousChannel responseChannel = topicSubscribers.get(key);
        if (responseChannel != null)
            responseChannel.send(message);
    }

    private String getKey(String deviceId, String action) {
        return action + "/" + deviceId;
    }
}