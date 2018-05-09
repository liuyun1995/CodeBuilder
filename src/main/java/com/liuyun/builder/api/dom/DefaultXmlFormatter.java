package com.liuyun.builder.api.dom;

import com.liuyun.builder.api.XmlFormatter;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.config.label.Context;

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
