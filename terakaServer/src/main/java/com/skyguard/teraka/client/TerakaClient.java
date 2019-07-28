package com.skyguard.teraka.client;

import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.http.HttpClientUtil;
import com.skyguard.teraka.http.HttpRequest;
import com.skyguard.teraka.http.HttpStatus;
import com.skyguard.teraka.invoker.TerakaClientInvoker;
import com.skyguard.teraka.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TerakaClient {

    private static final Logger LOG = LoggerFactory.getLogger(TerakaClient.class);

    public void register(){
        try {
            TerakaClientInvoker.register();
        }catch (Exception e){
            LOG.error("send request error",e);
        }
    }

    public List<ServiceConfig> getProducerList(String topic){

        List<ServiceConfig> producers = TerakaClientInvoker.getProducerList(topic);

        return producers;
    }

    public ServiceConfig getServer(String topic){

        try {
            
            ServiceConfig serviceConfig = TerakaClientInvoker.select(topic);
            HttpStatus.increase(serviceConfig);
            return serviceConfig;
        }catch (Exception e){
            LOG.error("get server error",e);
        }

        return null;
    }

    public String invoke(HttpRequest request){

        try {
            String url = UrlUtil.getUrl(request);
            String result = HttpClientUtil.getData(url);
            return result;
        }catch (Exception e){
            LOG.error("get data error",e);
        }

        return null;
    }


    


}
