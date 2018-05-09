package com.liuyun.builder.logging;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import com.liuyun.builder.logging.commons.JakartaCommonsLoggingLogFactory;
import com.liuyun.builder.logging.jdk14.Jdk14LoggingLogFactory;
import com.liuyun.builder.logging.log4j.Log4jLoggingLogFactory;
import com.liuyun.builder.logging.log4j2.Log4j2LoggingLogFactory;
import com.liuyun.builder.logging.nologging.NoLoggingLogFactory;
import com.liuyun.builder.logging.slf4j.Slf4jLoggingLogFactory;

//日志工厂
public class LogFactory {
	
    private static AbstractLogFactory logFactory;
    
    public static String MARKER = "MYBATIS-GENERATOR"; 

    static {
    	//依次尝试加载下列日志
    	tryImplementation(new Log4jLoggingLogFactory());
        tryImplementation(new Slf4jLoggingLogFactory());
        tryImplementation(new JakartaCommonsLoggingLogFactory());
        tryImplementation(new Log4j2LoggingLogFactory());
        tryImplementation(new Jdk14LoggingLogFactory());
        tryImplementation(new NoLoggingLogFactory());
    }

    //获取log
    public static Log getLog(Class<?> clazz) {
        try {
            return logFactory.getLog(clazz);
        } catch (Throwable t) {
            throw new RuntimeException(getString("RuntimeError.21", clazz.getName(), t.getMessage()), t);
        }
    }
    
    public static synchronized void forceJavaLogging() {
        setImplementation(new Jdk14LoggingLogFactory());
    }

    public static synchronized void forceSlf4jLogging() {
        setImplementation(new Slf4jLoggingLogFactory());
    }

    public static synchronized void forceCommonsLogging() {
        setImplementation(new JakartaCommonsLoggingLogFactory());
    }

    public static synchronized void forceLog4jLogging() {
        setImplementation(new Log4jLoggingLogFactory());
    }

    public static synchronized void forceLog4j2Logging() {
        setImplementation(new Log4j2LoggingLogFactory());
    }

    public static synchronized void forceNoLogging() {
        setImplementation(new NoLoggingLogFactory());
    }

    public static void setLogFactory(AbstractLogFactory logFactory) {
        setImplementation(logFactory);
    }

    private static void tryImplementation(AbstractLogFactory factory) {
        if (logFactory == null) {
            try {
                setImplementation(factory);
            } catch (LogException e) {
                // ignore
            }
        }
    }

    //设置日志的实现
    private static void setImplementation(AbstractLogFactory factory) {
        try {
            Log log = factory.getLog(LogFactory.class);
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + factory + "' adapter.");
            }
            logFactory = factory;
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation.  Cause: " + t.getMessage(), t); 
        }
    }
}
