package com.liuyun.builder.logging.nologging;

import com.liuyun.builder.logging.Log;

public class NoLoggingImpl implements Log {

    public NoLoggingImpl(Class<?> clazz) {
        // Do Nothing
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void error(String s, Throwable e) {
        // Do Nothing
    }

    @Override
    public void error(String s) {
        // Do Nothing
    }

    @Override
    public void debug(String s) {
        // Do Nothing
    }

    @Override
    public void warn(String s) {
        // Do Nothing
    }

}
