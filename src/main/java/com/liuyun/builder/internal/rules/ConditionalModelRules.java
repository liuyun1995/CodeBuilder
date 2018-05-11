package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;

public class ConditionalModelRules extends BaseRules {
	
    public ConditionalModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    //生成只包含主键的类
    @Override
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.getPrimaryKeyColumns().size() > 1;
    }
    
    //生成包含基础字段的类
    @Override
	public boolean generateBaseRecordClass() {
		return false;
	}
    
	@Override
	public boolean generateResultMap() {
		return true;
	}

	@Override
	public boolean generateBaseColumnList() {
		return true;
	}

	@Override
	public boolean generateJavaClient() {
		return true;
	}
    
}
