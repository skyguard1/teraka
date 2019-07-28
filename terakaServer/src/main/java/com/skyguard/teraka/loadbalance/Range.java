package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;

public class Range {

    private ServiceConfig serviceConfig;
    private int low;
    private int high;

    public Range(ServiceConfig serviceConfig, int low, int high) {
        this.serviceConfig = serviceConfig;
        this.low = low;
        this.high = high;
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
}
