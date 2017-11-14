package org.sample.mqtt.dao;

import org.sample.mqtt.entity.CurrentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alan.ye on 2017/2/22.
 */
public interface CurrentDao extends JpaRepository<CurrentInfo, Integer> {

}