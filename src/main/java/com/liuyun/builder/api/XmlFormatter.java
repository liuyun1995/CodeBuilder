package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.config.label.Context;

public interface XmlFormatter {
	
	//设置上下文
    void setContext(Context context);

    //获取格式化文本
    String getFormattedContent(Document document);
    
}
