package com.liuyun.builder.logging.jdk14;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.liuyun.builder.logging.Log;

public class Jdk14LoggingImpl implements Log {

    private Logger log;

    public Jdk14LoggingImpl(Class<?> clazz) {
        log = Logger.getLogger(clazz.getName());
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(Level.SEVERE, s, e);
    }

    @Override
    public void error(String s) {
        log.log(Level.SEVERE, s);
    }

    @Override
    public void debug(String s) {
        log.log(Level.FINE, s);
    }

    @Override
    public void warn(String s) {
        log.log(Level.WARNING, s);
    }

}
