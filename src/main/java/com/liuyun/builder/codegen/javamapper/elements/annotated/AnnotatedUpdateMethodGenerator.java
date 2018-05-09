package com.liuyun.builder.codegen.javamapper.elements.annotated;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static com.liuyun.builder.codegen.util.FormatUtil.getEscapedColumnName;
import static com.liuyun.builder.codegen.util.FormatUtil.getParameterClause;
import static com.liuyun.builder.internal.utils.StringUtil.escapeStringForJava;
import java.util.Iterator;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.codegen.javamapper.elements.UpdateMethodGenerator;
import com.liuyun.builder.codegen.util.ListUtil;

public class AnnotatedUpdateMethodGenerator extends UpdateMethodGenerator {

    private boolean isSimple;

    public AnnotatedUpdateMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addMapperAnnotations(Method method) {
        method.addAnnotation("@Update({"); 
        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"update "); 
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); 
        method.addAnnotation(sb.toString());
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"set "); 
        Iterator<IntrospectedColumn> iter;
        if (isSimple) {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns()).iterator();
        } else {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getBaseColumns()).iterator();
        }

        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            sb.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            sb.append(" = "); 
            sb.append(getParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(',');
            }
            sb.append("\","); 
            method.addAnnotation(sb.toString());
            if (iter.hasNext()) {
                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append("  \""); 
            }
        }

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
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Update")); 
    }
    
}
