package com.dailydatahub.dailydatacrawler.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JsonComponent {

    /**
     * convert json to Map
     */
    public static Map<String, Object> jsonToMap(JSONObject json) throws Exception {
        Map<String, Object> retMap = new HashMap<String, Object>();
        
        if(json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    //iteratable json converter to map
    /**
     * iteratable json converter to map
     * @param object
     * @return
     * @throws Exception
     */
    public static Map<String, Object> toMap(JSONObject json) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        @SuppressWarnings("rawtypes")
        Set keys = json.keySet();
        @SuppressWarnings("unchecked")
        Iterator<String> keysItr = keys.iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = json.get(key);
            
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }
            
            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * convert json to list which hava jsonArray
     * @param array
     * @return
     * @throws Exception
     */
    public static List<? extends Object> toList(JSONArray array) throws Exception {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    
}
