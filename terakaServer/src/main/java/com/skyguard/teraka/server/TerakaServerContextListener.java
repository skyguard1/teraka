package com.skyguard.teraka.server;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TerakaServerContextListener implements ServletContextListener{



        @Override
        public void contextInitialized(ServletContextEvent sce){

            TerakaServer terakaServer = new TerakaServer();
            terakaServer.start();

        }



}
