package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.EqualsUtil.areEqual;
import static com.liuyun.builder.internal.utils.HashCodeUtil.SEED;
import static com.liuyun.builder.internal.utils.HashCodeUtil.hash;
import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.ColumnOverride;
import com.liuyun.builder.config.ColumnRenamingRule;
import com.liuyun.builder.config.DomainObjectRenamingRule;
import com.liuyun.builder.config.IgnoredColumn;
import com.liuyun.builder.config.IgnoredColumnPattern;
import com.liuyun.builder.config.PropertyHolder;

//table标签配置
public class TablesConfiguration extends PropertyHolder {
	
	//数据库catalog
    private String catalog;
    //数据库schema
    private String schema;
    //表名
    private String tableName;
    //生成路径
    private String target;
    
    private List<ColumnOverride> columnOverrides;

    private Map<IgnoredColumn, Boolean> ignoredColumns;
    
    private JavaModelConfiguration javaModelConfiguration;
    
    private JavaMapperConfiguration javaMapperConfiguration;
    
    private XmlMapperConfiguration xmlMapperConfiguration;
    
    private ColumnRenamingRule columnRenamingRule;
    
    private List<IgnoredColumnPattern> ignoredColumnPatterns = new ArrayList<IgnoredColumnPattern>();

    public TablesConfiguration(Context context) {}
    
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public JavaModelConfiguration getJavaModelConfiguration() {
		return javaModelConfiguration;
	}

	public void setJavaModelConfiguration(JavaModelConfiguration javaModelConfiguration) {
		this.javaModelConfiguration = javaModelConfiguration;
	}

	public JavaMapperConfiguration getJavaMapperConfiguration() {
		return javaMapperConfiguration;
	}

	public void setJavaMapperConfiguration(JavaMapperConfiguration javaMapperConfiguration) {
		this.javaMapperConfiguration = javaMapperConfiguration;
	}

	public XmlMapperConfiguration getXmlMapperConfiguration() {
		return xmlMapperConfiguration;
	}

	public void setXmlMapperConfiguration(XmlMapperConfiguration xmlMapperConfiguration) {
		this.xmlMapperConfiguration = xmlMapperConfiguration;
	}
	
	public ColumnOverride getColumnOverride(String columnName) {
        for (ColumnOverride co : columnOverrides) {
            if (co.isColumnNameDelimited()) {
                if (columnName.equals(co.getColumnName())) {
                    return co;
                }
            } else {
                if (columnName.equalsIgnoreCase(co.getColumnName())) {
                    return co;
                }
            }
        }
        return null;
    }
	
	public ColumnRenamingRule getColumnRenamingRule() {
        return columnRenamingRule;
    }
	
	public boolean isColumnIgnored(String columnName) {
        for (Map.Entry<IgnoredColumn, Boolean> entry : ignoredColumns.entrySet()) {
            if (entry.getKey().matches(columnName)) {
                entry.setValue(Boolean.TRUE);
                return true;
            }
        }
        for (IgnoredColumnPattern ignoredColumnPattern : ignoredColumnPatterns) {
            if (ignoredColumnPattern.matches(columnName)) {
                return true;
            }
        }
        return false;
    }

	public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("table"); 
        xmlElement.addAttribute(new Attribute("tableName", tableName)); 
        if (stringHasValue(catalog)) {
            xmlElement.addAttribute(new Attribute("catalog", catalog)); 
        }
        if (stringHasValue(schema)) {
            xmlElement.addAttribute(new Attribute("schema", schema)); 
        }
        addPropertyXmlElements(xmlElement);
        return xmlElement;
    }

    @Override
    public String toString() {
        return composeFullyQualifiedTableName(catalog, schema, tableName, '.');
    }

    public void validate(List<String> errors, int listPosition) {
        if (!stringHasValue(tableName)) {
            errors.add(getString("ValidationError.6", Integer.toString(listPosition))); 
        }
    }
    
    @Override
    public int hashCode() {
        int result = SEED;
        result = hash(result, catalog);
        result = hash(result, schema);
        result = hash(result, tableName);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TablesConfiguration)) {
            return false;
        }
        TablesConfiguration other = (TablesConfiguration) obj;
        return areEqual(this.catalog, other.catalog) && areEqual(this.schema, other.schema) && areEqual(this.tableName, other.tableName);
    }

	public String getDomainObjectName() {
		return null;
	}

	public DomainObjectRenamingRule getDomainObjectRenamingRule() {
		return null;
	}

	public String getAlias() {
		return null;
	}

	public boolean isDelimitIdentifiers() {
		// TODO Auto-generated method stub
		return false;
	}
    
}
