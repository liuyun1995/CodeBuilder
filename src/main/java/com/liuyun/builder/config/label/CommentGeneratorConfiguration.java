package com.liuyun.builder.config.label;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.TypedPropertyHolder;

public class CommentGeneratorConfiguration extends TypedPropertyHolder {
	
    public CommentGeneratorConfiguration() {}

    public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("commentGenerator");
        if (getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", getConfigurationType())); 
        }
        addPropertyXmlElements(answer);
        return answer;
    }
    
}
