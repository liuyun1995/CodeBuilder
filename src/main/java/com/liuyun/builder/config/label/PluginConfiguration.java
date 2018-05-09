package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.TypedPropertyHolder;

public class PluginConfiguration extends TypedPropertyHolder {
	
	public PluginConfiguration() {}

	public XmlElement toXmlElement() {
		XmlElement answer = new XmlElement("plugin"); 
		if (getConfigurationType() != null) {
			answer.addAttribute(new Attribute("type", getConfigurationType())); 
		}
		addPropertyXmlElements(answer);
		return answer;
	}

	public void validate(List<String> errors, String contextId) {
		if (!stringHasValue(getConfigurationType())) {
			errors.add(getString("ValidationError.17", 
					contextId));
		}
	}
	
}
