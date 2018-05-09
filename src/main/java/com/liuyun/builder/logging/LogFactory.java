package com.liuyun.builder.logging;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import org.mybatis.generator.logging.commons.JakartaCommonsLoggingLogFactory;
import org.mybatis.generator.logging.jdk14.Jdk14LoggingLogFactory;
import org.mybatis.generator.logging.log4j.Log4jLoggingLogFactory;
import org.mybatis.generator.logging.log4j2.Log4j2LoggingLogFactory;
import org.mybatis.generator.logging.nologging.NoLoggingLogFactory;
import org.mybatis.generator.logging.slf4j.Slf4jLoggingLogFactory;

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

    /**
     * This method will switch the logging implementation to Java native
     * logging. This is useful in situations where you want to use Java native
     * logging to log activity but Log4J is on the classpath. Note that this
     * method is only effective for log classes obtained after calling this
     * method. If you intend to use this method you should call it before
     * calling any other method.
     */
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
