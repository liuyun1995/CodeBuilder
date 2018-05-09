package com.liuyun.builder.internal;

import com.liuyun.builder.api.DAOMethodNameCalculator;
import com.liuyun.builder.api.IntrospectedTable;

public class ExtendedDAOMethodNameCalculator implements DAOMethodNameCalculator {

    public ExtendedDAOMethodNameCalculator() {
        super();
    }

    @Override
    public String getInsertMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }
    
    @Override
    public String getDeleteMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }
    
    @Override
    public String getUpdateMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }
    
    @Override
    public String getSelectMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select");
        sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }
    
}
