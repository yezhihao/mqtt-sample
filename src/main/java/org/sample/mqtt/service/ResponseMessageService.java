package org.sample.mqtt.service;

import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.messaging.Message;

/**
 * Created by Alan on 2017/2/22.
 */
public interface ResponseMessageService {

    RendezvousChannel subscribeTopic(String topic);

    void unSubscribeTopic(String topic);

    void response(Message<?> message);

}