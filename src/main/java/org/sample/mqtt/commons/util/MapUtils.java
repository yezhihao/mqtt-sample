package org.sample.mqtt.commons.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 2017/2/22.
 */
public class MapUtils {

    public static Map<String, String> newMap(String... objs) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < objs.length; i += 2) {
            String key = String.valueOf(objs[i]);
            String value = objs[i + 1];
            if (value == null)
                value = "";
            result.put(key, value);
        }
        return result;
    }
}