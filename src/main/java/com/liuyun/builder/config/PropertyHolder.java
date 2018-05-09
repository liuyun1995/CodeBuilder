package com.liuyun.builder.config;

import java.util.Enumeration;
import java.util.Properties;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;

//属性处理器
public abstract class PropertyHolder {
    
	private Properties properties;
	
    public PropertyHolder() {
        super();
        properties = new Properties();
    }

    public void addProperty(String name, String value) {
        properties.setProperty(name, value);
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public Properties getProperties() {
        return properties;
    }

    protected void addPropertyXmlElements(XmlElement xmlElement) {
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();
            XmlElement propertyElement = new XmlElement("property"); 
            propertyElement.addAttribute(new Attribute("name", propertyName)); 
            propertyElement.addAttribute(new Attribute("value", properties.getProperty(propertyName)));
            xmlElement.addElement(propertyElement);
        }
    }
    
}
