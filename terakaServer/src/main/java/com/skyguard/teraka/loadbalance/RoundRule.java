package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class RoundRule implements IRule{

    private static int index = 0;


    @Override
    public ServiceConfig getServer(List<ServiceConfig> servers) {

        if(CollectionUtils.isEmpty(servers)){
            return null;
        }

        if(servers.size()==1){
            return servers.get(0);
        }

        int length = servers.size();
        ServiceConfig serviceConfig = servers.get(index);
        index++;
        if(index==length){
            index = 0;
        }

        return serviceConfig;
    }
}
