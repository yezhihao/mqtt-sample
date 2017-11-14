package org.sample.mqtt.controller;

import org.sample.mqtt.service.ResponseMessageService;
import org.sample.mqtt.mqtt.MQTTProducer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "mqtt")
public class MqttController {

    @Autowired
    private MQTTProducer mqttProducer;

    @Autowired
    private ResponseMessageService messageService;

    @ApiOperation(value = "发送MQ.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "响应码")})
    @RequestMapping(path = "send", method = RequestMethod.POST)
    public String control(@RequestParam String topic, @RequestParam String message) {

        RendezvousChannel responseChannel = messageService.subscribeTopic(topic);
        if (responseChannel == null)
            return "REJ";

        String payload = "NAK";
        try {
            mqttProducer.sendTo(topic, message);
            Message<?> response = responseChannel.receive(5000);
            if (response != null)
                payload = (String) response.getPayload();
        } finally {
            messageService.unSubscribeTopic(topic);
        }
        return payload;
    }

}