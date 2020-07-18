package org.sample.mqtt.model.toclient;

import io.protostuff.Tag;
import org.sample.mqtt.component.annotations.Topic;
import org.sample.mqtt.component.model.MqttRequest;

/**
 * 配置参数更新
 */
@Topic("iot_client/{deviceId}/settings")
public class SettingUpdate extends MqttRequest {

    @Tag(2)
    private int param1;
    @Tag(3)
    private String param2;

    public int getParam1() {
        return param1;
    }

    public void setParam1(int param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}