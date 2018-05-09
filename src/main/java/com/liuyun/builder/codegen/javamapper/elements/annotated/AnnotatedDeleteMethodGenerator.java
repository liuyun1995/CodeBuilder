package com.liuyun.builder.codegen.javamapper.elements.annotated;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static org.mybatis.generator.codegen.util.FormatUtil.getEscapedColumnName;
import static org.mybatis.generator.codegen.util.FormatUtil.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtil.escapeStringForJava;

import java.util.Iterator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.codegen.javamapper.elements.DeleteMethodGenerator;

public class AnnotatedDeleteMethodGenerator extends
        DeleteMethodGenerator {

    public AnnotatedDeleteMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Method method) {
        method.addAnnotation("@Delete({"); 
        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"delete from "); 
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); 
        method.addAnnotation(sb.toString());
        boolean and = false;
        Iterator<IntrospectedColumn> iter = introspectedTable.getPrimaryKeyColumns().iterator();
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
            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); 
            sb.append(getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }
        method.addAnnotation("})"); 
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Delete")); 
    }
}
