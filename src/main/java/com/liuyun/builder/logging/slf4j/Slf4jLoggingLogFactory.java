package com.liuyun.builder.logging.slf4j;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class Slf4jLoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new Slf4jImpl(clazz);
    }
    
}