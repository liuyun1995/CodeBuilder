package com.liuyun.builder.internal.db;

import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;

//全限定表名
public class ActualTableName {

    private String tableName;  //表名
    private String catalog;    //目录
    private String schema;     //概要
    private String fullName;   //全名

    public ActualTableName(String catalog, String schema, String tableName) {
        this.catalog = catalog;
        this.schema = schema;
        this.tableName = tableName;
        fullName = composeFullyQualifiedTableName(catalog, schema, tableName, '.');
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ActualTableName)) {
            return false;
        }
        return obj.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public String toString() {
        return fullName;
    }
}
