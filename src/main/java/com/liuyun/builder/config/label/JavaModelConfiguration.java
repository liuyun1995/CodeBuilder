package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.PropertyHolder;

//JavaModel生成器配置
public class JavaModelConfiguration extends PropertyHolder {

	private String name;             //javaModel名
	private String targetProject;    //项目路径
	private String targetPackage;    //包路径
    
    public JavaModelConfiguration() {
        super();
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("javaModelGenerator"); 
        if (targetProject != null) {
            answer.addAttribute(new Attribute("targetProject", targetProject)); 
        }
        if (targetPackage != null) {
            answer.addAttribute(new Attribute("targetPackage", targetPackage)); 
        }
        addPropertyXmlElements(answer);
        return answer;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(targetPackage)) {
            errors.add(getString("ValidationError.0", contextId)); 
        }
    }
    
}
