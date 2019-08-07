package org.sample.mqtt.controller;

import io.swagger.annotations.ApiOperation;
import org.sample.mqtt.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "mqtt", method = {RequestMethod.POST, RequestMethod.GET})
public class MqttController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "发送MQ.")
    @RequestMapping(path = "send")
    public String control(@RequestParam String deviceId,
                          @RequestParam String action,
                          @RequestParam(required = false) String message) {
        return messageService.send(deviceId, action, message);
    }
}