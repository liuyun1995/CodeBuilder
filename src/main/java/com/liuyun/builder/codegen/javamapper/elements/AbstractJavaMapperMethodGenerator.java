package com.liuyun.builder.codegen.javamapper.elements;

import static com.liuyun.builder.codegen.util.FormatUtil.getRenamedColumnNameForResultMap;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.codegen.core.AbstractGenerator;
import com.liuyun.builder.config.GeneratedKey;

//抽象的JavaMapper生成器
public abstract class AbstractJavaMapperMethodGenerator extends AbstractGenerator {
	
	//添加接口元素
    public abstract void addInterfaceElements(Interface interfaze);

    public AbstractJavaMapperMethodGenerator() {
        super();
    }
    
    protected String getResultAnnotation(Interface interfaze, IntrospectedColumn introspectedColumn,
            boolean idColumn, boolean constructorBased) {
        StringBuilder sb = new StringBuilder();
        if (constructorBased) {
            interfaze.addImportedType(introspectedColumn.getFullyQualifiedJavaType());
            sb.append("@Arg(column=\""); 
            sb.append(getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", javaType="); 
            sb.append(introspectedColumn.getFullyQualifiedJavaType().getShortName());
            sb.append(".class"); 
        } else {
            sb.append("@Result(column=\""); 
            sb.append(getRenamedColumnNameForResultMap(introspectedColumn));
            sb.append("\", property=\""); 
            sb.append(introspectedColumn.getJavaProperty());
            sb.append('\"');
        }

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedColumn.getTypeHandler());
            interfaze.addImportedType(fqjt);
            sb.append(", typeHandler="); 
            sb.append(fqjt.getShortName());
            sb.append(".class"); 
        }

        sb.append(", jdbcType=JdbcType."); 
        sb.append(introspectedColumn.getJdbcTypeName());
        if (idColumn) {
            sb.append(", id=true"); 
        }
        sb.append(')');

        return sb.toString();
    }

    protected void addGeneratedKeyAnnotation(Method method, GeneratedKey gk) {
        StringBuilder sb = new StringBuilder();
        IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
        if (introspectedColumn != null) {
            if (gk.isJdbcStandard()) {
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\""); 
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")"); 
                method.addAnnotation(sb.toString());
            } else {
                FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                sb.append("@SelectKey(statement=\""); 
                sb.append(gk.getRuntimeSqlStatement());
                sb.append("\", keyProperty=\""); 
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\", before="); 
                sb.append(gk.isIdentity() ? "false" : "true");
                sb.append(", resultType="); 
                sb.append(fqjt.getShortName());
                sb.append(".class)"); 
                method.addAnnotation(sb.toString());
            }
        }
    }

    protected void addGeneratedKeyImports(Interface interfaze, GeneratedKey gk) {
        IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
        if (introspectedColumn != null) {
            if (gk.isJdbcStandard()) {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options")); 
            } else {
                interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectKey")); 
                FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
                interfaze.addImportedType(fqjt);
            }
        }
    }
}
