package com.skyguard.teraka.http;

import java.util.Map;

public class HttpRequest {

    private String ip;
    private int port;
    private String url;
    private Map<String,Object> param;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public HttpRequest withIp(String ip){
        this.ip = ip;
        return this;
    }

    public HttpRequest withPort(int port){
        this.port = port;
        return this;
    }

    public HttpRequest withUrl(String url){
        this.url = url;
        return this;
    }

    public HttpRequest withParam(Map<String,Object> param){
        this.param = param;
        return this;
    }


    public HttpRequest build(){

        HttpRequest request = new HttpRequest();
        request.setIp(ip);
        request.setPort(port);
        request.setUrl(url);
        request.setParam(param);

        return request;
    }



}
