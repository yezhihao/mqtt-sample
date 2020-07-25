package org.sample.mqtt.component.support;

import org.sample.mqtt.commons.ClassUtils;
import org.sample.mqtt.commons.JsonUtils;
import org.sample.mqtt.component.annotations.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mapping.BytesMessageMapper;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用Json传输报文(基于Jackson)
 * Created by Alan on 2020/5/4
 */
public class MqttJsonMessageMapper implements BytesMessageMapper {

    private static final Logger logger = LoggerFactory.getLogger(BytesMessageMapper.class.getSimpleName());

    private static Map<String, Class> mapper = new HashMap<>();

    public MqttJsonMessageMapper(String modelPackages) {
        String[] packages = modelPackages.split(",");
        for (String aPackage : packages) {
            List<Class<?>> classList = ClassUtils.getClassList(aPackage, Topic.class);
            for (Class<?> clazz : classList) {
                addClass(clazz);
            }
            System.out.println("add mapper:" + classList.size());
        }
    }

    public static void addClass(Class<?> clazz) {
        Topic annotation = clazz.getAnnotation(Topic.class);
        String value = annotation.value();
        mapper.put(value.substring(0, value.lastIndexOf("/")), clazz);
    }

    @Override
    public Message<?> toMessage(byte[] bytes, Map<String, Object> headers) {
        String topic = (String) headers.get(MqttHeaders.TOPIC);
        String action = topic.substring(0, topic.lastIndexOf("/"));

        Class clazz = mapper.get(action);
        Object payload;

        String str = new String(bytes, StandardCharsets.UTF_8);
        if (clazz != null) {
            try {
                payload = JsonUtils.toObj(str, clazz);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder("\n反序列化失败：");
                sb.append(clazz.getName());
                sb.append(topic);
                sb.append("\nstring ").append(str);
                logger.error(sb.toString());
                throw e;
            }
        } else {
            payload = str;
        }
        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(payload).copyHeaders(headers);
        return messageBuilder.build();
    }

    @Override
    public byte[] fromMessage(Message<?> message) {
        Object payload = message.getPayload();
        String str = JsonUtils.toJson(payload);

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
}