package com.liuyun.builder.config;

import static org.mybatis.generator.internal.util.StringUtil.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.mybatis.generator.internal.db.DatabaseDialects;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

//生成主键
public class GeneratedKey {

    private String column;

    private String configuredSqlStatement;

    private String runtimeSqlStatement;

    private boolean isIdentity;

    private String type;

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

    /**
     * This method is used by the iBATIS2 generators to know if the XML &lt;selectKey&gt; element should be placed before the
     * insert SQL statement.
     *
     * @return true, if is placed before insert in ibatis2
     */
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
