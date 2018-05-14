package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.StringUtil.stringContainsSpace;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.PropertyHolder;

public class ColumnOverride extends PropertyHolder {
	
    private String columnName;
    
    private String javaProperty;
    
    private String jdbcType;
    
    private String javaType;
    
    private String typeHandler;
    
    private boolean isColumnNameDelimited;
    
    private String configuredDelimitedColumnName;
    
    private boolean isGeneratedAlways;
    
    public ColumnOverride(String columnName) {
        super();
        this.columnName = columnName;
        isColumnNameDelimited = stringContainsSpace(columnName);
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public String getJavaProperty() {
        return javaProperty;
    }
    
    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }
    
    public String getJavaType() {
        return javaType;
    }
    
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
    
    public String getJdbcType() {
        return jdbcType;
    }
    
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
    
    public String getTypeHandler() {
        return typeHandler;
    }
    
    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("columnOverride"); 
        xmlElement.addAttribute(new Attribute("column", columnName)); 

        if (stringHasValue(javaProperty)) {
            xmlElement.addAttribute(new Attribute("property", javaProperty)); 
        }

        if (stringHasValue(javaType)) {
            xmlElement.addAttribute(new Attribute("javaType", javaType)); 
        }

        if (stringHasValue(jdbcType)) {
            xmlElement.addAttribute(new Attribute("jdbcType", jdbcType)); 
        }

        if (stringHasValue(typeHandler)) {
            xmlElement.addAttribute(new Attribute("typeHandler", typeHandler)); 
        }

        if (stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", configuredDelimitedColumnName)); 
        }
        addPropertyXmlElements(xmlElement);
        return xmlElement;
    }
    
    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }
    
    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        configuredDelimitedColumnName = isColumnNameDelimited ? "true" : "false";
    }
    
    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.22", tableName));
        }
    }

    public boolean isGeneratedAlways() {
        return isGeneratedAlways;
    }

    public void setGeneratedAlways(boolean isGeneratedAlways) {
        this.isGeneratedAlways = isGeneratedAlways;
    }
}
