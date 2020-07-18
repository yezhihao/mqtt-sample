package org.sample.mqtt.service;

import org.sample.mqtt.component.annotations.Topic;
import org.sample.mqtt.component.model.MqttRequest;
import org.sample.mqtt.component.model.MqttResponse;
import org.sample.mqtt.endpoint.MqttProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alan on 2019/8/7.
 */
@Service
public class MessageService {

    private Map<String, RendezvousChannel> topicSubscribers = new ConcurrentHashMap<>();

    @Autowired
    private MqttProducer mqttProducer;

    public void sendNotice(String deviceId, Object payload) {
        Topic annotation = payload.getClass().getAnnotation(Topic.class);
        String value = annotation.value();
        String topic = value.replace("{deviceId}", deviceId);
        mqttProducer.sendTo(topic, payload);
    }

    public MqttResponse sendMessage(String deviceId, MqttRequest payload) {
        return sendMessage(deviceId, payload, 20000);
    }


    public MqttResponse sendMessage(String deviceId, MqttRequest payload, long timeout) {
        Class<? extends MqttRequest> clazz = payload.getClass();
        Topic annotation = clazz.getAnnotation(Topic.class);
        String value = annotation.value();
        String topic = value.replace("{deviceId}", deviceId);

        long messageId = payload.getMessageId();
        if (messageId == 0L)
            payload.setMessageId(messageId = System.currentTimeMillis());

        String key = getKey(deviceId, messageId);
        RendezvousChannel responseChannel = subscribeTopic(key);
        if (responseChannel == null)
            return null;

        try {
            mqttProducer.sendTo(topic, payload);
            Message<MqttResponse> response = (Message<MqttResponse>) responseChannel.receive(timeout);
            if (response != null)
                return response.getPayload();
        } finally {
            unSubscribeTopic(key);
        }
        return null;
    }

    private RendezvousChannel subscribeTopic(String key) {
        RendezvousChannel result = null;
        if (!topicSubscribers.containsKey(key))
            topicSubscribers.put(key, result = new RendezvousChannel());
        return result;
    }

    private void unSubscribeTopic(String key) {
        topicSubscribers.remove(key);
    }

    public boolean response(Message<MqttResponse> message) throws MessagingException {
        MqttResponse payload = message.getPayload();
        RendezvousChannel responseChannel = topicSubscribers.get(getKey(payload.getDeviceId(), payload.getMessageId()));
        if (responseChannel != null)
            return responseChannel.send(message);
        return false;
    }

    private String getKey(String deviceId, long messageId) {
        return deviceId + "/" + messageId;
    }
}