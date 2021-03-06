package com.liuyun.builder.api;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.liuyun.builder.config.GeneratedKey;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.config.label.JavaMapperConfiguration;
import com.liuyun.builder.config.label.JavaModelConfiguration;
import com.liuyun.builder.config.label.TablesConfiguration;
import com.liuyun.builder.config.label.XmlMapperConfiguration;
import com.liuyun.builder.internal.rules.Rules;
import com.liuyun.builder.internal.rules.SimpleModelRules;

//逆向表
public abstract class IntrospectedTable {

    //内置属性
    protected enum InternalAttribute {
        ATTR_BASE_RECORD_TYPE,
        
        ATTR_XML_MAPPER_FILE_NAME,
        ATTR_JAVA_MAPPER_TYPE,
        ATTR_XML_MAPPER_NAMESPACE,
        
        ATTR_INSERT_STATEMENT_ID,
        ATTR_DELETE_STATEMENT_ID,
        ATTR_UPDATE_STATEMENT_ID,
        ATTR_SELECT_STATEMENT_ID,
        
        ATTR_BASE_RESULT_MAP_ID,
        ATTR_BASE_COLUMN_LIST_ID, 
        
        ATTR_PRIMARY_KEY_TYPE,
        ATTR_FULLY_QUALIFIED_TABLE_NAME,
        ATTR_ACTUAL_TABLE_NAME
    }

    //表的配置
    protected TablesConfiguration tablesConfiguration;

    //表的全限定名
    protected FullyQualifiedTable fullyQualifiedTable;

    //配置上下文
    protected Context context;

    //代码生成规则
    protected Rules rules;

    //主键集合
    protected List<IntrospectedColumn> primaryKeyColumns;

    //基础列集合
    protected List<IntrospectedColumn> baseColumns;

    //blob列集合
    protected List<IntrospectedColumn> blobColumns;

    //属性集合
    protected Map<String, Object> attributes;

    //内部属性集合
    protected Map<IntrospectedTable.InternalAttribute, String> internalAttributes;

    //从数据库元数据检索的表备注
    protected String remarks;

    //从数据库元数据检索的表类型
    protected String tableType;

    public IntrospectedTable() {
        super();
        primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        baseColumns = new ArrayList<IntrospectedColumn>();
        blobColumns = new ArrayList<IntrospectedColumn>();
        attributes = new HashMap<String, Object>();
        internalAttributes = new HashMap<IntrospectedTable.InternalAttribute, String>();
    }

    //-----------------------------------------getter和setter------------------------------------------

    //获取tablesConfiguration
    public TablesConfiguration getTableConfiguration() {
        return tablesConfiguration;
    }
    
    //设置tablesConfiguration
    public void setTableConfiguration(TablesConfiguration tableConfiguration) {
        this.tablesConfiguration = tableConfiguration;
    }
    
