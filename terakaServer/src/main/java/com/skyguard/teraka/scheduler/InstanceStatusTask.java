package com.skyguard.teraka.scheduler;

import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.http.HttpClientUtil;
import com.skyguard.teraka.registry.TerakaRegistry;
import com.skyguard.teraka.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InstanceStatusTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(InstanceStatusTask.class);

    @Override
    public void run() {


            TerakaRegistry terakaRegistry = TerakaRegistry.getInstance();
            List<ServiceConfig> producerList = terakaRegistry.getProducer();
            for (ServiceConfig serviceConfig : producerList) {
                try {
                    StringBuilder url = new StringBuilder();
                    String testUrl = PropertyUtil.getValue("teraka.testUrl");
                    url.append("http://").append(serviceConfig.getIp()).append(":").append(serviceConfig.getPort()).append(testUrl);
                    HttpClientUtil.getData(url.toString());
                }catch (Exception e){
                    LOG.error("get data error,service ip is{},port is {}",serviceConfig.getIp(),serviceConfig.getPort(),e);
                    terakaRegistry.removeProducer("",serviceConfig);
                }
            }


    }




}
