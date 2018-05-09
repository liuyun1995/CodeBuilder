package com.liuyun.builder.api.dom;

import com.liuyun.builder.api.JavaFormatter;
import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.config.label.Context;

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
