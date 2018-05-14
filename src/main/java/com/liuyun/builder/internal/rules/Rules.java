package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;

//规则生成接口
public interface Rules {

	//生成insert方法
    boolean generateInsert();
    
    //生成delete方法
    boolean generateDelete();
    
    //生成update方法
    boolean generateUpdate();
    
    //生成select方法
    boolean generateSelect();
    
    //生成resultMap
    boolean generateResultMap();

    //生成BaseColumnList
    boolean generateBaseColumnList();
    
    //生成主键类
    boolean generatePrimaryKeyClass();

    //生成基础字段类
    boolean generateBaseRecordClass();
    
    //生成javaClient
    boolean generateJavaClient();

    //获取逆向表
    IntrospectedTable getIntrospectedTable();
    
    //计算所有字段类型
    FullyQualifiedJavaType calculateAllFieldsClass();
    
}
