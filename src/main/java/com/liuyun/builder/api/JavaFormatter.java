package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.config.label.Context;

public interface JavaFormatter {
	
	//设置上下文
    void setContext(Context context);

    //获取格式化文本
    String getFormattedContent(CompilationUnit compilationUnit);
    
}
