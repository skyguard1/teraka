package com.skyguard.teraka.http;

import com.google.common.collect.Maps;
import com.skyguard.teraka.config.ServiceConfig;

import java.util.Map;

public class HttpStatus {

    private static Map<String,Integer> dataMap = Maps.newConcurrentMap();

    public static Integer getStatus(ServiceConfig serviceConfig){

        String name = serviceConfig.getIp()+":"+serviceConfig.getPort();

        if(!dataMap.containsKey(name)){
            dataMap.put(name,0);
        }

        Integer num = dataMap.get(name);

        return num;
    }

    public static void increase(ServiceConfig serviceConfig){

        String name = serviceConfig.getIp()+":"+serviceConfig.getPort();

        if(!dataMap.containsKey(name)){
            dataMap.put(name,0);
        }

        Integer num = dataMap.get(name);
        num++;
        dataMap.put(name,num);

    }


}
