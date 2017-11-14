package org.sample.mqtt.entity;

import org.sample.mqtt.commons.DateEntityListener;
import org.sample.mqtt.commons.annotation.LastModified;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alan on 2017/2/22.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "mc_accumulated")
public class AccumulatedInfo {

    private Integer deviceId;
    private Integer pulseNum;
    private Integer workTime;
    private Integer amount;
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


    @Column(name = "pulse_num")
    public Integer getPulseNum() {
        return pulseNum;
    }

    public void setPulseNum(Integer pulseNum) {
        this.pulseNum = pulseNum;
    }


    @Column(name = "work_time")
    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }


    @Column(name = "amount")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @LastModified
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Transient
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}