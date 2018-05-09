package com.liuyun.builder.config.label;

import static org.mybatis.generator.internal.util.StringUtil.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.PropertyHolder;

//JavaClient生成器配置
public class JavaMapperConfiguration extends PropertyHolder {
    
	private String target;
    
    public JavaMapperConfiguration() {
        super();
    }

    public String getTarget() {
		return target;
	}
    
	public void setTarget(String target) {
		this.target = target;
	}

	public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("javaClientGenerator"); 
        if (target != null) {
            answer.addAttribute(new Attribute("target", target)); 
        }
        addPropertyXmlElements(answer);
        return answer;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(target)) {
            errors.add(getString("ValidationError.2", contextId)); 
        }
    }
}