package com.skyguard.teraka.spring.config;

import com.skyguard.teraka.server.TerakaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerakaServerConfig {

    @Bean(initMethod = "start")
    public TerakaServer terakaServer(){
        return new TerakaServer();
    }


}
