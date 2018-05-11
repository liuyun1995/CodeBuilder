package com.liuyun.builder.config;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.exception.InvalidConfigurationException;

//配置代表类
public class Configuration {

    //context列表
    private List<Context> contexts;

    //classPathEntrie列表
    private List<String> jdbcDrivers;

    public Configuration() {
        super();
        contexts = new ArrayList<Context>();
        jdbcDrivers = new ArrayList<String>();
    }
    
    public void addJdbcDrivers(String entry) {
    	jdbcDrivers.add(entry);
    }
    
    public List<String> getJdbcDrivers() {
        return jdbcDrivers;
    }

    //验证方法
    public void validate() throws InvalidConfigurationException {
        List<String> errors = new ArrayList<String>();
        for (String jdbcDriver : jdbcDrivers) {
            if (!stringHasValue(jdbcDriver)) {
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
        Document document = new Document();
        XmlElement rootElement = new XmlElement("generatorConfiguration"); 
        document.setRootElement(rootElement);
        for (String jdbcDriver : jdbcDrivers) {
            XmlElement cpeElement = new XmlElement("jdbcDriver"); 
            cpeElement.addAttribute(new Attribute("location", jdbcDriver)); 
            rootElement.addElement(cpeElement);
        }
        for (Context context : contexts) {
            rootElement.addElement(context.toXmlElement());
        }
        return document;
    }
}
