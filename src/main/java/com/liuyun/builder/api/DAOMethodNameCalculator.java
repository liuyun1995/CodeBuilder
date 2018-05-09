package com.liuyun.builder.api;

//获取DAO方法名
public interface DAOMethodNameCalculator {

    String getInsertMethodName(IntrospectedTable introspectedTable);

    String getDeleteMethodName(IntrospectedTable introspectedTable);
    
    String getUpdateMethodName(IntrospectedTable introspectedTable);

    String getSelectMethodName(IntrospectedTable introspectedTable);

}
