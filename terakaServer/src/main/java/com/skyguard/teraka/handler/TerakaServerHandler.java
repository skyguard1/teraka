package com.skyguard.teraka.handler;

import com.google.common.collect.Lists;
import com.skyguard.teraka.config.ServerInfo;
import com.skyguard.teraka.config.ServiceConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TerakaServerHandler {

    public static List<ServerInfo> getServerInfo(Map<String,List<ServiceConfig>> map){
        List<ServerInfo> serverInfos = Lists.newArrayList();

        if(!map.isEmpty()){
            Set<String> topics = map.keySet();
            for(String topic:topics){
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.setTopic(topic);
                List<ServiceConfig> serviceConfigs = map.get(topic);
                serverInfo.setServiceConfigs(serviceConfigs);
                serverInfos.add(serverInfo);
            }

        }

       return serverInfos;
    }





}
