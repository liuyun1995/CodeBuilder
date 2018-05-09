package com.liuyun.builder.internal;

import com.liuyun.builder.api.DAOMethodNameCalculator;
import com.liuyun.builder.api.IntrospectedTable;

public class DefaultDAOMethodNameCalculator implements DAOMethodNameCalculator {

    public DefaultDAOMethodNameCalculator() {
        super();
    }

    @Override
    public String getInsertMethodName(IntrospectedTable introspectedTable) {
        return "insert";
    }
    
    @Override
    public String getDeleteMethodName(IntrospectedTable introspectedTable) {
        return "delete";
    }
    
    @Override
    public String getUpdateMethodName(IntrospectedTable introspectedTable) {
    	return "update"; 
    }

    @Override
    public String getSelectMethodName(IntrospectedTable introspectedTable) {
        return "select"; 
    }
    
}
