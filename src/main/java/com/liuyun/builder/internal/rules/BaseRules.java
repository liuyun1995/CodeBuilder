package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.config.label.TablesConfiguration;

//基础模型规则
public abstract class BaseRules implements Rules {

    protected TablesConfiguration tableConfiguration;

    protected IntrospectedTable introspectedTable;

    public BaseRules(IntrospectedTable introspectedTable) {
        super();
        this.introspectedTable = introspectedTable;
        this.tableConfiguration = introspectedTable.getTableConfiguration();
    }
    
    //生成insert方法
    @Override
    public boolean generateInsert() {
        return true;
    }
    
    //生成delete方法
    @Override
    public boolean generateDelete() {
        return true;
    }
    
    //生成update方法
    @Override
    public boolean generateUpdate() {
        return true;
    }
    
    //生成select
    @Override
    public boolean generateSelect() {
        return true;
    }

    @Override
    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }
    
    //计算所有字段的类型
    @Override
    public FullyQualifiedJavaType calculateAllFieldsClass() {
        String answer;
        if (generateBaseRecordClass()) {
            answer = introspectedTable.getBaseRecordType();
        } else {
            answer = introspectedTable.getPrimaryKeyType();
        }
        return new FullyQualifiedJavaType(answer);
    }
    
}
