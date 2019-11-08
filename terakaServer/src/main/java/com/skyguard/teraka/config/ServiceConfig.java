package com.skyguard.teraka.config;

import java.util.Objects;

public class ServiceConfig {

    private int type;
    private String ip;
    private int port;
    private int weight;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceConfig that = (ServiceConfig) o;
        return port == that.port &&
               ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
