package com.liuyun.builder.api;

import java.util.Properties;
import java.util.Set;

import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.api.dom.java.Field;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.InnerClass;
import com.liuyun.builder.api.dom.java.InnerEnum;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.TopLevelClass;
import com.liuyun.builder.api.dom.xml.XmlElement;

public interface CommentGenerator {

	void addConfigurationProperties(Properties properties);

	void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn);

	void addFieldComment(Field field, IntrospectedTable introspectedTable);

	void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

	void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable);

	void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete);

	void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable);

	void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn);

	void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn);

	void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable);

	void addJavaFileComment(CompilationUnit compilationUnit);

	void addComment(XmlElement xmlElement);

	void addRootComment(XmlElement rootElement);

	void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports);

	void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports);

	void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports);

	void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn,
			Set<FullyQualifiedJavaType> imports);

	void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports);
	
}
