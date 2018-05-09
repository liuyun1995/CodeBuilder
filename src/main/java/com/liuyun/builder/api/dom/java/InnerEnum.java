package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.liuyun.builder.api.dom.OutputUtil;

//内部枚举类
public class InnerEnum extends JavaElement {

    //字段集合
    private List<Field> fields;

    //内部类集合
    private List<InnerClass> innerClasses;

    //内部接口集合
    private List<InnerEnum> innerEnums;

    //全限定类型
    private FullyQualifiedJavaType type;

    //实现的接口集合
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    //方法集合
    private List<Method> methods;

    //枚举常量
    private List<String> enumConstants;
    
    public InnerEnum(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        fields = new ArrayList<Field>();
        innerClasses = new ArrayList<InnerClass>();
        innerEnums = new ArrayList<InnerEnum>();
        superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        enumConstants = new ArrayList<String>();
    }
    
    public List<Field> getFields() {
        return fields;
    }
    
    public void addField(Field field) {
        fields.add(field);
    }
    
    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }
    
    public void addInnerClass(InnerClass innerClass) {
        innerClasses.add(innerClass);
    }
    
    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }
    
    public void addInnerEnum(InnerEnum innerEnum) {
        innerEnums.add(innerEnum);
    }
    
    public List<String> getEnumConstants() {
        return enumConstants;
    }
    
    public void addEnumConstant(String enumConstant) {
        enumConstants.add(enumConstant);
    }
    
    //获取格式化内容
    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();
        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);
        OutputUtil.javaIndent(sb, indentLevel);
        if (getVisibility() == JavaVisibility.PUBLIC) {
            sb.append(getVisibility().getValue());
        }
        sb.append("enum "); 
        sb.append(getType().getShortName());
        if (superInterfaceTypes.size() > 0) {
            sb.append(" implements "); 
            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : superInterfaceTypes) {
                if (comma) {
                    sb.append(", "); 
                } else {
                    comma = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }
        sb.append(" {"); 
        indentLevel++;
        Iterator<String> strIter = enumConstants.iterator();
        while (strIter.hasNext()) {
            OutputUtil.newLine(sb);
            OutputUtil.javaIndent(sb, indentLevel);
            String enumConstant = strIter.next();
            sb.append(enumConstant);
            if (strIter.hasNext()) {
                sb.append(',');
            } else {
                sb.append(';');
            }
        }
        if (fields.size() > 0) {
            OutputUtil.newLine(sb);
        }
        Iterator<Field> fldIter = fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtil.newLine(sb);
            Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
            if (fldIter.hasNext()) {
                OutputUtil.newLine(sb);
            }
        }
        if (methods.size() > 0) {
            OutputUtil.newLine(sb);
        }
        Iterator<Method> mtdIter = methods.iterator();
        while (mtdIter.hasNext()) {
            OutputUtil.newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, false, compilationUnit));
            if (mtdIter.hasNext()) {
                OutputUtil.newLine(sb);
            }
        }
        if (innerClasses.size() > 0) {
            OutputUtil.newLine(sb);
        }
        Iterator<InnerClass> icIter = innerClasses.iterator();
        while (icIter.hasNext()) {
            OutputUtil.newLine(sb);
            InnerClass innerClass = icIter.next();
            sb.append(innerClass.getFormattedContent(indentLevel, compilationUnit));
            if (icIter.hasNext()) {
                OutputUtil.newLine(sb);
            }
        }
        if (innerEnums.size() > 0) {
            OutputUtil.newLine(sb);
        }
        Iterator<InnerEnum> ieIter = innerEnums.iterator();
        while (ieIter.hasNext()) {
            OutputUtil.newLine(sb);
            InnerEnum innerEnum = ieIter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel, compilationUnit));
            if (ieIter.hasNext()) {
                OutputUtil.newLine(sb);
            }
        }
        indentLevel--;
        OutputUtil.newLine(sb);
        OutputUtil.javaIndent(sb, indentLevel);
        sb.append('}');
        return sb.toString();
    }
    
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }
    
    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }
    
    public List<Method> getMethods() {
        return methods;
    }
    
    public void addMethod(Method method) {
        methods.add(method);
    }
    
    public FullyQualifiedJavaType getType() {
        return type;
    }
    
}
