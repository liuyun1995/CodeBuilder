package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;

public class SimpleModelRules extends BaseRules {
	
    public SimpleModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    //生成主键类
    @Override
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.getPrimaryKeyColumns().size() > 1;
    }
    
    //生成基础记录类
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
