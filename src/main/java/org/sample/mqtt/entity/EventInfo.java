package org.sample.mqtt.entity;

import org.sample.mqtt.commons.DateEntityListener;
import org.sample.mqtt.commons.annotation.CreatedAt;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alan on 2017/2/22.
 */
@Entity
@EntityListeners(DateEntityListener.class)
@Table(name = "mc_event")
public class EventInfo {

    private Integer id;
    private Integer deviceId;
    private String event;
    private Date createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Column(name = "device_id")
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }


    @Column(name = "event")
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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