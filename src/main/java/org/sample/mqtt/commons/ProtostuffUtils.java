package org.sample.mqtt.commons;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;

public class ProtostuffUtils {

    private static final ThreadLocal<LinkedBuffer> BUFFER_THREAD_LOCAL = ThreadLocal.withInitial(() -> LinkedBuffer.allocate(512));

    public static byte[] encode(Schema schema, Object obj) {
        LinkedBuffer buffer = BUFFER_THREAD_LOCAL.get();
        try {
            byte[] bytes = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T decode(Schema<T> schema, byte[] bytes) {
        T result = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, result, schema);
        return result;
    }
}