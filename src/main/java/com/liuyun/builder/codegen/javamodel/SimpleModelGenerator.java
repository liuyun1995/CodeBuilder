package com.liuyun.builder.codegen.javamodel;

import static com.liuyun.builder.internal.utils.JavaBeansUtil.getJavaBeansField;
import static com.liuyun.builder.internal.utils.JavaBeansUtil.getJavaBeansGetter;
import static com.liuyun.builder.internal.utils.JavaBeansUtil.getJavaBeansSetter;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.CommentGenerator;
import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.Plugin;
import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.api.dom.java.Field;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.JavaVisibility;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.TopLevelClass;
import com.liuyun.builder.codegen.core.AbstractJavaGenerator;
import com.liuyun.builder.codegen.core.RootClassInfo;

//生成JavaModel包含所有键
public class SimpleModelGenerator extends AbstractJavaGenerator {
	
    public SimpleModelGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString())); 
        Plugin plugins = context.getPlugins();
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        //获取父类类型
        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
        
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }

            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method);
            }
            
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelSetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(method);
            }
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    //获取父类
    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        String rootClass = getRootClass();
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }
        return superClass;
    }
    
}
