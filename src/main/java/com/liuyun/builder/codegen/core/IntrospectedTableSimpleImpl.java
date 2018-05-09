package com.liuyun.builder.codegen.core;

import java.util.List;

import org.mybatis.generator.codegen.javamapper.SimpleAnnotatedClientGenerator;
import org.mybatis.generator.codegen.javamapper.SimpleJavaClientGenerator;
import org.mybatis.generator.codegen.javamodel.SimpleModelGenerator;
import org.mybatis.generator.codegen.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.internal.ObjectFactory;

import com.liuyun.builder.api.ProgressCallback;

public class IntrospectedTableSimpleImpl extends IntrospectedTableImpl {
    
	public IntrospectedTableSimpleImpl() {
        super();
    }

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaMapperGenerator javaClientGenerator, 
            List<String> warnings,
            ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (tablesConfiguration.getXmlMapperConfiguration() != null) {
                xmlMapperGenerator = new SimpleXMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
        
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    //获取JavaClient生成器
    @Override
    protected AbstractJavaMapperGenerator createJavaClientGenerator() {
        if (tablesConfiguration.getJavaMapperConfiguration() == null) {
            return null;
        }
        
        String type = tablesConfiguration.getJavaMapperConfiguration().getConfigurationType();

        AbstractJavaMapperGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { 
            javaGenerator = new SimpleJavaClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { 
            javaGenerator = new SimpleAnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { 
            javaGenerator = new SimpleJavaClientGenerator();
        } else {
            javaGenerator = (AbstractJavaMapperGenerator) ObjectFactory.createInternalObject(type);
        }
        return javaGenerator;
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        //生成包含所有键的JavaModel
    	AbstractJavaGenerator javaGenerator = new SimpleModelGenerator();
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaModelGenerators.add(javaGenerator);
    }
}
