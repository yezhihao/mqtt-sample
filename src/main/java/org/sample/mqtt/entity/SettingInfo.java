package org.sample.mqtt.entity;

import org.sample.mqtt.commons.DateEntityListener;
import org.sample.mqtt.commons.annotation.CreatedAt;
import org.sample.mqtt.commons.annotation.LastModified;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alan on 2017/2/22.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "mc_setting")
public class SettingInfo {

    private Integer deviceId;
    private Integer ratePulse;
    private Integer rateMinute;
    private Date dateTime;
    private String rewardModel;
    private String otherSetting;
    private String errorStatus;
    private String terminalFirmwareVersion;
    private Date updateTime;
    private Date createTime;

    @Id
    @Column(name = "device_id")
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }


    @Column(name = "rate_pulse")
    public Integer getRatePulse() {
        return ratePulse;
    }

    public void setRatePulse(Integer ratePulse) {
        this.ratePulse = ratePulse;
    }


    @Column(name = "rate_minute")
    public Integer getRateMinute() {
        return rateMinute;
    }

    public void setRateMinute(Integer rateMinute) {
        this.rateMinute = rateMinute;
    }


    @Column(name = "date_time")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }


    @Column(name = "reward_model")
    public String getRewardModel() {
        return rewardModel;
    }

    public void setRewardModel(String rewardModel) {
        this.rewardModel = rewardModel;
    }


    @Column(name = "other_setting")
    public String getOtherSetting() {
        return otherSetting;
    }

    public void setOtherSetting(String otherSetting) {
        this.otherSetting = otherSetting;
    }


    @Column(name = "error_status")
    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }


    @Column(name = "terminal_firmware_version")
    public String getTerminalFirmwareVersion() {
        return terminalFirmwareVersion;
    }

    public void setTerminalFirmwareVersion(String terminalFirmwareVersion) {
        this.terminalFirmwareVersion = terminalFirmwareVersion;
    }


    @LastModified
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @CreatedAt
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}