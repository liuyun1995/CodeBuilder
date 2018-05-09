package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.dom.OutputUtil;

//Java文件元素
public abstract class JavaElement {
	
    private List<String> javaDocLines;
    
    private JavaVisibility visibility = JavaVisibility.DEFAULT;
    
    private boolean isStatic;
    
    private boolean isFinal;
    
    private List<String> annotations;
    
    public JavaElement() {
        super();
        javaDocLines = new ArrayList<String>();
        annotations = new ArrayList<String>();
    }

    public JavaElement(JavaElement original) {
        this();
        this.annotations.addAll(original.annotations);
        this.isFinal = original.isFinal;
        this.isStatic = original.isStatic;
        this.javaDocLines.addAll(original.javaDocLines);
        this.visibility = original.visibility;
    }

    //获取JavaDoc行
    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    //添加JavaDoc行
    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    //获取注解
    public List<String> getAnnotations() {
        return annotations;
    }

    //添加注解
    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
    
    //获取访问标志
    public JavaVisibility getVisibility() {
        return visibility;
    }
    
    //设置访问标志
    public void setVisibility(JavaVisibility visibility) {
        this.visibility = visibility;
    }
    
    //添加SuppressWarnings注解
    public void addSuppressTypeWarningsAnnotation() {
        addAnnotation("@SuppressWarnings(\"unchecked\")"); 
    }
    
    //添加格式化JavaDoc
    public void addFormattedJavadoc(StringBuilder sb, int indentLevel) {
        for (String javaDocLine : javaDocLines) {
            OutputUtil.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtil.newLine(sb);
        }
    }
    
    //添加格式化注释
    public void addFormattedAnnotations(StringBuilder sb, int indentLevel) {
        for (String annotation : annotations) {
            OutputUtil.javaIndent(sb, indentLevel);
            sb.append(annotation);
            OutputUtil.newLine(sb);
        }
    }
    
    //是否是final类型
    public boolean isFinal() {
        return isFinal;
    }
    
    //设置为final类型
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    //是否是static类型
    public boolean isStatic() {
        return isStatic;
    }
    
    //设置为static类型
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
}
