package org.sample.mqtt.endpoint;

import org.sample.mqtt.commons.JsonUtils;
import org.sample.mqtt.component.model.Connected;
import org.sample.mqtt.component.model.Disconnected;
import org.sample.mqtt.component.model.MqttRequest;
import org.sample.mqtt.component.model.MqttResponse;
import org.sample.mqtt.model.toclient.SettingUpdate;
import org.sample.mqtt.model.toserver.CommonResponse;
import org.sample.mqtt.service.impl.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

/**
 * Created by Alan on 2020/7/18.
 */
@Service
public class MqttHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MqttHandler.class.getSimpleName());

    @Autowired
    private MessageService messageService;

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get(MqttHeaders.TOPIC, String.class);
        Object payload = message.getPayload();

        if (topic.startsWith("$")) {
            //TODO emq支持设备上线离线通知
            logger.info((String) payload);
            if (topic.endsWith("disconnected")) {
                JsonUtils.toObj((String) payload, Disconnected.class);
            } else if (topic.endsWith("connected")) {
                JsonUtils.toObj((String) payload, Connected.class);
            }
        } else {
            try {
                //需要响应的同步类消息
                if (payload instanceof MqttResponse) {
                    messageService.response((Message<MqttResponse>) message);
                } else {
                    //TODO 服务端业务处理
                    logger.debug("\n{}: {}", topic, JsonUtils.toJson(payload));
                }

                //TODO 模拟客户端回复消息
                if (topic.startsWith("iot_client")) {
                    String deviceId = "test123";
                    MqttRequest request = JsonUtils.toObj((String) payload, SettingUpdate.class);
                    CommonResponse commonResponse = new CommonResponse(request.getMessageId(), deviceId, (byte) 0);
                    messageService.notify(deviceId, commonResponse);
                }
            } catch (Exception e) {
                logger.error("\n系统出错: " + JsonUtils.toJson(payload), e);
            }
        }
    }
}