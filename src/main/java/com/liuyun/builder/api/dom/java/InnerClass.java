package com.liuyun.builder.api.dom.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.liuyun.builder.api.dom.OutputUtil;

//内部类
public class InnerClass extends JavaElement {

    //字段集合
    private List<Field> fields;

    //内部类集合
    private List<InnerClass> innerClasses;

    //内部枚举集合
    private List<InnerEnum> innerEnums;

    //类型参数集合
    private List<TypeParameter> typeParameters;

    //父类类型
    private FullyQualifiedJavaType superClass;

    //本类类型
    private FullyQualifiedJavaType type;

    //接口集合
    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    //方法集合
    private List<Method> methods;

    //是否抽象类
    private boolean isAbstract;

    //初始代码块集合
    private List<InitializationBlock> initializationBlocks;

    public InnerClass(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        fields = new ArrayList<Field>();
        innerClasses = new ArrayList<InnerClass>();
        innerEnums = new ArrayList<InnerEnum>();
        this.typeParameters = new ArrayList<TypeParameter>();
        superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        initializationBlocks = new ArrayList<InitializationBlock>();
    }
    
    public InnerClass(String typeName) {
        this(new FullyQualifiedJavaType(typeName));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }
    
    public FullyQualifiedJavaType getSuperClass() {
        return superClass;
    }
    
    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }
    
    public void setSuperClass(String superClassType) {
        this.superClass = new FullyQualifiedJavaType(superClassType);
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
    
    public List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }
    
    public void addTypeParameter(TypeParameter typeParameter) {
        this.typeParameters.add(typeParameter);
    }
    
    public List<InitializationBlock> getInitializationBlocks() {
        return initializationBlocks;
    }
    
    public void addInitializationBlock(InitializationBlock initializationBlock) {
        initializationBlocks.add(initializationBlock);
    }
    
    //获取格式化内容
    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        OutputUtil.javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isAbstract()) {
            sb.append("abstract "); 
        }

        if (isStatic()) {
            sb.append("static "); 
        }

        if (isFinal()) {
            sb.append("final "); 
        }

        sb.append("class "); 
        sb.append(getType().getShortName());

        if (!this.getTypeParameters().isEmpty()) {
            boolean comma = false;
            sb.append("<"); 
            for (TypeParameter typeParameter : typeParameters) {
                if (comma) {
                    sb.append(", "); 
                }
                sb.append(typeParameter.getFormattedContent(compilationUnit));
                comma = true;
            }
            sb.append("> "); 
        }

        if (superClass != null) {
            sb.append(" extends "); 
            sb.append(JavaDomUtils.calculateTypeName(compilationUnit, superClass));
        }

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

        Iterator<Field> fldIter = fields.iterator();
        while (fldIter.hasNext()) {
            OutputUtil.newLine(sb);
            Field field = fldIter.next();
            sb.append(field.getFormattedContent(indentLevel, compilationUnit));
            if (fldIter.hasNext()) {
                OutputUtil.newLine(sb);
            }
        }

        if (initializationBlocks.size() > 0) {
            OutputUtil.newLine(sb);
        }

        Iterator<InitializationBlock> blkIter = initializationBlocks.iterator();
        while (blkIter.hasNext()) {
            OutputUtil.newLine(sb);
            InitializationBlock initializationBlock = blkIter.next();
            sb.append(initializationBlock.getFormattedContent(indentLevel));
            if (blkIter.hasNext()) {
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
    
    public boolean isAbstract() {
        return isAbstract;
    }
    
    public void setAbstract(boolean isAbtract) {
        this.isAbstract = isAbtract;
    }
}
