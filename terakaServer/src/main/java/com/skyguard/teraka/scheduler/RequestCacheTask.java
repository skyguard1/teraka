package com.skyguard.teraka.scheduler;

import com.skyguard.teraka.invoker.TerakaClientInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestCacheTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(RequestCacheTask.class);

    @Override
    public void run() {
        TerakaClientInvoker.clearCache();
    }


}
