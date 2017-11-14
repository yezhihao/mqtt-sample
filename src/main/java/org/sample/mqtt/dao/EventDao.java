package org.sample.mqtt.dao;

import org.sample.mqtt.entity.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alan.ye on 2017/2/22.
 */
public interface EventDao extends JpaRepository<EventInfo, Integer> {

}