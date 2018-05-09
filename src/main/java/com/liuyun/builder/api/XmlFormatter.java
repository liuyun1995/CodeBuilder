package com.liuyun.builder.api;

import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.config.label.Context;

public interface XmlFormatter {
	
	//设置上下文
    void setContext(Context context);

    //获取格式化文本
    String getFormattedContent(Document document);
    
}
