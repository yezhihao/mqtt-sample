package org.sample.mqtt.service;

import org.sample.mqtt.component.annotations.Topic;
import org.sample.mqtt.component.model.MqttRequest;
import org.sample.mqtt.component.model.MqttResponse;
import org.sample.mqtt.endpoint.MqttProducer;
import org.sample.mqtt.service.impl.MessageService;
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
public class MessageServiceImpl implements MessageService {

    private Map<String, RendezvousChannel> topicSubscribers = new ConcurrentHashMap<>();

    @Autowired
    private MqttProducer mqttProducer;

    @Override
    public void notify(String deviceId, Object payload) {
        Topic annotation = payload.getClass().getAnnotation(Topic.class);
        String value = annotation.value();
        String topic = value.replace("{deviceId}", deviceId);
        mqttProducer.sendTo(topic, payload);
    }

    @Override
    public MqttResponse request(String deviceId, MqttRequest payload) {
        return request(deviceId, payload, 10000);
    }

    @Override
    public MqttResponse request(String deviceId, MqttRequest payload, long timeout) {
        Class<? extends MqttRequest> clazz = payload.getClass();
        Topic annotation = clazz.getAnnotation(Topic.class);
        String value = annotation.value();
        String topic = value.replace("{deviceId}", deviceId);

        long messageId = payload.getMessageId();
        if (messageId == 0L)
            payload.setMessageId(messageId = System.currentTimeMillis());

        String key = getKey(deviceId, messageId);
        RendezvousChannel responseChannel = subscribe(key);
        if (responseChannel == null)
            return null;

        try {
            mqttProducer.sendTo(topic, payload);
            Message<MqttResponse> response = (Message<MqttResponse>) responseChannel.receive(timeout);
            if (response != null)
                return response.getPayload();
        } finally {
            unSubscribe(key);
        }
        return null;
    }

    @Override
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

    private RendezvousChannel subscribe(String key) {
        RendezvousChannel result = null;
        if (!topicSubscribers.containsKey(key))
            topicSubscribers.put(key, result = new RendezvousChannel());
        return result;
    }

    private void unSubscribe(String key) {
        topicSubscribers.remove(key);
    }
}