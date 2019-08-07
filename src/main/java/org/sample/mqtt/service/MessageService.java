package org.sample.mqtt.service;

import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.messaging.Message;

/**
 * Created by Alan on 2017/2/22.
 */
public interface MessageService {

    String send(String deviceId, String action, String payload);

    RendezvousChannel subscribeTopic(String deviceId, String action);

    void unSubscribeTopic(String deviceId, String action);

    void response(Message<?> message);

}