package org.sample.mqtt.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sample.mqtt.component.model.MqttResponse;
import org.sample.mqtt.model.toclient.SettingUpdate;
import org.sample.mqtt.model.toserver.Location;
import org.sample.mqtt.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void sendNotice(@ApiParam(value = "设备ID") @RequestParam(defaultValue = "test123") String deviceId,
                           Location location) {
        messageService.sendNotice(deviceId, location);
    }

    @ApiOperation(value = "发送同步消息")
    @RequestMapping(path = "send")
    public MqttResponse sendMessage(@ApiParam(value = "设备ID") @RequestParam(defaultValue = "test123") String deviceId,
                                    SettingUpdate settingUpdate) {
        MqttResponse response = messageService.sendMessage(deviceId, settingUpdate);
        return response;
    }
}