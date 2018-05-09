package com.liuyun.builder.api;

import org.mybatis.generator.config.label.Context;

import com.liuyun.builder.api.dom.java.CompilationUnit;

public interface JavaFormatter {
	
	//设置上下文
    void setContext(Context context);

    //获取格式化文本
    String getFormattedContent(CompilationUnit compilationUnit);
    
}
