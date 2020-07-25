package org.sample.mqtt.model.toserver;

import io.protostuff.Tag;
import org.sample.mqtt.component.annotations.Topic;

/**
 * 位置信息上报
 * 通知类消息不需要继承 MqttResponse
 */
@Topic("iot_server/location/{deviceId}")
public class Location {

    @Tag(1)
    private String deviceId;
    @Tag(2)
    private long timestamp;
    @Tag(3)
    private double longitude;
    @Tag(4)
    private double latitude;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}