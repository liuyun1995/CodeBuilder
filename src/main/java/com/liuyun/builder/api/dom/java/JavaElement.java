package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.dom.OutputUtil;

//Java文件元素
public abstract class JavaElement {

    /** The java doc lines. */
    private List<String> javaDocLines;

    /** The visibility. */
    private JavaVisibility visibility = JavaVisibility.DEFAULT;

    /** The is static. */
    private boolean isStatic;

    /** The is final. */
    private boolean isFinal;

    /** The annotations. */
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

    /**
     * Gets the java doc lines.
     *
     * @return Returns the javaDocLines.
     */
    public List<String> getJavaDocLines() {
        return javaDocLines;
    }

    /**
     * Adds the java doc line.
     *
     * @param javaDocLine
     *            the java doc line
     */
    public void addJavaDocLine(String javaDocLine) {
        javaDocLines.add(javaDocLine);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }
    
    public JavaVisibility getVisibility() {
        return visibility;
    }
    
    public void setVisibility(JavaVisibility visibility) {
        this.visibility = visibility;
    }
    
    
    public void addSuppressTypeWarningsAnnotation() {
        addAnnotation("@SuppressWarnings(\"unchecked\")"); 
    }
    
    
    public void addFormattedJavadoc(StringBuilder sb, int indentLevel) {
        for (String javaDocLine : javaDocLines) {
            OutputUtil.javaIndent(sb, indentLevel);
            sb.append(javaDocLine);
            OutputUtil.newLine(sb);
        }
    }
    
    
    public void addFormattedAnnotations(StringBuilder sb, int indentLevel) {
        for (String annotation : annotations) {
            OutputUtil.javaIndent(sb, indentLevel);
            sb.append(annotation);
            OutputUtil.newLine(sb);
        }
    }
    
    
    public boolean isFinal() {
        return isFinal;
    }
    
    
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    
    public boolean isStatic() {
        return isStatic;
    }
    
    
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
}
