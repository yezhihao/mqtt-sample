package org.sample.mqtt.model.toserver;

import io.protostuff.Tag;
import org.sample.mqtt.component.annotations.Topic;
import org.sample.mqtt.component.model.MqttResponse;

/**
 * 通用回复
 */
@Topic("iot_server/reply/{deviceId}")
public class CommonResponse extends MqttResponse {

    /**
     * 0.成功
     * 1.失败
     * 2.消息错误
     * 3.不支持
     */
    @Tag(3)
    private byte result;

    public CommonResponse() {
    }

    public CommonResponse(long messageId, String deviceId, byte result) {
        super(messageId, deviceId);
        this.result = result;
    }

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }
}