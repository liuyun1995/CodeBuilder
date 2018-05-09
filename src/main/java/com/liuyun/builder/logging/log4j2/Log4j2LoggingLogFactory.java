package com.liuyun.builder.logging.log4j2;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class Log4j2LoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new Log4j2Impl(clazz);
    }
    
}
