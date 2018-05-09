package com.liuyun.builder.api.dom;

import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.config.label.Context;

import com.liuyun.builder.api.XmlFormatter;

//生成XML文件的格式器
public class DefaultXmlFormatter implements XmlFormatter {
	
    protected Context context;

    @Override
    public String getFormattedContent(Document document) {
        return document.getFormattedContent();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
    
}
