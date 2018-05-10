package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringContainsSpace;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.util.List;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

public class IgnoredColumn {
	
    protected String columnName;
    
    private boolean isColumnNameDelimited;
    
    protected String configuredDelimitedColumnName;
    
    public IgnoredColumn(String columnName) {
        super();
        this.columnName = columnName;
        isColumnNameDelimited = stringContainsSpace(columnName);
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }
    
    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        configuredDelimitedColumnName = isColumnNameDelimited ? "true" : "false";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IgnoredColumn)) {
            return false;
        }
        return columnName.equals(((IgnoredColumn) obj).getColumnName());
    }
    
    @Override
    public int hashCode() {
        return columnName.hashCode();
    }
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("ignoreColumn"); 
        xmlElement.addAttribute(new Attribute("column", columnName)); 
        if (stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", configuredDelimitedColumnName)); 
        }
        return xmlElement;
    }
    
    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.21", tableName));
        }
    }

    public boolean matches(String columnName) {
        if (isColumnNameDelimited) {
            return this.columnName.equals(columnName);
        } else {
            return this.columnName.equalsIgnoreCase(columnName);
        }
    }
}
