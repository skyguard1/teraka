package com.skyguard.teraka.filter;

import com.skyguard.teraka.config.ServiceConfig;

import java.util.List;

public interface Filter {


    public List<ServiceConfig> filterSerer(String topic);




}
