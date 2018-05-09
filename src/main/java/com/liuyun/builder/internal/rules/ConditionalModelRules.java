package com.liuyun.builder.internal.rules;

import com.liuyun.builder.api.IntrospectedTable;

public class ConditionalModelRules extends BaseRules {
	
    public ConditionalModelRules(IntrospectedTable introspectedTable) {
        super(introspectedTable);
    }
    
    @Override
    public boolean generatePrimaryKeyClass() {
        return introspectedTable.getPrimaryKeyColumns().size() > 1;
    }
    
    @Override
    public boolean generateBaseRecordClass() {
        return introspectedTable.hasBaseColumns()
                || introspectedTable.getPrimaryKeyColumns().size() == 1
                || blobsAreInBaseRecord();
    }
    
    private boolean blobsAreInBaseRecord() {
        return introspectedTable.hasBLOBColumns() && !generateRecordWithBLOBsClass();
    }
    
    @Override
    public boolean generateRecordWithBLOBsClass() {
        int otherColumnCount = introspectedTable.getPrimaryKeyColumns().size() + introspectedTable.getBaseColumns().size();
        return otherColumnCount > 1 && introspectedTable.getBLOBColumns().size() > 1;
    }

	@Override
	public boolean generateResultMap() {
		return false;
	}

	@Override
	public boolean generateBaseColumnList() {
		return false;
	}

	@Override
	public boolean generateJavaClient() {
		return false;
	}
    
}
