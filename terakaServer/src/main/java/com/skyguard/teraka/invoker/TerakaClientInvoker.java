package com.skyguard.teraka.invoker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.skyguard.teraka.annotation.EnableTerakaClient;
import com.skyguard.teraka.annotation.LoadBalance;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.entity.RequestEntity;
import com.skyguard.teraka.entity.RequestType;
import com.skyguard.teraka.http.HttpClientUtil;
import com.skyguard.teraka.loadbalance.BaseRule;
import com.skyguard.teraka.loadbalance.IRule;
import com.skyguard.teraka.scheduler.RequestCacheTask;
import com.skyguard.teraka.util.ClassUtil;
import com.skyguard.teraka.util.IpUtil;
import com.skyguard.teraka.util.JsonUtil;
import com.skyguard.teraka.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TerakaClientInvoker {

    private static final Logger LOG = LoggerFactory.getLogger(TerakaClientInvoker.class);

    private static int type;

    private static String topic;

    private static int weight;

    private static boolean isTerakaClient;

    private static Map<String,List<ServiceConfig>> requestCache = Maps.newConcurrentMap();

    private static  final  int size = 10;

    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(size,new ThreadFactoryBuilder().setNameFormat("terekaThread").build());

    private static ReentrantLock lock = new ReentrantLock();

    private static IRule rule;

    private static boolean enable = true;

    public static boolean isTerakaClient(){

        String packageName = PropertyUtil.getValue("teraka.package.name");
        boolean flag = false;
        if(!isTerakaClient) {
            if (StringUtils.isNotEmpty(packageName)) {
                Set<Class<?>> classes = ClassUtil.getClasses(packageName);
                for (Class clazz : classes) {
                    if (clazz.isAnnotationPresent(EnableTerakaClient.class)) {
                        EnableTerakaClient terakaClient = (EnableTerakaClient) clazz.getAnnotation(EnableTerakaClient.class);
                        type = terakaClient.type();
                        topic = terakaClient.topic();
                        weight = terakaClient.weight();
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    isTerakaClient = true;
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

    public static void register(){

        try {
            if(isTerakaClient()) {
                if(type!=0) {
                    String ip = IpUtil.getLocalIpAddr();
                    int port = Integer.parseInt(PropertyUtil.getValue("teraka.client.port"));
                    RequestEntity requestEntity = new RequestEntity();
                    requestEntity.setRequestType(type);
                    ServiceConfig serviceConfig = new ServiceConfig();
                    serviceConfig.setType(type);
                    serviceConfig.setIp(ip);
                    serviceConfig.setPort(port);
                    serviceConfig.setWeight(weight);
                    requestEntity.setServiceConfig(serviceConfig);
                    requestEntity.setTopic(topic);
                    String url = PropertyUtil.getValue("teraka.client.register.url");
                    String param = JsonUtil.toJsonString(requestEntity);
                    HttpClientUtil.postData(url, param);
                    if(type==RequestType.CONSUMER.getCode()){
                        initTask();
                    }
                }
            }
        }catch (Exception e){
            LOG.error("post data error",e);
        }



    }

    public static void initTask(){

        executorService.scheduleAtFixedRate(new RequestCacheTask(),10,30, TimeUnit.SECONDS);

    }

    public static void clearCache(){
        try {
            lock.lock();
            if(enable) {
                List<ServiceConfig> producers = sendRequest("");
                if(producers!=null) {
                    requestCache.clear();
                }
            }
        }catch (Exception e){
            LOG.error("clear cache error",e);
        }finally {
            lock.unlock();
        }
    }

    public static List<ServiceConfig> getProducerList(String topic){

        List<ServiceConfig> producers = Lists.newArrayList();

        try {
            lock.lock();
            if(requestCache.isEmpty()) {
                producers = sendRequest(topic);
                requestCache.put(topic,producers);
            }else {
                if(!requestCache.containsKey(topic)){
                    producers = sendRequest(topic);
                    requestCache.put(topic, producers);
                }else {
                    producers = requestCache.get(topic);
                }
            }
            enable = true;
        }catch (Exception e){
            LOG.error("get data error",e);
            enable = false;
        }finally {
            lock.unlock();
        }

        return producers;
    }

    private static List<ServiceConfig> sendRequest(String topic) throws Exception{

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setTopic(topic);
        String param = JsonUtil.toJsonString(requestEntity);
        String url = PropertyUtil.getValue("teraka.server.producer.url");
        String result = HttpClientUtil.postData(url, param);
        List<ServiceConfig> producers = JsonUtil.getObject(result, new TypeReference<List<ServiceConfig>>() {
        });

        return producers;
    }

    public static void stopTask(){
        executorService.shutdown();
    }


    public static ServiceConfig select(String topic){

        List<ServiceConfig> servers = getProducerList(topic);

        IRule rule = getRule();
        return getServer(servers,rule);

    }

    public static ServiceConfig getServer(List<ServiceConfig> servers,IRule rule){

        return rule.getServer(servers);
    }





}
