package com.skyguard.teraka.config;

import java.util.List;

public class ServerInfo {

    private String topic;
    private List<ServiceConfig> serviceConfigs;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<ServiceConfig> getServiceConfigs() {
        return serviceConfigs;
    }

    public void setServiceConfigs(List<ServiceConfig> serviceConfigs) {
        this.serviceConfigs = serviceConfigs;
    }
}
