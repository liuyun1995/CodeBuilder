package com.liuyun.builder.api;

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.config.label.Context;

import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;


public interface JavaTypeResolver {
	
	//添加配置属性
    void addConfigurationProperties(Properties properties);

    //设置上下文
    void setContext(Context context);

    //设置警告信息
    void setWarnings(List<String> warnings);

    //获取指定列的java类型
    FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn);

    //获取指定列的jdbc类型
    String calculateJdbcTypeName(IntrospectedColumn introspectedColumn);
    
}
