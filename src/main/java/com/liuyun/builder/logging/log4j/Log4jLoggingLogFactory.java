package com.liuyun.builder.logging.log4j;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class Log4jLoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new Log4jImpl(clazz);
    }
    
}