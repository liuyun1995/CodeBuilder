package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

public class IgnoredColumnPattern {

    private String patternRegex;
    private Pattern pattern;
    private List<IgnoredColumnException> exceptions = new ArrayList<IgnoredColumnException>();

    public IgnoredColumnPattern(String patternRegex) {
        this.patternRegex = patternRegex;
        pattern = Pattern.compile(patternRegex);
    }

    public void addException(IgnoredColumnException exception) {
        exceptions.add(exception);
    }

    public boolean matches(String columnName) {
        boolean matches = pattern.matcher(columnName).matches();
        if (matches) {
            for (IgnoredColumnException exception : exceptions) {
                if (exception.matches(columnName)) {
                    matches = false;
                    break;
                }
            }
        }
        return matches;
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("ignoreColumnsByRegex"); 
        xmlElement.addAttribute(new Attribute("pattern", patternRegex)); 
        for (IgnoredColumnException exception : exceptions) {
            xmlElement.addElement(exception.toXmlElement());
        }
        return xmlElement;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(patternRegex)) {
            errors.add(getString("ValidationError.27", tableName));
        }
    }
}
