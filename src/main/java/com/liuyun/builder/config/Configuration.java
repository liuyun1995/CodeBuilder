package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.ArrayList;
import java.util.List;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.XmlConstants;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.exception.InvalidConfigurationException;

//配置代表类
public class Configuration {

    //context列表
    private List<Context> contexts;

    //classPathEntrie列表
    private List<String> classPathEntries;

    public Configuration() {
        super();
        contexts = new ArrayList<Context>();
        classPathEntries = new ArrayList<String>();
    }
    
    public void addClasspathEntry(String entry) {
        classPathEntries.add(entry);
    }
    
    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    //验证方法
    public void validate() throws InvalidConfigurationException {
        List<String> errors = new ArrayList<String>();
        for (String classPathEntry : classPathEntries) {
            if (!stringHasValue(classPathEntry)) {
                errors.add(getString("ValidationError.19")); 
                break;
            }
        }
        if (contexts.size() == 0) {
            errors.add(getString("ValidationError.11")); 
        } else {
            for (Context context : contexts) {
                context.validate(errors);
            }
        }
        if (errors.size() > 0) {
            throw new InvalidConfigurationException(errors);
        }
    }
    
    public List<Context> getContexts() {
        return contexts;
    }
    
    public void addContext(Context context) {
        contexts.add(context);
    }
    
    public Context getContext(String id) {
        for (Context context : contexts) {
            if (id.equals(context.getId())) {
                return context;
            }
        }
        return null;
    }
    
    public Document toDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID);
        XmlElement rootElement = new XmlElement("generatorConfiguration"); 
        document.setRootElement(rootElement);
        for (String classPathEntry : classPathEntries) {
            XmlElement cpeElement = new XmlElement("classPathEntry"); 
            cpeElement.addAttribute(new Attribute("location", classPathEntry)); 
            rootElement.addElement(cpeElement);
        }
        for (Context context : contexts) {
            rootElement.addElement(context.toXmlElement());
        }
        return document;
    }
}
