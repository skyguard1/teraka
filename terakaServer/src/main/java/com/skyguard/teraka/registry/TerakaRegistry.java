package com.skyguard.teraka.registry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.skyguard.teraka.config.ServiceConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TerakaRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(TerakaRegistry.class);

    private String name;
    private Map<String,List<ServiceConfig>> producer = Maps.newConcurrentMap();
    private Map<String,List<ServiceConfig>> consumer = Maps.newConcurrentMap();
    private List<ServiceConfig> producerList = Lists.newArrayList();
    private List<ServiceConfig> consumerList = Lists.newArrayList();

    public static class TerakaRegistryHolder{
        private static TerakaRegistry instance = new TerakaRegistry();
    }

    public static TerakaRegistry getInstance(){
        return TerakaRegistryHolder.instance;
    }

    private TerakaRegistry(){

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void registerProducer(String topic,ServiceConfig serviceConfig){

        LOG.info("register producer "+serviceConfig.getIp()+":"+serviceConfig.getPort());

        if(!producer.containsKey(topic)){
            List<ServiceConfig> producerList = Lists.newArrayList();
            producerList.add(serviceConfig);
            producer.put(topic,producerList);
        }else {
            List<ServiceConfig> producerLIst = producer.get(topic);
            if(!containsValue(producerLIst,serviceConfig)){
                producerLIst.add(serviceConfig);
            }
            producer.put(topic,producerLIst);
        }

        if(!containsValue(producerList,serviceConfig)){
            producerList.add(serviceConfig);
        }
    }

    private boolean containsValue(List<ServiceConfig> serviceConfigs,ServiceConfig serviceConfig){

        if(CollectionUtils.isNotEmpty(serviceConfigs)){
            return serviceConfigs.stream().anyMatch(serviceConfig1 -> {
                boolean flag = false;
                if(serviceConfig1.getIp().equals(serviceConfig.getIp())&&serviceConfig1.getPort()==serviceConfig.getPort()){
                    flag = true;
                }
                return flag;
            });
        }

        return false;
    }

    public void registerConsumer(String topic,ServiceConfig serviceConfig){

        LOG.info("register consumer:"+serviceConfig.getIp()+":"+serviceConfig.getPort());

        if(!consumer.containsKey(topic)){
            List<ServiceConfig> consumerList = Lists.newArrayList();
            consumerList.add(serviceConfig);
            consumer.put(topic,consumerList);
        }else {
            List<ServiceConfig> consumerList = consumer.get(topic);
            if(!containsValue(consumerList,serviceConfig)){
                consumerList.add(serviceConfig);
            }
            consumer.put(topic,consumerList);
        }

        if(!containsValue(consumerList,serviceConfig)){
            consumerList.add(serviceConfig);
        }
    }

    public List<ServiceConfig> getProducerList(String topic){

        List<ServiceConfig> producers = Lists.newArrayList();
        if(StringUtils.isNotEmpty(topic)){
            producers = producer.get(topic);
        }else{
            producers = producerList;
        }

        return producers;
    }

    public void removeProducer(String topic,ServiceConfig serviceConfig){

        if(producer.containsKey(topic)){
            List<ServiceConfig> producers = producer.get(topic);
            producers.remove(serviceConfig);
        }else {
            Set<String> keys = producer.keySet();
            for(String str:keys){
                List<ServiceConfig> producerList = producer.get(str);
                if(producerList.contains(serviceConfig)){
                    producerList.remove(serviceConfig);
                }
            }
        }

        producerList.remove(serviceConfig);

    }

    public void removeConsumer(String topic,ServiceConfig serviceConfig){

        if(consumer.containsKey(topic)){
            List<ServiceConfig> consumers = consumer.get(topic);
            consumers.remove(serviceConfig);
        }else {
            Set<String> keys = consumer.keySet();
            for(String str:keys){
                List<ServiceConfig> cosumerList = consumer.get(str);
                if(cosumerList.contains(serviceConfig)){
                    cosumerList.remove(serviceConfig);
                }
            }
        }

        consumerList.remove(serviceConfig);

    }

    public Map<String,List<ServiceConfig>> getDataMap(){
        return producer;
    }

    public List<ServiceConfig> getProducer(){
        return producerList;
    }

    public List<ServiceConfig> getConsumer(){
        return consumerList;
    }


}
