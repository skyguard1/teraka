package com.skyguard.teraka.spring.web;

import com.google.common.collect.Lists;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class LoadBalanceRestTemplate extends RestTemplate{

    public LoadBalanceRestTemplate(){
        List<ClientHttpRequestInterceptor> interceptors = Lists.newArrayList();
        LoadBalanceInterceptor interceptor = new LoadBalanceInterceptor();
        interceptors.add(interceptor);
        this.setInterceptors(interceptors);
    }





}
