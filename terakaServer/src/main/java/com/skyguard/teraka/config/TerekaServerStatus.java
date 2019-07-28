package com.skyguard.teraka.config;

import java.util.List;

public class TerekaServerStatus {

    private List<String> topics;
    private List<ServerInfo> serverInfos;
    private List<ServiceConfig> producers;
    private List<ServiceConfig> consumers;


    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<ServerInfo> getServerInfos() {
        return serverInfos;
    }

    public void setServerInfos(List<ServerInfo> serverInfos) {
        this.serverInfos = serverInfos;
    }

    public List<ServiceConfig> getProducers() {
        return producers;
    }

    public void setProducers(List<ServiceConfig> producers) {
        this.producers = producers;
    }

    public List<ServiceConfig> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<ServiceConfig> consumers) {
        this.consumers = consumers;
    }
}
