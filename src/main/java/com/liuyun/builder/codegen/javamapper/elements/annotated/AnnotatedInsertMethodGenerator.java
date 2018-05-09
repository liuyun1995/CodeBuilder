package com.liuyun.builder.codegen.javamapper.elements.annotated;

import static com.liuyun.builder.api.dom.OutputUtil.javaIndent;
import static com.liuyun.builder.codegen.util.FormatUtil.getEscapedColumnName;
import static com.liuyun.builder.codegen.util.FormatUtil.getParameterClause;
import static com.liuyun.builder.internal.utils.StringUtil.escapeStringForJava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.codegen.javamapper.elements.InsertMethodGenerator;
import com.liuyun.builder.codegen.util.ListUtil;
import com.liuyun.builder.config.GeneratedKey;

public class AnnotatedInsertMethodGenerator extends InsertMethodGenerator {

    public AnnotatedInsertMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Method method) {
        method.addAnnotation("@Insert({"); 
        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();
        javaIndent(insertClause, 1);
        javaIndent(valuesClause, 1);
        insertClause.append("\"insert into "); 
        insertClause.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        insertClause.append(" ("); 
        valuesClause.append("\"values ("); 
        List<String> valuesClauses = new ArrayList<String>();
        Iterator<IntrospectedColumn> iter = ListUtil.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns()).iterator();
        boolean hasFields = false;
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            insertClause.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            valuesClause.append(getParameterClause(introspectedColumn));
            hasFields = true;
            if (iter.hasNext()) {
                insertClause.append(", "); 
                valuesClause.append(", "); 
            }
            if (valuesClause.length() > 60) {
                if (!iter.hasNext()) {
                    insertClause.append(')');
                    valuesClause.append(')');
                }
                insertClause.append("\","); 
                valuesClause.append('\"');
                if (iter.hasNext()) {
                    valuesClause.append(',');
                }
                method.addAnnotation(insertClause.toString());
                insertClause.setLength(0);
                javaIndent(insertClause, 1);
                insertClause.append('\"');
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                javaIndent(valuesClause, 1);
                valuesClause.append('\"');
                hasFields = false;
            }
        }
        if (hasFields) {
            insertClause.append(")\","); 
            method.addAnnotation(insertClause.toString());
            valuesClause.append(")\""); 
            valuesClauses.add(valuesClause.toString());
        }
        for (String clause : valuesClauses) {
            method.addAnnotation(clause);
        }
        method.addAnnotation("})"); 
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            addGeneratedKeyAnnotation(method, gk);
        }
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            addGeneratedKeyImports(interfaze, gk);
        }
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert")); 
    }
    
}
