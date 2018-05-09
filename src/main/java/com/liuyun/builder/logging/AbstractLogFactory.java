package com.liuyun.builder.logging;

public interface AbstractLogFactory {
	
    Log getLog(Class<?> targetClass);
    
}
