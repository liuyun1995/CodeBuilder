package com.liuyun.builder.codegen.core;

import java.util.List;

import com.liuyun.builder.api.ProgressCallback;
import com.liuyun.builder.codegen.javamapper.SimpleJavaClientGenerator;
import com.liuyun.builder.codegen.javamodel.SimpleModelGenerator;
import com.liuyun.builder.codegen.xmlmapper.SimpleXMLMapperGenerator;

public class IntrospectedTableSimpleImpl extends IntrospectedTableImpl {
    
	public IntrospectedTableSimpleImpl() {
        super();
    }

    @Override
    protected void calculateXmlMapperGenerator( List<String> warnings, ProgressCallback progressCallback) {
        xmlMapperGenerator = new SimpleXMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    //获取JavaClient生成器
    @Override
    protected AbstractJavaMapperGenerator createJavaClientGenerator() {
        if (tablesConfiguration.getJavaMapperConfiguration() == null) {
            return null;
        }
        return new SimpleJavaClientGenerator();
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        //生成包含所有键的JavaModel
    	AbstractJavaGenerator javaGenerator = new SimpleModelGenerator();
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaModelGenerators.add(javaGenerator);
    }
}
