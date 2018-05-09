package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.liuyun.builder.api.dom.OutputUtil;

//方法
public class Method extends JavaElement {

    //方法体
    private List<String> bodyLines;

    //是否是构造器
    private boolean constructor;

    //返回类型
    private FullyQualifiedJavaType returnType;

    //方法名
    private String name;

    //参数类型列表
    private List<TypeParameter> typeParameters;

    //参数列表
    private List<Parameter> parameters;

    //声明异常列表
    private List<FullyQualifiedJavaType> exceptions;

    private boolean isSynchronized;

    private boolean isNative;

    private boolean isDefault;

    public Method() {
        this("bar"); 
    }
    
    public Method(String name) {
        super();
        bodyLines = new ArrayList<String>();
        typeParameters = new ArrayList<TypeParameter>();
        parameters = new ArrayList<Parameter>();
        exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.name = name;
    }
    
    public Method(Method original) {
        super(original);
        bodyLines = new ArrayList<String>();
        typeParameters = new ArrayList<TypeParameter>();
        parameters = new ArrayList<Parameter>();
        exceptions = new ArrayList<FullyQualifiedJavaType>();
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.typeParameters.addAll(original.typeParameters);
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
        this.isDefault = original.isDefault;
    }
    
    public List<String> getBodyLines() {
        return bodyLines;
    }
    
    public void addBodyLine(String line) {
        bodyLines.add(line);
    }
    
    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }
    
    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }
    
    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }
    
    //获取格式化内容
    public String getFormattedContent(int indentLevel, boolean interfaceMethod, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();
        //添加javadoc
        addFormattedJavadoc(sb, indentLevel);
        //添加注解
        addFormattedAnnotations(sb, indentLevel);

        OutputUtil.javaIndent(sb, indentLevel);

        if (interfaceMethod) {
            if (isStatic()) {
                sb.append("static "); 
            } else if (isDefault()) {
                sb.append("default "); 
            }
        } else {
            sb.append(getVisibility().getValue());

            if (isStatic()) {
                sb.append("static "); 
            }

            if (isFinal()) {
                sb.append("final "); 
            }

            if (isSynchronized()) {
                sb.append("synchronized "); 
            }

            if (isNative()) {
                sb.append("native "); 
            } else if (bodyLines.size() == 0) {
                sb.append("abstract "); 
            }
        }

        if (!getTypeParameters().isEmpty()) {
            sb.append("<"); 
            boolean comma = false;
            for (TypeParameter typeParameter : getTypeParameters()) {
                if (comma) {
                    sb.append(", "); 
                } else {
                    comma = true;
                }

                sb.append(typeParameter.getFormattedContent(compilationUnit));
            }
            sb.append("> "); 
        }

        if (!constructor) {
            if (getReturnType() == null) {
                sb.append("void"); 
            } else {
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, getReturnType()));
            }
            sb.append(' ');
        }

        sb.append(getName());
        sb.append('(');

        boolean comma = false;
        for (Parameter parameter : getParameters()) {
            if (comma) {
                sb.append(", "); 
            } else {
                comma = true;
            }

            sb.append(parameter.getFormattedContent(compilationUnit));
        }

        sb.append(')');

        if (getExceptions().size() > 0) {
            sb.append(" throws "); 
            comma = false;
            for (FullyQualifiedJavaType fqjt : getExceptions()) {
                if (comma) {
                    sb.append(", "); 
                } else {
                    comma = true;
                }

                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt));
            }
        }

        // if no body lines, then this is an abstract method
        if (bodyLines.size() == 0 || isNative()) {
            sb.append(';');
        } else {
            sb.append(" {"); 
            indentLevel++;

            ListIterator<String> listIter = bodyLines.listIterator();
            while (listIter.hasNext()) {
                String line = listIter.next();
                if (line.startsWith("}")) { 
                    indentLevel--;
                }

                OutputUtil.newLine(sb);
                OutputUtil.javaIndent(sb, indentLevel);
                sb.append(line);

                if ((line.endsWith("{") && !line.startsWith("switch"))  //$NON-NLS-2$
                        || line.endsWith(":")) { 
                    indentLevel++;
                }

                if (line.startsWith("break")) { 
                    // if the next line is '}', then don't outdent
                    if (listIter.hasNext()) {
                        String nextLine = listIter.next();
                        if (nextLine.startsWith("}")) { 
                            indentLevel++;
                        }

                        // set back to the previous element
                        listIter.previous();
                    }
                    indentLevel--;
                }
            }

            indentLevel--;
            OutputUtil.newLine(sb);
            OutputUtil.javaIndent(sb, indentLevel);
            sb.append('}');
        }

        return sb.toString();
    }
    
    
    public boolean isConstructor() {
        return constructor;
    }
    
    
    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }
    
    
    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }
    
    
    public void addTypeParameter(int index, TypeParameter typeParameter) {
        typeParameters.add(index, typeParameter);
    }
    
    
    public List<Parameter> getParameters() {
        return parameters;
    }
    
    
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }
    
    
    public void addParameter(int index, Parameter parameter) {
        parameters.add(index, parameter);
    }
    
    
    public FullyQualifiedJavaType getReturnType() {
        return returnType;
    }
    
    
    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }
    
    
    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }
    
    
    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }
    
    
    public boolean isSynchronized() {
        return isSynchronized;
    }
    
    
    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }
    
    
    public boolean isNative() {
        return isNative;
    }
    
    
    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
}
