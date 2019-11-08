package com.skyguard.teraka.spring.config;

import com.skyguard.teraka.client.TerakaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerakaClientConfig {


    @Bean
    public TerakaClient terakaClient(){
        return new TerakaClient();
    }


}
