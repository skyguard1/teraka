package com.skyguard.teraka.filter;

import com.google.common.collect.Lists;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.registry.TerakaRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseFilter implements Filter{


    @Override
    public List<ServiceConfig> filterSerer(String topic){

        List<ServiceConfig> serviceConfigs = Lists.newArrayList();
        TerakaRegistry terakaRegistry = TerakaRegistry.getInstance();
        Map<String,List<ServiceConfig>> dataMap = terakaRegistry.getDataMap();
        Set<String> keys = dataMap.keySet();
        Pattern p=Pattern.compile(topic);
        for(String key:keys){
            Matcher m=p.matcher(key);
            if(m.matches()){
                List<ServiceConfig> serviceConfigList = dataMap.get(key);
                serviceConfigs.addAll(serviceConfigList);
            }
        }

        return serviceConfigs;
    }



}
