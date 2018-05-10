package com.liuyun.builder.api;

import java.sql.Types;
import java.util.Properties;

import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.internal.utils.StringUtil;

//逆向列
public class IntrospectedColumn {
	
    protected String actualColumnName;                         //列名
    protected int jdbcType;                                   //jdbc类型
    protected String jdbcTypeName;                             //jdbc类型名
    protected boolean nullable;                               //是否可为空
    protected int length;                                     //列的长度
    protected int scale;                                      //列的精度
    protected boolean identity;                               //是否是主键
    protected boolean isSequenceColumn;                       //是否是自增列
    protected String javaProperty;                             //java属性名
    protected FullyQualifiedJavaType fullyQualifiedJavaType;   //全限定属性名
    protected String tableAlias;                               //类型别名
    protected String typeHandler;                              //类型处理器
    protected Context context;                                 //配置上下文
    protected boolean isColumnNameDelimited;                  //列名是否区分大小写
    protected IntrospectedTable introspectedTable;             //逆向表
    protected Properties properties;                           //属性
    protected String remarks;                                  //备注
    protected String defaultValue;                             //默认值
    protected boolean isAutoIncrement;                        //是否自动递增
    protected boolean isGeneratedColumn;                      //是否已经生成的列
    protected boolean isGeneratedAlways;                      //是否总是生成

    public IntrospectedColumn() {
        super();
        properties = new Properties();
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Actual Column Name: "); 
        sb.append(actualColumnName);
        sb.append(", JDBC Type: "); 
        sb.append(jdbcType);
        sb.append(", Nullable: "); 
        sb.append(nullable);
        sb.append(", Length: "); 
        sb.append(length);
        sb.append(", Scale: "); 
        sb.append(scale);
        sb.append(", Identity: "); 
        sb.append(identity);
        return sb.toString();
    }

    public void setActualColumnName(String actualColumnName) {
        this.actualColumnName = actualColumnName;
        isColumnNameDelimited = StringUtil.stringContainsSpace(actualColumnName);
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isBLOBColumn() {
        String typeName = getJdbcTypeName();
        return "BINARY".equals(typeName) || "BLOB".equals(typeName)  
                || "CLOB".equals(typeName) || "LONGNVARCHAR".equals(typeName)   
                || "LONGVARBINARY".equals(typeName) || "LONGVARCHAR".equals(typeName)  
                || "NCLOB".equals(typeName) || "VARBINARY".equals(typeName);   
    }

    public boolean isStringColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getStringInstance());
    }

    public boolean isJdbcCharacterColumn() {
        return jdbcType == Types.CHAR || jdbcType == Types.CLOB
                || jdbcType == Types.LONGVARCHAR || jdbcType == Types.VARCHAR
                || jdbcType == Types.LONGNVARCHAR || jdbcType == Types.NCHAR
                || jdbcType == Types.NCLOB || jdbcType == Types.NVARCHAR;
    }

    public String getJavaProperty() {
        return getJavaProperty(null);
    }

    public String getJavaProperty(String prefix) {
        if (prefix == null) {
            return javaProperty;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(javaProperty);
        return sb.toString();
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public boolean isJDBCDateColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance()) && "DATE".equalsIgnoreCase(jdbcTypeName); 
    }

    public boolean isJDBCTimeColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance()) && "TIME".equalsIgnoreCase(jdbcTypeName); 
    }

    public String getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    public String getActualColumnName() {
        return actualColumnName;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public String getJdbcTypeName() {
        if (jdbcTypeName == null) {
            return "OTHER"; 
        }
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return fullyQualifiedJavaType;
    }

    public void setFullyQualifiedJavaType(FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isSequenceColumn() {
        return isSequenceColumn;
    }

    public void setSequenceColumn(boolean isSequenceColumn) {
        this.isSequenceColumn = isSequenceColumn;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public boolean isGeneratedColumn() {
        return isGeneratedColumn;
    }

    public void setGeneratedColumn(boolean isGeneratedColumn) {
        this.isGeneratedColumn = isGeneratedColumn;
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void setGeneratedAlways(boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
