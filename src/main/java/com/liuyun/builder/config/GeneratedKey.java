package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.internal.db.DatabaseDialects;

//生成主键
public class GeneratedKey {

    private String column;                     //列名

    private String configuredSqlStatement;     //配置的sql声明

    private String runtimeSqlStatement;        //运行时sql声明

    private boolean isIdentity;               //是否是主键

    private String type;                       //类型

    public GeneratedKey(String column, String configuredSqlStatement,
            boolean isIdentity, String type) {
        super();
        this.column = column;
        this.type = type;
        this.isIdentity = isIdentity;
        this.configuredSqlStatement = configuredSqlStatement;
        DatabaseDialects dialect = DatabaseDialects.getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        } else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
    }

    public String getColumn() {
        return column;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public String getRuntimeSqlStatement() {
        return runtimeSqlStatement;
    }

    public String getType() {
        return type;
    }
    
    public boolean isPlacedBeforeInsertInIbatis2() {
        boolean rc;
        if (stringHasValue(type)) {
            rc = true;
        } else {
            rc = !isIdentity;
        }
        return rc;
    }

    public String getMyBatis3Order() {
        return isIdentity ? "AFTER" : "BEFORE";  
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("generatedKey"); 
        xmlElement.addAttribute(new Attribute("column", column)); 
        xmlElement.addAttribute(new Attribute("sqlStatement", configuredSqlStatement)); 
        if (stringHasValue(type)) {
            xmlElement.addAttribute(new Attribute("type", type)); 
        }
        xmlElement.addAttribute(new Attribute("identity", isIdentity ? "true" : "false"));  
        return xmlElement;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(runtimeSqlStatement)) {
            errors.add(getString("ValidationError.7", tableName));
        }
        if (stringHasValue(type) && !"pre".equals(type) && !"post".equals(type)) {
            errors.add(getString("ValidationError.15", tableName)); 
        }
        if ("pre".equals(type) && isIdentity) { 
            errors.add(getString("ValidationError.23",  tableName));
        }
        if ("post".equals(type) && !isIdentity) {
            errors.add(getString("ValidationError.24", tableName));
        }
    }

    public boolean isJdbcStandard() {
        return "JDBC".equals(runtimeSqlStatement); 
    }
}
