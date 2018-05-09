package com.liuyun.builder.config.label;

import static org.mybatis.generator.internal.util.EqualsUtil.areEqual;
import static org.mybatis.generator.internal.util.HashCodeUtil.SEED;
import static org.mybatis.generator.internal.util.HashCodeUtil.hash;
import static org.mybatis.generator.internal.util.StringUtil.composeFullyQualifiedTableName;
import static org.mybatis.generator.internal.util.StringUtil.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
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
    
    private JavaModelConfiguration javaModelConfiguration;
    
    private JavaMapperConfiguration javaMapperConfiguration;
    
    private XmlMapperConfiguration xmlMapperConfiguration;

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
    
}
