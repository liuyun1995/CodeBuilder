package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;

//规则代表
public class RulesDelegate implements Rules {
	
    protected Rules rules;

    public RulesDelegate(Rules rules) {
        this.rules = rules;
    }
    
    @Override
    public boolean generateInsert() {
        return rules.generateInsert();
    }
    
    @Override
    public boolean generateDelete() {
        return rules.generateDelete();
    }

    @Override
    public boolean generateUpdate() {
        return rules.generateUpdate();
    }
    
    @Override
    public boolean generateSelect() {
        return rules.generateSelect();
    }
    
    @Override
    public boolean generateResultMap() {
        return rules.generateResultMap();
    }
    
    @Override
    public boolean generateBaseColumnList() {
        return rules.generateBaseColumnList();
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return rules.generatePrimaryKeyClass();
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return rules.generateBaseRecordClass();
    }
    
    @Override
    public boolean generateJavaClient() {
        return rules.generateJavaClient();
    }

    @Override
    public IntrospectedTable getIntrospectedTable() {
        return rules.getIntrospectedTable();
    }
    
    @Override
    public FullyQualifiedJavaType calculateAllFieldsClass() {
        return rules.calculateAllFieldsClass();
    }
    
}
