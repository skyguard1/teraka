package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;

import java.util.List;

public interface IRule {


    public ServiceConfig getServer(List<ServiceConfig> servers);


}
