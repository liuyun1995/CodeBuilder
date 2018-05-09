package com.liuyun.builder.api.dom;

import org.mybatis.generator.config.label.Context;

import com.liuyun.builder.api.JavaFormatter;
import com.liuyun.builder.api.dom.java.CompilationUnit;

//生成Java文件的格式器
public class DefaultJavaFormatter implements JavaFormatter {
	
    protected Context context;

    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        return compilationUnit.getFormattedContent();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
    
}
