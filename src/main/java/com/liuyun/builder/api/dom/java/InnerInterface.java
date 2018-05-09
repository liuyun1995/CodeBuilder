package com.liuyun.builder.api.dom.java;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static com.liuyun.builder.api.dom.OutputUtil.newLine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.liuyun.builder.api.dom.OutputUtil;

//内部接口
public class InnerInterface extends JavaElement {

	//字段集合
    private List<Field> fields;

    //全限定类型
    private FullyQualifiedJavaType type;

    //内部接口集合
    private List<InnerInterface> innerInterfaces;

    //父接口集合
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    //方法列表
    private List<Method> methods;

    public InnerInterface(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        innerInterfaces = new ArrayList<InnerInterface>();
        superInterfaceTypes = new LinkedHashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        fields = new ArrayList<Field>();
    }

    public InnerInterface(String type) {
        this(new FullyQualifiedJavaType(type));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }
    
    //获取格式化内容
    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isStatic()) {
            sb.append("static "); 
        }

        if (isFinal()) {
            sb.append("final "); 
        }

        sb.append("interface "); 
        sb.append(getType().getShortName());

        if (getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends "); 

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : getSuperInterfaceTypes()) {
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

        Iterator<Field> fldIter = fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtil.newLine(sb);
            Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
        }

        if (fields.size() > 0 && methods.size() > 0) {
            OutputUtil.newLine(sb);
        }

        Iterator<Method> mtdIter = getMethods().iterator();
        while (mtdIter.hasNext()) {
            newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, true, compilationUnit));
            if (mtdIter.hasNext()) {
                newLine(sb);
            }
        }

        if (innerInterfaces.size() > 0) {
            newLine(sb);
        }
        Iterator<InnerInterface> iiIter = innerInterfaces.iterator();
        while (iiIter.hasNext()) {
            newLine(sb);
            InnerInterface innerInterface = iiIter.next();
            sb.append(innerInterface.getFormattedContent(indentLevel, compilationUnit));
            if (iiIter.hasNext()) {
                newLine(sb);
            }
        }

        indentLevel--;
        newLine(sb);
        javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
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
    
    
    public FullyQualifiedJavaType getSuperClass() {
        return null;
    }
    
    
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }
    
    
    public List<InnerInterface> getInnerInterfaces() {
        return innerInterfaces;
    }
    
    
    public void addInnerInterfaces(InnerInterface innerInterface) {
        innerInterfaces.add(innerInterface);
    }
    
    
    public boolean isJavaInterface() {
        return true;
    }
    
    
    public boolean isJavaEnumeration() {
        return false;
    }
    
}
