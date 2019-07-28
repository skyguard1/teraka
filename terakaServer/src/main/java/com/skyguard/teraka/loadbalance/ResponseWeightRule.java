package com.skyguard.teraka.loadbalance;

import com.skyguard.teraka.config.ServiceConfig;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ResponseWeightRule implements IRule{

    private static final Logger LOG = LoggerFactory.getLogger(ResponseWeightRule.class);

    private final Random random = new Random();

    @Override
    public ServiceConfig getServer(List<ServiceConfig> servers) {

        if(CollectionUtils.isEmpty(servers)){
            return null;
        }

        if(servers.size()==1){
            return servers.get(0);
        }

        int weight = getAllWeight(servers);
        int length = servers.size();
        if(weight==0){
            return servers.get(random.nextInt(length));
        }

        List<Range> ranges = servers.stream().filter(serviceConfig -> serviceConfig.getWeight()>0).map(serviceConfig -> new Range(serviceConfig,1,serviceConfig.getWeight())).collect(Collectors.toList());


        for(int i=1;i<ranges.size();i++){
            int low = ranges.get(i-1).getHigh();
            ranges.get(i).setLow(low+1);
            int high = ranges.get(i).getHigh();
            ranges.get(i).setHigh(low+high);
        }

        int num = random.nextInt(weight)+1;

        ServiceConfig serviceConfig = null;
        int low = 0;
        int high = ranges.size();
        while (low<=high){
            int middle = (low+high)/2;
            if(num>=ranges.get(middle).getLow()&&num<=ranges.get(middle).getHigh()){
                serviceConfig = ranges.get(middle).getServiceConfig();
                break;
            }else if(num<ranges.get(middle).getLow()){
                high = middle-1;
            }else{
                low = middle+1;
            }

        }


        if(serviceConfig!=null){
            LOG.info("get server ip:"+serviceConfig.getIp()+",port:"+serviceConfig.getPort());
        }else{
            LOG.info("no server found");
        }


        return serviceConfig;
    }

    private int getAllWeight(List<ServiceConfig> servers){

        int weight = 0;

        for(ServiceConfig serviceConfig:servers){
            weight+=serviceConfig.getWeight();
        }


        return weight;
    }


}
