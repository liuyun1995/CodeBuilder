package com.liuyun.builder.codegen.javamapper.elements.annotated;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static org.mybatis.generator.codegen.util.FormatUtil.getEscapedColumnName;
import static org.mybatis.generator.codegen.util.FormatUtil.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtil.escapeStringForJava;

import java.util.Iterator;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.util.ListUtil;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.codegen.javamapper.elements.UpdateMethodGenerator;

public class AnnotatedUpdateMethodGenerator
        extends UpdateMethodGenerator {

    private boolean isSimple;

    public AnnotatedUpdateMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addMapperAnnotations(Method method) {

        method.addAnnotation("@Update({"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"update "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        // set up for first column
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"set "); //$NON-NLS-1$

        Iterator<IntrospectedColumn> iter;
        if (isSimple) {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns())
                   .iterator();
        } else {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getBaseColumns())
                   .iterator();
        }

        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            sb.append("\","); //$NON-NLS-1$
            method.addAnnotation(sb.toString());

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append("  \""); //$NON-NLS-1$
            }
        }

        boolean and = false;
        iter = introspectedTable.getPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            sb.setLength(0);
            javaIndent(sb, 1);
            if (and) {
                sb.append("  \"and "); //$NON-NLS-1$
            } else {
                sb.append("\"where "); //$NON-NLS-1$
                and = true;
            }

            IntrospectedColumn introspectedColumn = iter.next();
            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(getParameterClause(introspectedColumn));
            sb.append('\"');
            if (iter.hasNext()) {
                sb.append(',');
            }
            method.addAnnotation(sb.toString());
        }

        method.addAnnotation("})"); //$NON-NLS-1$
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Update")); //$NON-NLS-1$
    }
}
