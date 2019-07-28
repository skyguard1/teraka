package com.skyguard.teraka.loadbalance;

import com.google.common.collect.Lists;
import com.skyguard.teraka.config.ServiceConfig;
import com.skyguard.teraka.http.HttpStatus;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Random;

public class LeastActiveRule implements IRule{

    private final Random random = new Random();

    @Override
    public ServiceConfig getServer(List<ServiceConfig> servers) {

        if(CollectionUtils.isEmpty(servers)){
            return null;
        }

        if(servers.size()==1){
            return servers.get(0);
        }

        int length = servers.size();
        int leastActive = -1;
        int leastCount = 0;
        int index = 0;
        List<Integer> indices = Lists.newArrayList();

        for(int i=0;i<length;i++){
            ServiceConfig serviceConfig = servers.get(i);
            Integer num = HttpStatus.getStatus(serviceConfig);
            if(leastActive==-1||num<leastActive){
                indices.clear();
                leastActive = num;
                index = i;
                leastCount = 0;
                indices.add(i);
            }else if(num==leastActive){
                leastCount++;
                indices.add(i);
            }


        }

        if(leastCount>0){
            int length1 = indices.size();
            List<ServiceConfig> serviceConfigs = Lists.newArrayList();
            for(int i=0;i<length1;i++){
                int leastIndex = indices.get(i);
                ServiceConfig serviceConfig = servers.get(leastIndex);
                serviceConfigs.add(serviceConfig);
            }

            int size = serviceConfigs.size();
            ServiceConfig serviceConfig = serviceConfigs.get(random.nextInt(size));
            return serviceConfig;

        }else {

            ServiceConfig serviceConfig = servers.get(index);

            return serviceConfig;
        }

    }
}
