package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

public class IgnoredColumnException extends IgnoredColumn {

    public IgnoredColumnException(String columnName) {
        super(columnName);
    }

    @Override
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("except"); 
        xmlElement.addAttribute(new Attribute("column", columnName)); 
        if (stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", configuredDelimitedColumnName)); 
        }
        return xmlElement;
    }

    @Override
    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.26", tableName));
        }
    }
    
}
