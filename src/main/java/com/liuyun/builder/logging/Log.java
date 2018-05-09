package com.liuyun.builder.logging;

//日志接口
public interface Log {

    boolean isDebugEnabled();

    void error(String s, Throwable e);

    void error(String s);

    void debug(String s);

    void warn(String s);
}
