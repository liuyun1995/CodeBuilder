package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.PropertyHolder;

//SqlMap生成器配置
public class XmlMapperConfiguration extends PropertyHolder {
	
    private String target;

    public XmlMapperConfiguration() {
        super();
    }

    public String getTarget() {
		return target;
	}
    
	public void setTarget(String target) {
		this.target = target;
	}
	
	public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("sqlMapGenerator"); 
        if (target != null) {
            answer.addAttribute(new Attribute("target", target)); 
        }
        addPropertyXmlElements(answer);
        return answer;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(target)) {
            errors.add(getString("ValidationError.1", contextId)); 
        }
    }
}
