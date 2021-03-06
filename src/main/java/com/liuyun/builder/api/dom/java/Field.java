package com.liuyun.builder.api.dom.java;

import com.liuyun.builder.api.dom.OutputUtil;

public class Field extends JavaElement {
	
    private FullyQualifiedJavaType type;     //字段类型
    private String name;                     //字段名
    private String initializationString;     //初始化字符串
    private boolean isTransient;            //是否是transient
    private boolean isVolatile;             //是否是volatile

    public Field() {
        this("foo", FullyQualifiedJavaType.getIntInstance()); 
    }

    public Field(String name, FullyQualifiedJavaType type) {
        super();
        this.name = name;
        this.type = type;
    }

    public Field(Field field) {
        super(field);
        this.type = field.type;
        this.name = field.name;
        this.initializationString = field.initializationString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public String getInitializationString() {
        return initializationString;
    }

    public void setInitializationString(String initializationString) {
        this.initializationString = initializationString;
    }

    //获取格式化内容
    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        OutputUtil.javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isStatic()) {
            sb.append("static "); 
        }

        if (isFinal()) {
            sb.append("final "); 
        }

        if (isTransient()) {
            sb.append("transient "); 
        }

        if (isVolatile()) {
            sb.append("volatile "); 
        }

        sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));

        sb.append(' ');
        sb.append(name);

        if (initializationString != null && initializationString.length() > 0) {
            sb.append(" = "); 
            sb.append(initializationString);
        }

        sb.append(';');

        return sb.toString();
    }

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public boolean isVolatile() {
        return isVolatile;
    }

    public void setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
    }
}
