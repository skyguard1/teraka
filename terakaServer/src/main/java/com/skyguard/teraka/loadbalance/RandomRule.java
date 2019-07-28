package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Random;

public class RandomRule implements IRule{


    private final Random random = new Random();

    @Override
    public ServiceConfig getServer(List<ServiceConfig> servers) {

        if(CollectionUtils.isEmpty(servers)){
            return null;
        }

        if(servers.size()==1){
            return servers.get(0);
        }

        int length = servers.size();

        ServiceConfig serviceConfig = servers.get(random.nextInt(length));

        return serviceConfig;
    }
}
