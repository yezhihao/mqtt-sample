package org.sample.mqtt.service;

import org.sample.mqtt.component.annotations.Topic;
import org.sample.mqtt.component.model.MqttRequest;
import org.sample.mqtt.component.model.MqttResponse;
import org.sample.mqtt.endpoint.MqttProducer;
import org.sample.mqtt.service.impl.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by Alan on 2019/8/7.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Mono<MqttResponse> Rejected = Mono.error(new RejectedExecutionException("客户端暂未响应，请勿重复发送"));

    private Map<String, MonoSink<MqttResponse>> topicSubscribers = new ConcurrentHashMap<>();

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
    public Mono<MqttResponse> request(String deviceId, MqttRequest payload) {
        Class<? extends MqttRequest> clazz = payload.getClass();
        Topic annotation = clazz.getAnnotation(Topic.class);
        String value = annotation.value();
        String topic = value.replace("{deviceId}", deviceId);

        long messageId = payload.getMessageId();
        if (messageId == 0L)
            payload.setMessageId(messageId = System.currentTimeMillis());

        String key = getKey(deviceId, messageId);
        Mono<MqttResponse> receive = subscribe(key);
        if (receive == null)
            return Rejected;

        return Mono.create(sink -> {
            mqttProducer.sendTo(topic, payload);
            sink.success();
        }).then(receive).doFinally(signal -> unSubscribe(key));
    }

    @Override
    public boolean response(Message<MqttResponse> message) {
        MqttResponse payload = message.getPayload();
        MonoSink<MqttResponse> sink = topicSubscribers.get(getKey(payload.getDeviceId(), payload.getMessageId()));
        if (sink != null) {
            sink.success(payload);
            return true;
        }
        return false;
    }

    private String getKey(String deviceId, long messageId) {
        return deviceId + "/" + messageId;
    }

    private Mono<MqttResponse> subscribe(String key) {
        if (!topicSubscribers.containsKey(key)) {
            return Mono.create(sink -> topicSubscribers.put(key, sink));
        }
        return null;
    }

    private void unSubscribe(String key) {
        topicSubscribers.remove(key);
    }
}