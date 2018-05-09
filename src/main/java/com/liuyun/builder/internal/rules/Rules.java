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
    
    //生成resultMap(xmlMapper)
    boolean generateResultMap();

    //生成BaseColumnList(xmlMapper)
    boolean generateBaseColumnList();
    
    //是否是主键(javaModel)
    boolean generatePrimaryKeyClass();

    //是否是基础字段(javaModel)
    boolean generateBaseRecordClass();
    
    //是否是javaClient
    boolean generateJavaClient();

    //获取逆向表
    IntrospectedTable getIntrospectedTable();
    
    //计算所有字段类型
    FullyQualifiedJavaType calculateAllFieldsClass();
    
}
