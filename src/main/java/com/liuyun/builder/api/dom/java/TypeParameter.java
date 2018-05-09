package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.List;

//类型参数
public class TypeParameter {
    private String name;                                  //类型参数名
    private List<FullyQualifiedJavaType> extendsTypes;    //继承类型集合

    public TypeParameter(String name) {
        this(name, new ArrayList<FullyQualifiedJavaType>());
    }

    public TypeParameter(String name, List<FullyQualifiedJavaType> extendsTypes) {
        super();
        this.name = name;
        this.extendsTypes = extendsTypes;
    }
    
    public String getName() {
        return name;
    }
    
    public List<FullyQualifiedJavaType> getExtendsTypes() {
        return extendsTypes;
    }

    public String getFormattedContent(CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (!extendsTypes.isEmpty()) {
            sb.append(" extends "); 
            boolean addAnd = false;
            for (FullyQualifiedJavaType type : extendsTypes) {
                if (addAnd) {
                    sb.append(" & "); 
                } else {
                    addAnd = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedContent(null);
    }
}
