package com.skyguard.teraka.invoker;

import com.google.common.collect.Lists;
import com.skyguard.teraka.annotation.EnableTerakaServer;
import com.skyguard.teraka.annotation.LoadBalance;
import com.skyguard.teraka.config.ServerInfo;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.config.TerekaServerStatus;
import com.skyguard.teraka.entity.RequestEntity;
import com.skyguard.teraka.entity.RequestType;
import com.skyguard.teraka.filter.BaseFilter;
import com.skyguard.teraka.filter.Filter;
import com.skyguard.teraka.handler.TerakaServerHandler;
import com.skyguard.teraka.http.HttpClientUtil;
import com.skyguard.teraka.loadbalance.BaseRule;
import com.skyguard.teraka.loadbalance.IRule;
import com.skyguard.teraka.registry.TerakaRegistry;
import com.skyguard.teraka.scheduler.InstanceScheduler;
import com.skyguard.teraka.server.Node;
import com.skyguard.teraka.server.NodeProcessor;
import com.skyguard.teraka.util.ClassUtil;
import com.skyguard.teraka.util.JsonUtil;
import com.skyguard.teraka.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TerakaServerInvoker {

    private static final Logger LOG = LoggerFactory.getLogger(TerakaServerInvoker.class);

    private static boolean isTerakaServer;

    private static IRule rule;

    private static Filter filter = new BaseFilter();

    public static boolean isTerakaServer(){

        String packageName = PropertyUtil.getValue("teraka.package.name");
        boolean flag = false;
        if(!isTerakaServer) {
            if (StringUtils.isNotEmpty(packageName)) {
                Set<Class<?>> classes = ClassUtil.getClasses(packageName);
                for (Class clazz : classes) {
                    if (clazz.isAnnotationPresent(EnableTerakaServer.class)) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    isTerakaServer = true;
                }

            }
        }else {
            flag = true;
        }



        return flag;
    }

    public static IRule getRule(){

        if(rule==null) {
            rule = new BaseRule();

            try {
                String packageName = PropertyUtil.getValue("teraka.package.name");
                if (StringUtils.isNotEmpty(packageName)) {
                    Set<Class<?>> classes = ClassUtil.getClasses(packageName);
                    for (Class clazz : classes) {
                        if (clazz.isAnnotationPresent(LoadBalance.class)) {
                            LoadBalance loadBalance = (LoadBalance) clazz.getAnnotation(LoadBalance.class);
                            Class<?> ruleClass = loadBalance.rule();
                            rule = (IRule) ruleClass.newInstance();
                            break;
                        }
                    }


                }
            } catch (Exception e) {
                LOG.error("get rule error", e);
            }
        }

        return rule;
    }

    public static void register(RequestEntity requestEntity){

        if(isTerakaServer()) {
            TerakaRegistry registry = TerakaRegistry.getInstance();
            ServiceConfig serviceConfig = requestEntity.getServiceConfig();
            String topic = "";
            if(StringUtils.isNotEmpty(requestEntity.getTopic())){
                topic = requestEntity.getTopic();
            }
            if (requestEntity.getRequestType() == RequestType.PRODUCER.getCode()) {
                registry.registerProducer(topic, serviceConfig);
            } else if (requestEntity.getRequestType() == RequestType.CONSUMER.getCode()) {
                registry.registerConsumer(topic, serviceConfig);
            }
            replicaNodes(requestEntity);
        }else {
            LOG.info("is not teraka server");
        }

    }

    private static void replicaNodes(RequestEntity requestEntity){

        List<Node> nodes = NodeProcessor.getServerNode();

        try {
            for (Node node : nodes) {
                String url = "http://" + node.getIp() + ":" + node.getPort() + PropertyUtil.getValue("teraka.client.register.url");
                String param = JsonUtil.toJsonString(requestEntity);
                HttpClientUtil.postData(url, param);
            }
        }catch (Exception e){
            LOG.error("get data error",e);
        }

    }

    public static void start(){

        if(isTerakaServer()){
            InstanceScheduler.scheduleInstanceTask();
        }

    }

    public static void stop(){

        if(isTerakaServer()){
            InstanceScheduler.stop();
        }


    }

    public static List<ServiceConfig> getProducerList(RequestEntity requestEntity){

        List<ServiceConfig> producers = Lists.newArrayList();
        if(isTerakaServer()){
            String enableFilter = PropertyUtil.getValue("teraka.server.enableFilter");
            if(StringUtils.isNotEmpty(enableFilter)&&enableFilter.equals("true")){
                producers = filter.filterSerer(requestEntity.getTopic());
            }else {
                TerakaRegistry terakaRegistry = TerakaRegistry.getInstance();
                producers = terakaRegistry.getProducerList(requestEntity.getTopic());
            }

        }

        return producers;
    }


    public static ServiceConfig select(RequestEntity requestEntity){

        List<ServiceConfig> servers = getProducerList(requestEntity);

        IRule rule = getRule();
        return getServer(servers,rule);

    }

    public static ServiceConfig getServer(List<ServiceConfig> servers,IRule rule){

        return rule.getServer(servers);
    }

    public static TerekaServerStatus getServerStatus(){

        TerekaServerStatus serverStatus = new TerekaServerStatus();
        TerakaRegistry terakaRegistry = TerakaRegistry.getInstance();
        Map<String,List<ServiceConfig>> dataMap = terakaRegistry.getDataMap();
        Set<String> topics = dataMap.keySet();
        List<String> list = Lists.newArrayList(topics);
        serverStatus.setTopics(list);
        List<ServerInfo> serverInfos = TerakaServerHandler.getServerInfo(dataMap);
        serverStatus.setServerInfos(serverInfos);
        List<ServiceConfig> producers = terakaRegistry.getProducer();
        serverStatus.setProducers(producers);
        List<ServiceConfig> consumers = terakaRegistry.getConsumer();
        serverStatus.setConsumers(consumers);

        return serverStatus;
    }

    public static void remove(RequestEntity requestEntity){

        if(isTerakaServer()){
            TerakaRegistry registry = TerakaRegistry.getInstance();
            ServiceConfig serviceConfig = requestEntity.getServiceConfig();
            String topic = "";
            if(StringUtils.isNotEmpty(requestEntity.getTopic())){
                topic = requestEntity.getTopic();
            }
            if(requestEntity.getRequestType()==RequestType.PRODUCER.getCode()){
                  registry.removeProducer(topic,serviceConfig);
            }else {
                registry.removeConsumer(topic,serviceConfig);
            }


        }


    }





}
