package com.liuyun.builder.logging.jdk14;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class Jdk14LoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new Jdk14LoggingImpl(clazz);
    }
    
}