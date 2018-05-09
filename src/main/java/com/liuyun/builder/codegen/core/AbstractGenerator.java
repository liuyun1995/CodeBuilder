package com.liuyun.builder.codegen.core;

import java.util.List;

import org.mybatis.generator.config.label.Context;

import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.ProgressCallback;
import com.liuyun.builder.api.dom.xml.XmlElement;

//抽象生成器
public abstract class AbstractGenerator {
	
    protected Context context;                         //配置上下文
    protected IntrospectedTable introspectedTable;     //逆向表
    protected List<String> warnings;                   //警告信息
    protected ProgressCallback progressCallback;       //回调句柄
    
    public AbstractGenerator() {
        super();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

	protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
		
	}
}
