package org.sample.mqtt.service.impl;

import org.sample.mqtt.commons.util.BeanUtils;
import org.sample.mqtt.commons.util.ParserUtils;
import org.sample.mqtt.dao.AccumulatedDao;
import org.sample.mqtt.dao.CurrentDao;
import org.sample.mqtt.dao.EventDao;
import org.sample.mqtt.dao.SettingDao;
import org.sample.mqtt.entity.AccumulatedInfo;
import org.sample.mqtt.entity.CurrentInfo;
import org.sample.mqtt.entity.EventInfo;
import org.sample.mqtt.entity.SettingInfo;
import org.sample.mqtt.service.NoticeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by alan on 2017/2/22.
 */
@Service
public class NoticeMessageServiceImpl implements NoticeMessageService {

    @Autowired
    private AccumulatedDao accumulatedDao;

    @Autowired
    private CurrentDao currentDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private SettingDao settingDao;

    @Override
    public void saveAccumulated(String deviceId, String message) {
        AccumulatedInfo entity = ParserUtils.parse(deviceId, message, AccumulatedInfo.class);
        if (entity != null)
            accumulatedDao.save(entity);
    }

    @Override
    public void saveCurrent(String deviceId, String message) {
        CurrentInfo entity = ParserUtils.parse(deviceId, message, CurrentInfo.class);
        if (entity != null)
            currentDao.save(entity);
    }

    @Override
    public void saveEvent(String deviceId, String message) {
        EventInfo entity = ParserUtils.parse(deviceId, message, EventInfo.class);
        if (entity != null)
            eventDao.save(entity);
    }

    @Override
    public void saveSetting(String deviceId, String message) {
        SettingInfo entity = ParserUtils.parse(deviceId, message, SettingInfo.class);
        if (entity != null) {
            SettingInfo old = settingDao.findOne(entity.getDeviceId());
            if (old != null)
                BeanUtils.copyBeanNotNull(old, entity);
            settingDao.save(entity);
        }
    }

}