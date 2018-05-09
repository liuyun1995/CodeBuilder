package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.List;

public class Parameter {
    private String name;                   //参数名
    private FullyQualifiedJavaType type;   //参数类型
    private boolean isVarargs;            //是否是可变参数
    private List<String> annotations;      //注释集合

    public Parameter(FullyQualifiedJavaType type, String name, boolean isVarargs) {
        super();
        this.name = name;
        this.type = type;
        this.isVarargs = isVarargs;
        annotations = new ArrayList<String>();
    }

    public Parameter(FullyQualifiedJavaType type, String name) {
        this(type, name, false);
    }

    public Parameter(FullyQualifiedJavaType type, String name, String annotation) {
        this(type, name, false);
        addAnnotation(annotation);
    }

    public Parameter(FullyQualifiedJavaType type, String name, String annotation, boolean isVarargs) {
        this(type, name, isVarargs);
        addAnnotation(annotation);
    }

    public String getName() {
        return name;
    }
    
    public FullyQualifiedJavaType getType() {
        return type;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    public String getFormattedContent(CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();
        for (String annotation : annotations) {
            sb.append(annotation);
            sb.append(' ');
        }
        sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));
        sb.append(' ');
        if (isVarargs) {
            sb.append("... "); 
        }
        sb.append(name);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedContent(null);
    }

    public boolean isVarargs() {
        return isVarargs;
    }
}
