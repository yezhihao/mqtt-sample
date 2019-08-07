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
 * Created by ICQ on 16/5/24.
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
        String topic = getKey(deviceId, action);
        RendezvousChannel result = null;
        if (!topicSubscribers.containsKey(topic))
            topicSubscribers.put(topic, result = new RendezvousChannel());
        return result;
    }

    public void unSubscribeTopic(String deviceId, String action) {
        String topic = getKey(deviceId, action);
        topicSubscribers.remove(topic);
    }

    public void response(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC, String.class);
        String[] split = topic.split("/");
        topic = getKey(split[2], split[1]);
        RendezvousChannel responseChannel = topicSubscribers.get(topic);
        if (responseChannel != null)
            responseChannel.send(message);
    }

    private String getKey(String deviceId, String action) {
        return action + "/" + deviceId;
    }
}