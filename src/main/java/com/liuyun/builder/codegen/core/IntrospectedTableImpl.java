package com.liuyun.builder.codegen.core;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.codegen.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.javamodel.BaseRecordGenerator;
import org.mybatis.generator.codegen.javamodel.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;

import com.liuyun.builder.api.GeneratedJavaFile;
import com.liuyun.builder.api.GeneratedXmlFile;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.ProgressCallback;
import com.liuyun.builder.api.dom.xml.Document;

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
        AbstractJavaMapperGenerator javaClientGenerator = calculateJavaMapperGenerators(warnings, progressCallback);
        //获取XmlMapper生成器
        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }
    
    //获取JavaModel生成器
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }
    
    //获取JavaMapper生成器
    protected AbstractJavaMapperGenerator calculateJavaMapperGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }
        AbstractJavaMapperGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        javaMapperGenerators.add(javaGenerator);
        return javaGenerator;
    }
    
    //创建JavaClient生成器
    protected AbstractJavaMapperGenerator createJavaClientGenerator() {
        return new JavaMapperGenerator();
    }

    //获取XMLMapper生成器
    protected void calculateXmlMapperGenerator(AbstractJavaMapperGenerator javaClientGenerator, 
            List<String> warnings, ProgressCallback progressCallback) {
    	//如果javaClientGenerator为空
        if (javaClientGenerator == null) {
            if (tablesConfiguration.getXmlMapperConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
        	//否则就用javaClientGenerator生成
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
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
        	//获取编译单元
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            //构造GeneratedJavaFile
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                		        tablesConfiguration.getJavaModelConfiguration().getTarget(),
                                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                                context.getJavaFormatter());
                answer.add(gjf);
            }
        }
        //遍历JavaMapper生成器
        for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                                context.getJavaClientGeneratorConfiguration().getTargetProject(),
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
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getXmlMapperFileName(), getXmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
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

    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaMapperGenerator javaClientGenerator = createJavaClientGenerator();
        if (javaClientGenerator == null) {
            return false;
        } else {
            return javaClientGenerator.requiresXMLGenerator();
        }
    }
    
}
