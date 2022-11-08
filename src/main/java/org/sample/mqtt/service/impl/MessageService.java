package org.sample.mqtt.service.impl;

import org.sample.mqtt.component.model.MqttRequest;
import org.sample.mqtt.component.model.MqttResponse;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;

/**
 * Created by Alan on 2020/7/18.
 */
public interface MessageService {

    void notify(String deviceId, Object payload);

    Mono<MqttResponse> request(String deviceId, MqttRequest payload);

    boolean response(Message<MqttResponse> message);
}