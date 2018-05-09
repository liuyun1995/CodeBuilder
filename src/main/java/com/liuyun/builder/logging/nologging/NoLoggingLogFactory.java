package com.liuyun.builder.logging.nologging;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class NoLoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new NoLoggingImpl(clazz);
    }
    
}