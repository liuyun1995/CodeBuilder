package com.liuyun.builder.codegen.javamapper.elements;

import java.util.Set;
import java.util.TreeSet;

import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.JavaVisibility;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.Parameter;

public class UpdateMethodGenerator extends AbstractJavaMapperMethodGenerator {

    public UpdateMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getUpdateStatementId());
        method.addParameter(new Parameter(parameterType, "record")); 

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        addMapperAnnotations(method);
        if (context.getPlugins().clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Method method) {}

    public void addExtraImports(Interface interfaze) {}
    
}
