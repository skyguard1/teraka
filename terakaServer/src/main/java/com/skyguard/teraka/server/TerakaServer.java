package com.skyguard.teraka.server;

import com.google.common.collect.Lists;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.config.TerekaServerStatus;
import com.skyguard.teraka.entity.RequestEntity;
import com.skyguard.teraka.http.HttpStatus;
import com.skyguard.teraka.invoker.TerakaServerInvoker;
import com.skyguard.teraka.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TerakaServer {

    private static final Logger LOG = LoggerFactory.getLogger(TerakaServer.class);

    public void start(){

        try {
            TerakaServerInvoker.start();
            LOG.info("terekaServer start success");
        }catch (Exception e){
            LOG.error("start server error",e);
        }

    }

    public void stop(){

        try {
            TerakaServerInvoker.stop();
        }catch (Exception e){
            LOG.error("stop server error",e);
        }

    }

    public void register(String request){

        try {
            RequestEntity requestEntity = JsonUtil.toObject(request, RequestEntity.class);
            TerakaServerInvoker.register(requestEntity);
        }catch (Exception e){
            LOG.error("send request error",e);
        }

    }

    public List<ServiceConfig> getProducerList(String request){

        List<ServiceConfig> producers = Lists.newArrayList();

        try {
            RequestEntity requestEntity = JsonUtil.toObject(request, RequestEntity.class);
            producers = TerakaServerInvoker.getProducerList(requestEntity);

        }catch (Exception e){
            LOG.error("get producer list error",e);
        }

        return producers;
    }

    public ServiceConfig getServer(String request){

        try {
            RequestEntity requestEntity = JsonUtil.toObject(request, RequestEntity.class);
            ServiceConfig serviceConfig = TerakaServerInvoker.select(requestEntity);
            HttpStatus.increase(serviceConfig);
            return serviceConfig;
        }catch (Exception e){
            LOG.error("get server error",e);
        }

        return null;
    }

    public TerekaServerStatus getServerStatus(){

        try {
            TerekaServerStatus serverStatus = TerakaServerInvoker.getServerStatus();
            return serverStatus;
        }catch (Exception e){
            LOG.error("get data error",e);
        }

        return null;
    }



    public void remove(String request){

        try {
            RequestEntity requestEntity = JsonUtil.toObject(request, RequestEntity.class);
            TerakaServerInvoker.remove(requestEntity);
        }catch (Exception e){
            LOG.error("send request error",e);
        }


    }




}
