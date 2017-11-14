package org.sample.mqtt.service;

/**
 * Created by Alan on 2017/2/22.
 */
public interface NoticeMessageService {

    void saveAccumulated(String deviceId, String message);

    void saveCurrent(String deviceId, String message);

    void saveEvent(String deviceId, String message);

    void saveSetting(String deviceId, String message);

}