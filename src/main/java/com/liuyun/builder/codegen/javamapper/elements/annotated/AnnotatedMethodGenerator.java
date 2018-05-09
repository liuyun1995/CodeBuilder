package com.liuyun.builder.codegen.javamapper.elements.annotated;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static com.liuyun.builder.codegen.util.FormatUtil.getAliasedEscapedColumnName;
import static com.liuyun.builder.codegen.util.FormatUtil.getParameterClause;
import static com.liuyun.builder.codegen.util.FormatUtil.getSelectListPhrase;
import static com.liuyun.builder.internal.utils.StringUtil.escapeStringForJava;

import java.util.Iterator;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.codegen.javamapper.elements.SelectMethodGenerator;

public class AnnotatedMethodGenerator extends SelectMethodGenerator {
    
    private boolean useResultMapIfAvailable;

    public AnnotatedMethodGenerator(boolean useResultMapIfAvailable, boolean isSimple) {
        super(isSimple);
        this.useResultMapIfAvailable = useResultMapIfAvailable;
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        StringBuilder sb = new StringBuilder();
        method.addAnnotation("@Select({"); 
        javaIndent(sb, 1);
        sb.append("\"select\","); 
        method.addAnnotation(sb.toString());
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append('"');
        boolean hasColumns = false;
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            sb.append(escapeStringForJava(getSelectListPhrase(iter.next())));
            hasColumns = true;
            if (iter.hasNext()) {
                sb.append(", "); 
            }
            if (sb.length() > 80) {
                sb.append("\","); 
                method.addAnnotation(sb.toString());

                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append('"');
                hasColumns = false;
            }
        }
        if (hasColumns) {
            sb.append("\","); 
            method.addAnnotation(sb.toString());
        }
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"from "); 
        sb.append(escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); 
        method.addAnnotation(sb.toString());
        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            sb.setLength(0);
            javaIndent(sb, 1);
            if (and) {
                sb.append("  \"and "); 
            } else {
                sb.append("\"where "); 
                and = true;
            }
            IntrospectedColumn introspectedColumn = iter.next();
            sb.append(escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)));
            sb.append(" = "); 
            sb.append(getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})"); 
        if (useResultMapIfAvailable) {
            if (introspectedTable.getRules().generateResultMap()) {
                addResultMapAnnotation(method);
            } else {
                addAnnotatedResults(interfaze, method);
            }
        } else {
            addAnnotatedResults(interfaze, method);
        }
    }

    private void addResultMapAnnotation(Method method) {
        String annotation = String.format("@ResultMap(\"%s.%s\")", 
                introspectedTable.getSqlMapNamespace(),
                introspectedTable.getRules().generateResultMap() ? introspectedTable.getBaseResultMapId() : null);
        method.addAnnotation(annotation);
    }

    private void addAnnotatedResults(Interface interfaze, Method method) {
        if (introspectedTable.isConstructorBased()) {
            method.addAnnotation("@ConstructorArgs({"); 
        } else {
            method.addAnnotation("@Results({"); 
        }
        StringBuilder sb = new StringBuilder();

        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, true, introspectedTable.isConstructorBased()));
            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, false, introspectedTable.isConstructorBased()));
            if (iterNonPk.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})"); 
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); 
        if (useResultMapIfAvailable) {
            if (introspectedTable.getRules().generateResultMap()) {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap")); 
            } else {
                addAnnotationImports(interfaze);
            }
        } else {
            addAnnotationImports(interfaze);
        }
    }

    private void addAnnotationImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); 
        if (introspectedTable.isConstructorBased()) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); 
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); 
        } else {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); 
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); 
        }
    }
    
}
