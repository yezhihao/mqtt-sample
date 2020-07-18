package org.sample.mqtt.component.model;

import io.protostuff.Tag;

public abstract class MqttRequest {

    /** 消息ID */
    @Tag(1)
    protected long messageId;

    public MqttRequest() {
    }

    public MqttRequest(long messageId) {
        this.messageId = messageId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}