package org.sample.mqtt.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sample.mqtt.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "mqtt", method = {RequestMethod.GET})
public class MqttController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "发送通知消息（无返回报文）")
    @RequestMapping(path = "send/notice")
    public void sendNotice(@ApiParam(value = "原始Topic", required = true) @RequestParam(defaultValue = "mqtt/location/asd123") String topic,
                           @ApiParam(value = "消息体") @RequestParam(defaultValue = "{code:500,message:\"error action!\"}") String payload) {
        messageService.sendNotice(topic, payload);
    }

    @ApiOperation(value = "发送同步消息（有返回报文,Topic等于 {deviceId}/{action}）")
    @RequestMapping(path = "send")
    public String sendMessage(@ApiParam(value = "目标ID", required = true) @RequestParam(defaultValue = "12345") String target,
                              @ApiParam(value = "操作", required = true) @RequestParam(defaultValue = "test") String action,
                              @ApiParam(value = "消息体") @RequestParam(required = false) String payload) {
        Message message = messageService.sendMessage(target, action, payload);

        return String.valueOf(message.getPayload());
    }
}