    //获取对应的全限定表
    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }
    
    //设置fullyQualifiedTable
    public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }
    
    //获取context
    public Context getContext() {
        return context;
    }
    
    //设置context
    public void setContext(Context context) {
        this.context = context;
    }
    
    //获取生成规则
    public Rules getRules() {
        return rules;
    }
    
    //设置生成规则
    public void setRules(Rules rules) {
        this.rules = rules;
    }
    
    //获取主键列集合
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }
    
    //获取基础列集合
    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }
    
    //获取所有BLOB类型的列
    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }
    
    //获取备注
    public String getRemarks() {
        return remarks;
    }

    //设置备注
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    //获取表类型
    public String getTableType() {
        return tableType;
    }

    //设置表类型
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    //-------------------------------------------获取和设置internalAttributes---------------------------------------

    //获取Base_Column_List标签ID
    public String getBaseColumnListId() {
        return internalAttributes.get(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID);
    }
    
    //设置Base_Column_List标签ID
    public void setBaseColumnListId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID, s);
    }

    //获取Base_Result_Map标签ID
    public String getBaseResultMapId() {
        return internalAttributes.get(InternalAttribute.ATTR_BASE_RESULT_MAP_ID);
    }
    
    //设置Base_Result_Map标签ID
    public void setBaseResultMapId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RESULT_MAP_ID, s);
    }
    
    //获取Insert方法ID
    public String getInsertStatementId() {
        return internalAttributes.get(InternalAttribute.ATTR_INSERT_STATEMENT_ID);
    }
    
    //设置Insert方法ID
    public void setInsertStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_INSERT_STATEMENT_ID, s);
    }
    
    //获取Delete方法ID
    public String getDeleteStatementId() {
        return internalAttributes.get(InternalAttribute.ATTR_DELETE_STATEMENT_ID);
    }
    
    //设置Delete方法ID
    public void setDeleteStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_DELETE_STATEMENT_ID, s);
    }
    
    //获取Update方法ID
    public String getUpdateStatementId() {
        return internalAttributes.get(InternalAttribute.ATTR_UPDATE_STATEMENT_ID);
    }

    //设置Update方法ID
    public void setUpdateStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_UPDATE_STATEMENT_ID, s);
    }

    //获取Select方法ID
    public String getSelectStatementId() {
        return internalAttributes.get(InternalAttribute.ATTR_SELECT_STATEMENT_ID);
    }
    
    //设置Select方法ID
    public void setSelectStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_SELECT_STATEMENT_ID, s);
    }
    
    //获取javaModel类型
    public String getBaseRecordType() {
        return internalAttributes.get(InternalAttribute.ATTR_BASE_RECORD_TYPE);
    }
    
    //设置javaModel类型
    public void setBaseRecordType(String baseRecordType) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RECORD_TYPE, baseRecordType);
    }
    
    //获取JavaMapper类型
    public String getJavaMapperType() {
        return internalAttributes.get(InternalAttribute.ATTR_JAVA_MAPPER_TYPE);
    }

    //设置JavaMapper类型
    public void setJavaMapperType(String JavaMapperType) {
        internalAttributes.put(InternalAttribute.ATTR_JAVA_MAPPER_TYPE, JavaMapperType);
    }
    
    //获取xmlMapper文件名
    public String getXmlMapperFileName() {
        return internalAttributes.get(InternalAttribute.ATTR_XML_MAPPER_FILE_NAME);
    }

    //设置xmlMapper文件名
    public void setXmlMapperFileName(String xmlMapperFileName) {
        internalAttributes.put(InternalAttribute.ATTR_XML_MAPPER_FILE_NAME, xmlMapperFileName);
    }
    
    //获取namespace
    public String getXmlMapperNamespace() {
    	return internalAttributes.get(InternalAttribute.ATTR_XML_MAPPER_NAMESPACE);
    }
    
    //设置namespace
    public void setXmlMapperNamespace(String xmlMapperNamespace) {
    	internalAttributes.put(InternalAttribute.ATTR_XML_MAPPER_NAMESPACE, xmlMapperNamespace);
    }
    
    //获取主键类型
    public String getPrimaryKeyType() {
        return internalAttributes.get(InternalAttribute.ATTR_PRIMARY_KEY_TYPE);
    }
    
    //设置主键类型
    public void setPrimaryKeyType(String primaryKeyType) {
        internalAttributes.put(InternalAttribute.ATTR_PRIMARY_KEY_TYPE, primaryKeyType);
    }
    
    //获取全限定表名
    public String getFullyQualifiedTableName() {
		return internalAttributes.get(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME);
	}
	
    //设置全限定表名
	public void setFullyQualifiedTableName(String fullyQualifiedTableName) {
        internalAttributes.put(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME, fullyQualifiedTableName);
    }
	
	//获取实际表名
	public String getActualTableName() {
		return internalAttributes.get(InternalAttribute.ATTR_ACTUAL_TABLE_NAME);
	}
	
	//设置实际表名
	public void setActualTableName(String actualTableName) {
		internalAttributes.put(InternalAttribute.ATTR_ACTUAL_TABLE_NAME, actualTableName);
	}
    
    //--------------------------------------------------对列的一些操作-------------------------------------------------
    
    //通过列名获取逆向列
    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            //遍历寻找主键
            for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            //遍历寻找基础列
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            //遍历寻找blob列
            for (IntrospectedColumn introspectedColumn : blobColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            return null;
        }
    }

    //是否含有JDBCDate的列
    public boolean hasJDBCDateColumns() {
        boolean rc = false;
        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }
        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCDateColumn()) {
                    rc = true;
                    break;
                }
            }
        }
        return rc;
    }
    
    //是否含有JDBCTime的列
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;
        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }
        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCTimeColumn()) {
                    rc = true;
                    break;
                }
            }
        }
        return rc;
    }
    
    //是否存在主键
    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }
    
    //获取所有的列
    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);
        return answer;
    }

    //获取除了BLOB之外的所有列
    public List<IntrospectedColumn> getNonBLOBColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        return answer;
    }

    //获取非BLOB类型的列的数量
    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size() + baseColumns.size();
    }

    //获取除了主键之外的所有列
    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);
        return answer;
    }

    //是否含有BLOB类型的列
    public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
    }

    //是否存在基础列
    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }

    //是否存在列
    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0 || baseColumns.size() > 0 || blobColumns.size() > 0;
    }
    
    //添加列
    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }
        introspectedColumn.setIntrospectedTable(this);
    }

    //添加主键
    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }

    //---------------------------------------------初始化时计算出必要的属性-------------------------------------------
    
    //初始化
    public void initialize() {
    	calculateJavaMapperAttributes();       //计算JavaMapper属性
    	calculateJavaModelAttributes();        //计算JavaModel属性
        calculateXmlMapperAttributes();        //计算XmlMapper属性
        //根据类型获取不同的规则实例
        rules = new SimpleModelRules(this);
        context.getPlugins().initialized(this);
    }
    
    //计算JavaModel属性
    protected void calculateJavaModelAttributes() {
        String pakkage = calculateJavaModelPackage();
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        setBaseRecordType(sb.toString());
    }
    
    //获取JavaModel的包
    protected String calculateJavaModelPackage() {
        JavaModelConfiguration config = tablesConfiguration.getJavaModelConfiguration();
        if(config != null && !stringHasValue(config.getTargetPackage())) {
        	return config.getTargetPackage();
        }
        return tablesConfiguration.getTargetPackage();
    }

    //计算JavaMapper属性
    protected void calculateJavaMapperAttributes() {
    	StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaMapperPackage());
        sb.append('.');
        JavaMapperConfiguration config = tablesConfiguration.getJavaMapperConfiguration();
        if (config != null && stringHasValue(config.getName())) {
            sb.append(tablesConfiguration.getJavaMapperConfiguration().getName());
        } else {
            if (stringHasValue(fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); 
        }
        setJavaMapperType(sb.toString());
    }
    
    //获取JavaMapper的包
    protected String calculateJavaMapperPackage() {
        JavaMapperConfiguration config = tablesConfiguration.getJavaMapperConfiguration();
        if(config != null && !stringHasValue(config.getTargetPackage())) {
        	return config.getTargetPackage();
        }
        return tablesConfiguration.getTargetPackage();
    }
    
    //计算xmlMapper属性
    protected void calculateXmlMapperAttributes() {
    	setActualTableName(tablesConfiguration.getTableName().toUpperCase());
    	setFullyQualifiedTableName(calculateFullyQualifiedTableName());
        setXmlMapperFileName(calculateXmlMapperFileName());
        setXmlMapperNamespace(calculateXmlMapperNamespace());
        
        setBaseResultMapId("BaseResultMap");
        setBaseColumnListId("Base_Column_List");
        setInsertStatementId("insert");
        setDeleteStatementId("delete");
        setUpdateStatementId("update");
        setSelectStatementId("select");
    }
    
    //计算XmlMapper文件名
    protected String calculateXmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        XmlMapperConfiguration config = tablesConfiguration.getXmlMapperConfiguration();
        if (config != null && stringHasValue(config.getName())) {
            String mapperName = config.getName();
            int ind = mapperName.lastIndexOf('.');
            if (ind == -1) {
                sb.append(mapperName);
            } else {
                sb.append(mapperName.substring(ind + 1));
            }
            sb.append(".xml");
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml");
        }
        return sb.toString();
    }
    
    //-----------------------------------------------抽象方法----------------------------------------------------

    public abstract void calculateGenerators(List<String> warnings, ProgressCallback progressCallback);

    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();

    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();

    public abstract int getGenerationSteps();
    
    //-----------------------------------------------其他方法----------------------------------------------------

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }
    
    //根据属性名获取tablesConfiguration的属性
    public String getTableConfigurationProperty(String property) {
        return tablesConfiguration.getProperty(property);
    }
	
	protected String calculateFullyQualifiedTableName() {
        return fullyQualifiedTable.getFullyQualifiedTableName();
    }

	public GeneratedKey getGeneratedKey() {
		return null;
	}
	
	protected String calculateXmlMapperNamespace() {
        StringBuilder sb = new StringBuilder();
        JavaMapperConfiguration config = tablesConfiguration.getJavaMapperConfiguration();
        if (config != null && stringHasValue(config.getName())) {
        	sb.append(config.getTargetPackage());
        	sb.append('.');
            sb.append(config.getName());
        } else {
        	sb.append(tablesConfiguration.getTargetPackage());
        	sb.append('.');
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper"); 
        }
        return sb.toString();
    }
	
}
