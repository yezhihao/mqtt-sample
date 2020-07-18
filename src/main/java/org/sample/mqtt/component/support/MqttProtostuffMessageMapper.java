package org.sample.mqtt.component.support;

import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.sample.mqtt.commons.ClassUtils;
import org.sample.mqtt.commons.ProtostuffUtils;
import org.sample.mqtt.component.annotations.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.mapping.BytesMessageMapper;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用字节传输报文(基于Protostuff)
 * Created by Alan on 2019/8/7
 */
public class MqttProtostuffMessageMapper implements BytesMessageMapper {

    private static final Logger logger = LoggerFactory.getLogger(BytesMessageMapper.class.getSimpleName());

    private static Map<String, Class> mapper = new HashMap<>();

    public MqttProtostuffMessageMapper(String modelPackages) {
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

        if (clazz != null) {
            Schema schema = RuntimeSchema.getSchema(clazz);
            try {
                payload = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes, payload, schema);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder("\n反序列化失败：");
                sb.append(clazz.getName());
                sb.append(topic);
                sb.append("\nstring ").append(new String(bytes));
                sb.append("\nbase64 ").append(Base64.getEncoder().encodeToString(bytes));
                logger.error(sb.toString());
                throw e;
            }
        } else {
            //未找到映射则使用解析为字符串
            payload = new String(bytes, StandardCharsets.UTF_8);
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