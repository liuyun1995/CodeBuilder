package com.liuyun.builder.codegen.javamapper;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.ArrayList;
import java.util.List;
import com.liuyun.builder.api.CommentGenerator;
import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.JavaVisibility;
import com.liuyun.builder.codegen.core.AbstractJavaMapperGenerator;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;
import com.liuyun.builder.codegen.javamapper.elements.AbstractJavaMapperMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.DeleteMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.InsertMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.SelectMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.UpdateMethodGenerator;
import com.liuyun.builder.codegen.xmlmapper.XMLMapperGenerator;
import com.liuyun.builder.config.PropertyRegistry;

//JavaMapper生成器
public class JavaMapperGenerator extends AbstractJavaMapperGenerator {

    public JavaMapperGenerator() {
        super(true);
    }

    public JavaMapperGenerator(boolean requiresMatchedXMLGenerator) {
        super(requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        //获取JavaMapper类型
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getJavaMapperType());
        //根据类型新建接口
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = introspectedTable.getTableConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        
        //添加必要方法
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
    
    //添加插入方法
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    //添加删除方法
    protected void addDeleteMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    //添加更新方法
    protected void addUpdateMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    //添加查询方法
    protected void addSelectMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    //初始化和执行生成器
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
        return new XMLMapperGenerator();
    }
    
}
