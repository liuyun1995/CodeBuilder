package com.liuyun.builder.codegen.javamapper;

import static org.mybatis.generator.internal.util.StringUtil.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.javamapper.elements.DeleteMethodGenerator;
import org.mybatis.generator.codegen.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.codegen.javamapper.elements.SelectMethodGenerator;
import org.mybatis.generator.codegen.javamapper.elements.UpdateMethodGenerator;
import org.mybatis.generator.codegen.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;

import com.liuyun.builder.api.CommentGenerator;
import com.liuyun.builder.codegen.core.AbstractJavaMapperGenerator;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;

public class SimpleJavaClientGenerator extends AbstractJavaMapperGenerator {

    public SimpleJavaClientGenerator() {
        super(true);
    }

    public SimpleJavaClientGenerator(boolean requiresMatchedXMLGenerator) {
        super(requiresMatchedXMLGenerator);
    }

    //获取编译单元
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getJavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }

        //添加一些方法
        addInsertMethod(interfaze);
        addDeleteMethod(interfaze);
        addUpdateMethod(interfaze);
        addSelectMethod(interfaze);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addDeleteMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    protected void addUpdateMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void addSelectMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator, Interface interfaze) {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addInterfaceElements(interfaze);
    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return null;
    }

    @Override
    public AbstractXmlMapperGenerator getMatchedXMLGenerator() {
        return new SimpleXMLMapperGenerator();
    }
}
