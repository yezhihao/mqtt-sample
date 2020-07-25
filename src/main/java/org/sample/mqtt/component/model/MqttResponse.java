package org.sample.mqtt.component.model;

import io.protostuff.Tag;

public abstract class MqttResponse {

    /** 应答消息ID */
    @Tag(1)
    protected long messageId;

    /** 设备ID */
    @Tag(2)
    protected String deviceId;

    public MqttResponse() {
    }

    public MqttResponse(long messageId, String deviceId) {
        this.messageId = messageId;
        this.deviceId = deviceId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}