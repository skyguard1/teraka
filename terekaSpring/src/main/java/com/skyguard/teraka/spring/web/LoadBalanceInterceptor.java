package com.skyguard.teraka.spring.web;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoadBalanceInterceptor implements ClientHttpRequestInterceptor{


    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {

        HttpRequest httpRequest1 = HttpRequestProcessor.processHttpRequest(httpRequest);

        return execution.execute(httpRequest1,bytes);
    }
}
