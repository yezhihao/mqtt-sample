package org.sample.mqtt.support;

import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.sample.mqtt.model.Location;
import org.springframework.integration.mapping.BytesMessageMapper;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alan on 2019/8/7
 */
public class MqttBytesMessageMapper implements BytesMessageMapper {
    private static Map<String, Class> mapper = new HashMap<>();

    static {
        mapper.put("mqtt/location", Location.class);
    }

    @Override
    public Message<?> toMessage(byte[] bytes, Map<String, Object> headers) {
        String topic = (String) headers.get(MqttHeaders.RECEIVED_TOPIC);
        Class clazz = null;

        Set<Map.Entry<String, Class>> entries = mapper.entrySet();
        for (Map.Entry<String, Class> entry : entries) {
            String key = entry.getKey();
            if (topic.startsWith(key)) {
                clazz = entry.getValue();
                break;
            }
        }
        Object payload;
        if (clazz != null) {
            Schema schema = RuntimeSchema.getSchema(clazz);
            payload = ProtostuffUtils.decode(schema, bytes);
        } else {
            //未找到映射则使用解析为字符串
            payload = new String(bytes);
        }

        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(payload).copyHeaders(headers);
        return messageBuilder.build();
    }

    @Override
    public byte[] fromMessage(Message<?> message) {
        Object payload = message.getPayload();
        Schema<?> schema = RuntimeSchema.getSchema(payload.getClass());

        byte[] bytes = ProtostuffUtils.encode(schema, payload);
        return bytes;
    }
}