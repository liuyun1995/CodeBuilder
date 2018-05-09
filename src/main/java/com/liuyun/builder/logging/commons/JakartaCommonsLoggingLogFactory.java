package com.liuyun.builder.logging.commons;

import com.liuyun.builder.logging.AbstractLogFactory;
import com.liuyun.builder.logging.Log;

public class JakartaCommonsLoggingLogFactory implements AbstractLogFactory {
	
    @Override
    public Log getLog(Class<?> clazz) {
        return new JakartaCommonsLoggingImpl(clazz);
    }
    
}
