package org.sample.mqtt.commons.util;

import com.google.common.collect.ImmutableMap;
import org.sample.mqtt.entity.AccumulatedInfo;
import org.sample.mqtt.entity.CurrentInfo;
import org.sample.mqtt.entity.EventInfo;
import org.sample.mqtt.entity.SettingInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 2017/2/22.
 */
public class ParserUtils {

    private static final Map<Class, Map<String, String>> MapperMap;

    static {
        //累计值
        Map accumulatedInfo = MapUtils.newMap(
                "deviceId", "deviceId",//设备ID
                "A_P", "pulseNum",//目前已收到的脉冲数量
                "A_M", "amount",//目前已收到的钞票数量
                "A_B", "workTime");//目前已工作的时间(分钟数)

        //当前值
        Map currentInfo = MapUtils.newMap(
                "deviceId", "deviceId",//设备ID
                "C_P", "pulseNum",//指当次服务累计脉冲数量
                "C_S", "countDown");//指当前服务剩余秒数

        //系统参数
        Map settingInfo = MapUtils.newMap(
                "deviceId", "deviceId",//设备ID
                "R_P", "ratePulse",//费率脉衝(5@R_P)
                "R_M", "rateMinute",//费率分钟(5@R_M)
                "D_T", "dateTime",//时间日期(170218165021@D_T)
                "O_B", "rewardModel",//奖励模式(M=1&S=1&C=1&E=1&B=100@O_B)
                "O_C", "otherSetting",//其他设定(A=1&B=2&C=3@O_C)
                "ERR", "errorStatus",
                "FWV", "terminalFirmwareVersion");//错误状态 ("ERROR STRING1"&"ERROR STRING2"&"ERROR STRING3"@ERR)

        //事件
        Map eventInfo = MapUtils.newMap(
                "deviceId", "deviceId",//设备ID
                "", "event");//

        MapperMap = ImmutableMap.of(
                AccumulatedInfo.class, accumulatedInfo,
                CurrentInfo.class, currentInfo,
                SettingInfo.class, settingInfo,
                EventInfo.class, eventInfo
        );
    }

    public static <T> T parse(String deviceId, String message, Class<T> targetClass) {
        Map<String, String> propertyMapper = MapperMap.get(targetClass);

        Map<String, Object> entityMap = new HashMap();
        entityMap.put(propertyMapper.get("deviceId"), deviceId);

        String[] lines = message.split(",");
        for (String line : lines) {
            String[] entry = line.split("@");
            if (entry.length > 1) {
                String key = entry[1];
                String value = entry[0];
                entityMap.put(propertyMapper.get(key), value);
            } else {
                entityMap.put(propertyMapper.get(""), entry[0]);
            }
        }

        try {
            T entity = targetClass.newInstance();
            return BeanUtils.copyBean(entityMap, entity);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}