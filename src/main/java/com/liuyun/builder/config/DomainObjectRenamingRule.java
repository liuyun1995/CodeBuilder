package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

public class DomainObjectRenamingRule {
    private String searchString;
    private String replaceString;

    public String getReplaceString() {
        return replaceString;
    }

    public void setReplaceString(String replaceString) {
        this.replaceString = replaceString;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(searchString)) {
            errors.add(getString("ValidationError.28", tableName)); 
        }
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("domainRenamingRule"); 
        xmlElement.addAttribute(new Attribute("searchString", searchString)); 
        if (replaceString != null) {
            xmlElement.addAttribute(new Attribute("replaceString", replaceString)); 
        }
        return xmlElement;
    }
}
