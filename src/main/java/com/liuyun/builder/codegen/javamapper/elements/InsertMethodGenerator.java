package com.liuyun.builder.codegen.javamapper.elements;

import java.util.Set;
import java.util.TreeSet;

import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.JavaVisibility;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.Parameter;

public class InsertMethodGenerator extends AbstractJavaMapperMethodGenerator {

    private boolean isSimple;

    public InsertMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());  //设置返回类型
        method.setVisibility(JavaVisibility.PUBLIC);                    //设置访问标志
        method.setName(introspectedTable.getInsertStatementId());       //设置方法名称

        FullyQualifiedJavaType parameterType;
        if (isSimple) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        } else {
            parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        }

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(parameterType);                              //添加导入类型
        method.addParameter(new Parameter(parameterType, "record"));   //设置方法参数

        //添加注释
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        //添加注解
        addMapperAnnotations(method);
        if (context.getPlugins().clientInsertMethodGenerated(method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Method method) {}

    public void addExtraImports(Interface interfaze) {}
}
