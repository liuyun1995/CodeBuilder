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

//生成JavaModel只包含主键
public class PrimaryKeyGenerator extends AbstractJavaGenerator {

    public PrimaryKeyGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.7", table.toString())); 
        Plugin plugins = context.getPlugins();
        CommentGenerator commentGenerator = context.getCommentGenerator();

        TopLevelClass topLevelClass = new TopLevelClass(introspectedTable.getPrimaryKeyType());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        String rootClass = getRootClass();
        if (rootClass != null) {
            topLevelClass.setSuperClass(new FullyQualifiedJavaType(rootClass));
            topLevelClass.addImportedType(topLevelClass.getSuperClass());
        }
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);

        //遍历主键集合
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }

            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }

            Method method = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelGetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addMethod(method);
            }
            
            method = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
            if (plugins.modelSetterMethodGenerated(method, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.PRIMARY_KEY)) {
                topLevelClass.addMethod(method);
            }
            
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }
    
}
