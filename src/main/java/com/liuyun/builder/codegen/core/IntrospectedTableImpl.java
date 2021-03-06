package com.liuyun.builder.codegen.core;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.GeneratedJavaFile;
import com.liuyun.builder.api.GeneratedXmlFile;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.ProgressCallback;
import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.codegen.javamapper.JavaMapperGenerator;
import com.liuyun.builder.codegen.javamodel.SimpleModelGenerator;
import com.liuyun.builder.codegen.xmlmapper.XMLMapperGenerator;
import com.liuyun.builder.config.PropertyRegistry;

//逆向表的实现
public class IntrospectedTableImpl extends IntrospectedTable {
    
	//JavaModel生成器
    protected List<AbstractJavaGenerator> javaModelGenerators;

    //JavaMapper生成器
    protected List<AbstractJavaGenerator> javaMapperGenerators;

    //XMLMapper生成器
    protected AbstractXmlMapperGenerator xmlMapperGenerator;

    public IntrospectedTableImpl() {
        javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        javaMapperGenerators = new ArrayList<AbstractJavaGenerator>();
    }

    //获取生成器
    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
    	//获取JavaModel生成器
        calculateJavaModelGenerators(warnings, progressCallback);
        //获取JavaMapper生成器
        calculateJavaMapperGenerators(warnings, progressCallback);
        //获取XmlMapper生成器
        calculateXmlMapperGenerator(warnings, progressCallback);
    }
    
    //获取JavaModel生成器
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
    	AbstractJavaGenerator javaGenerator = new SimpleModelGenerator();
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaModelGenerators.add(javaGenerator);
    }
    
    //获取JavaMapper生成器
    protected void calculateJavaMapperGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaMapperGenerator javaMapperGenerator = new JavaMapperGenerator();
        initializeAbstractGenerator(javaMapperGenerator, warnings, progressCallback);
        javaMapperGenerators.add(javaMapperGenerator);
    }
    
    //获取XMLMapper生成器
    protected void calculateXmlMapperGenerator(List<String> warnings, ProgressCallback progressCallback) {
        xmlMapperGenerator = new XMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    //初始化生成器
    protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (abstractGenerator == null) return;
        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    //生成Java文件
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        //遍历JavaModel生成器
        for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            //构造GeneratedJavaFile
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                		        tablesConfiguration.getTargetProject(),
                		        tablesConfiguration.getTargetPackage(),
                                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                                context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        //遍历JavaMapper生成器
        for (AbstractJavaGenerator javaGenerator : javaMapperGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                		        tablesConfiguration.getTargetProject(),
                		        tablesConfiguration.getTargetPackage(),
                                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                                context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        return answer;
    }

    //生成XML文件
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();
        //生获取XmlMapper生成器
        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, getXmlMapperFileName(), 
                    tablesConfiguration.getTargetProject(),
                    tablesConfiguration.getTargetPackage(),
                    true, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        return answer;
    }
    
    //----------------------------------------------下面感觉没什么用-----------------------------------------

    @Override
    public int getGenerationSteps() {
        return javaModelGenerators.size() + javaMapperGenerators.size() + (xmlMapperGenerator == null ? 0 : 1);
    }
    
}
