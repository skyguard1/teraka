package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class BaseRule implements IRule{

    private List<ServiceConfig> servers;

    @Override
    public ServiceConfig getServer(List<ServiceConfig> servers) {
        this.servers = servers;
        if(CollectionUtils.isNotEmpty(servers)){
            return servers.get(0);
        }

        return null;
    }




}
