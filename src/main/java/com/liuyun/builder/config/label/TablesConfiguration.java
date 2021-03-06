package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.EqualsUtil.areEqual;
import static com.liuyun.builder.internal.utils.HashCodeUtil.SEED;
import static com.liuyun.builder.internal.utils.HashCodeUtil.hash;
import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

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
    //项目路径
    private String targetProject;
    //包路径
    private String targetPackage;
    
    private JavaModelConfiguration javaModelConfiguration;
    
    private JavaMapperConfiguration javaMapperConfiguration;
    
    private XmlMapperConfiguration xmlMapperConfiguration;
    
    private boolean delimitIdentifiers;
    
    private String alias;

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

	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
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

	public String getDomainObjectName() {
		return null;
	}

	public boolean isDelimitIdentifiers() {
        return delimitIdentifiers;
    }

	public String getAlias() {
		return alias;
	}
    
}
