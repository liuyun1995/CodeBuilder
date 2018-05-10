package com.liuyun.builder.internal.db;

import static com.liuyun.builder.internal.utils.JavaBeansUtil.getCamelCaseString;
import static com.liuyun.builder.internal.utils.JavaBeansUtil.getValidPropertyName;
import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
import static com.liuyun.builder.internal.utils.StringUtil.isTrue;
import static com.liuyun.builder.internal.utils.StringUtil.stringContainsSQLWildcard;
import static com.liuyun.builder.internal.utils.StringUtil.stringContainsSpace;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.JavaTypeResolver;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.config.ColumnOverride;
import com.liuyun.builder.config.GeneratedKey;
import com.liuyun.builder.config.PropertyRegistry;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.config.label.TablesConfiguration;
import com.liuyun.builder.internal.ObjectFactory;
import com.liuyun.builder.logging.Log;
import com.liuyun.builder.logging.LogFactory;

//数据库逆向器
public class DatabaseIntrospector {
	
    private DatabaseMetaData databaseMetaData;    //数据库元数据
    private JavaTypeResolver javaTypeResolver;    //Java类型转换
    private List<String> warnings;                //警告信息
    private Context context;                      //配置上下文
    private Log logger;                           //日志类

    public DatabaseIntrospector(Context context, DatabaseMetaData databaseMetaData, 
    		JavaTypeResolver javaTypeResolver, List<String> warnings) {
        super();
        this.context = context;
        this.databaseMetaData = databaseMetaData;
        this.javaTypeResolver = javaTypeResolver;
        this.warnings = warnings;
        logger = LogFactory.getLog(getClass());
    }

    //核心入口方法
    public List<IntrospectedTable> introspectTables(TablesConfiguration tc) throws SQLException {
    	//获取该表的所有列, 一个TablesConfiguration对应一个表
        Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);
        if (columns.isEmpty()) {
            warnings.add(getString("Warning.19", tc.getCatalog(), tc.getSchema(), tc.getTableName()));
            return null;
        }
        //移除忽略的列
        removeIgnoredColumns(tc, columns);
        //设置列对应的java类型和属性名
        calculateExtraColumnInformation(tc, columns);
        //根据配置对列进行覆盖
        applyColumnOverrides(tc, columns);

        //真正的获取逆向表的方法
        List<IntrospectedTable> introspectedTables = calculateIntrospectedTables(tc, columns);
        Iterator<IntrospectedTable> iter = introspectedTables.iterator();
        while (iter.hasNext()) {
            IntrospectedTable introspectedTable = iter.next();
            //若该表不存在任何列, 则从列表剔除
            if (!introspectedTable.hasAnyColumns()) {
                String warning = getString("Warning.1", introspectedTable.getFullyQualifiedTable().toString()); 
                warnings.add(warning);
                iter.remove();
            //若该表只有Blob列, 则从列表剔除
            } else if (!introspectedTable.hasPrimaryKeyColumns() && !introspectedTable.hasBaseColumns()) {
                String warning = getString("Warning.18", introspectedTable.getFullyQualifiedTable().toString()); 
                warnings.add(warning);
                iter.remove();
            //报告逆向转换的警告信息
            } else {
                reportIntrospectionWarnings(introspectedTable, tc, introspectedTable.getFullyQualifiedTable());
            }
        }
        return introspectedTables;
    }

    //移除忽略的列
    private void removeIgnoredColumns(TablesConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            Iterator<IntrospectedColumn> tableColumns = entry.getValue().iterator();
            while (tableColumns.hasNext()) {
                IntrospectedColumn introspectedColumn = tableColumns.next();
                if (tc.isColumnIgnored(introspectedColumn.getActualColumnName())) {
                    tableColumns.remove();
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.3", introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                    }
                }
            }
        }
    }

    //对列进行额外处理(包括属性名替换, 类型转换)
    private void calculateExtraColumnInformation(TablesConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = null;
        String replaceString = null;
        if (tc.getColumnRenamingRule() != null) {
        	//获取查找串的模式
            pattern = Pattern.compile(tc.getColumnRenamingRule().getSearchString());
            //获取替代字符串
            replaceString = tc.getColumnRenamingRule().getReplaceString();
            replaceString = replaceString == null ? "" : replaceString; 
        }

        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                String calculatedColumnName;
                //如果模式为空, 则列名为实际列名
                if (pattern == null) {
                    calculatedColumnName = introspectedColumn.getActualColumnName();
                } else {
                	//否则使用模式来匹配实际列名
                    Matcher matcher = pattern.matcher(introspectedColumn.getActualColumnName());
                    //替换实际列名
                    calculatedColumnName = matcher.replaceAll(replaceString);
                }
                //转换成驼峰命名
                introspectedColumn.setJavaProperty(getCamelCaseString(calculatedColumnName, false));

                //获取该列对应的java类型(使用传入的类型转换器)
                FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(introspectedColumn);
                
                if (fullyQualifiedJavaType != null) {
                    introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
                } else {
                    boolean warn = true;
                    //判断该列是否被忽略
                    if (tc.isColumnIgnored(introspectedColumn.getActualColumnName())) {
                        warn = false;
                    }
                    //判断该列是否被覆盖
                    ColumnOverride co = tc.getColumnOverride(introspectedColumn.getActualColumnName());
                    if (co != null && stringHasValue(co.getJavaType())) {
                        warn = false;
                    }
                    //以上两种情况都不是
                    if (warn) {
                    	//设置该列对应java类型为object
                        introspectedColumn.setFullyQualifiedJavaType(FullyQualifiedJavaType.getObjectInstance());
                        introspectedColumn.setJdbcTypeName("OTHER"); 
                        //生成警告信息
                        String warning = getString("Warning.14", 
                                Integer.toString(introspectedColumn.getJdbcType()),
                                entry.getKey().toString(),
                                introspectedColumn.getActualColumnName());
                        warnings.add(warning);
                    }
                }
            }
        }
    }
    
    //对列的属性进行替换
    private void applyColumnOverrides(TablesConfiguration tc,
            Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
            	//获取列覆盖信息
                ColumnOverride columnOverride = tc.getColumnOverride(introspectedColumn.getActualColumnName());
                if (columnOverride != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(getString("Tracing.4", introspectedColumn.getActualColumnName(), entry.getKey().toString()));
                    }
                    //设置java属性名
                    if (stringHasValue(columnOverride.getJavaProperty())) {
                        introspectedColumn.setJavaProperty(columnOverride.getJavaProperty());
                    }
                    //设置java类型
                    if (stringHasValue(columnOverride.getJavaType())) {
                        introspectedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType(columnOverride.getJavaType()));
                    }
                    //设置jdbc类型
                    if (stringHasValue(columnOverride.getJdbcType())) {
                        introspectedColumn.setJdbcTypeName(columnOverride.getJdbcType());
                    }
                    //设置类型处理器
                    if (stringHasValue(columnOverride.getTypeHandler())) {
                        introspectedColumn.setTypeHandler(columnOverride.getTypeHandler());
                    }
                    //设置区分大小写
                    if (columnOverride.isColumnNameDelimited()) {
                        introspectedColumn.setColumnNameDelimited(true);
                    }
                    //设置是否总是生成
                    introspectedColumn.setGeneratedAlways(columnOverride.isGeneratedAlways());
                    //设置其他属性
                    introspectedColumn.setProperties(columnOverride.getProperties());
                }
            }
        }
    }

    //------------------------------------------------核心逆向方法---------------------------------------------------

    //计算逆向表
    private List<IntrospectedTable> calculateIntrospectedTables(TablesConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {
        
    	boolean delimitIdentifiers = tc.isDelimitIdentifiers() 
    			|| stringContainsSpace(tc.getCatalog()) 
    			|| stringContainsSpace(tc.getSchema()) 
    			|| stringContainsSpace(tc.getTableName());
    	
        List<IntrospectedTable> answer = new ArrayList<IntrospectedTable>();
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            ActualTableName atn = entry.getKey();
            //生成全限定唯一表对象
            FullyQualifiedTable table = new FullyQualifiedTable(
                    stringHasValue(tc.getCatalog()) ? atn.getCatalog() : null,
                    stringHasValue(tc.getSchema()) ? atn.getSchema() : null,
                    atn.getTableName(),
                    tc.getDomainObjectName(),
                    tc.getAlias(),
                    isTrue(tc.getProperty(PropertyRegistry.TABLE_IGNORE_QUALIFIERS_AT_RUNTIME)),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_CATALOG),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_SCHEMA),
                    tc.getProperty(PropertyRegistry.TABLE_RUNTIME_TABLE_NAME),
                    delimitIdentifiers,
                    tc.getDomainObjectRenamingRule(),
                    context);
            //生成IntrospectedTable对象
            IntrospectedTable introspectedTable = ObjectFactory.createIntrospectedTable(tc, table, context);
            //添加所有逆向列到逆向表中
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                introspectedTable.addColumn(introspectedColumn);
            }
            //计算逆向表的主键
            calculatePrimaryKey(table, introspectedTable);
            //对逆向表进行增强
            enhanceIntrospectedTable(introspectedTable);
            answer.add(introspectedTable);
        }
        return answer;
    }
    
    //获取列名
    private Map<ActualTableName, List<IntrospectedColumn>> getColumns(TablesConfiguration tc) throws SQLException {
    	String localCatalog;
        String localSchema;
        String localTableName;
        
        //是否是唯一的标识符
        boolean delimitIdentifiers = tc.isDelimitIdentifiers()
                || stringContainsSpace(tc.getCatalog())
                || stringContainsSpace(tc.getSchema())
                || stringContainsSpace(tc.getTableName());
        
        //如果是唯一标识符则直接设值
        if (delimitIdentifiers) {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        //否则转换成小写
        } else if (databaseMetaData.storesLowerCaseIdentifiers()) {
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toLowerCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema().toLowerCase();
            localTableName = tc.getTableName() == null ? null : tc.getTableName().toLowerCase();
        //全部转换成大写
        } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
            localCatalog = tc.getCatalog() == null ? null : tc.getCatalog().toUpperCase();
            localSchema = tc.getSchema() == null ? null : tc.getSchema().toUpperCase();
            localTableName = tc.getTableName() == null ? null : tc.getTableName().toUpperCase();
        //否则的话还是设置原值
        } else {
            localCatalog = tc.getCatalog();
            localSchema = tc.getSchema();
            localTableName = tc.getTableName();
        }

        //是否启用通配符转义
        if (tc.isWildcardEscapingEnabled()) {
            String escapeString = databaseMetaData.getSearchStringEscape();
            StringBuilder sb = new StringBuilder();
            StringTokenizer st;
            //将localSchema中的"_"和"%"替换为数据库的通配符
            if (localSchema != null) {
                st = new StringTokenizer(localSchema, "_%", true); 
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.equals("_") || token.equals("%")) { 
                        sb.append(escapeString);
                    }
                    sb.append(token);
                }
                localSchema = sb.toString();
            }
            //将localTableName中的"_"和"%"替换为数据库的通配符
            sb.setLength(0);
            st = new StringTokenizer(localTableName, "_%", true); 
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals("_") || token.equals("%")) { 
                    sb.append(escapeString);
                }
                sb.append(token);
            }
            localTableName = sb.toString();
        }

        Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<ActualTableName, List<IntrospectedColumn>>();
        
        if (logger.isDebugEnabled()) {
            String fullTableName = composeFullyQualifiedTableName(localCatalog, localSchema, localTableName, '.');
            logger.debug(getString("Tracing.1", fullTableName)); 
        }
        
        ResultSet rs = databaseMetaData.getColumns(localCatalog, localSchema, localTableName, "%");
        
        boolean supportsIsAutoIncrement = false;
        boolean supportsIsGeneratedColumn = false;
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            if ("IS_AUTOINCREMENT".equals(rsmd.getColumnName(i))) { 
                supportsIsAutoIncrement = true;
            }
            if ("IS_GENERATEDCOLUMN".equals(rsmd.getColumnName(i))) { 
                supportsIsGeneratedColumn = true;
            }
        }
        while (rs.next()) {
            IntrospectedColumn introspectedColumn = ObjectFactory.createIntrospectedColumn(context);
            //设置表别名
            introspectedColumn.setTableAlias(tc.getAlias());
            //设置jdbc类型
            introspectedColumn.setJdbcType(rs.getInt("DATA_TYPE"));
            //设置列长度
            introspectedColumn.setLength(rs.getInt("COLUMN_SIZE"));
            //设置列名
            introspectedColumn.setActualColumnName(rs.getString("COLUMN_NAME"));
            //设置是否为空
            introspectedColumn.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
            //设置Scale
            introspectedColumn.setScale(rs.getInt("DECIMAL_DIGITS"));
            //设置Remarks
            introspectedColumn.setRemarks(rs.getString("REMARKS"));
            //设置默认值
            introspectedColumn.setDefaultValue(rs.getString("COLUMN_DEF")); 
            //设置是否是自增
            if (supportsIsAutoIncrement) {
                introspectedColumn.setAutoIncrement("YES".equals(rs.getString("IS_AUTOINCREMENT")));  
            }
            //设置是否被生成
            if (supportsIsGeneratedColumn) {
                introspectedColumn.setGeneratedColumn("YES".equals(rs.getString("IS_GENERATEDCOLUMN")));  
            }
            //获取表名包装类
            ActualTableName atn = new ActualTableName(
                    rs.getString("TABLE_CAT"),
                    rs.getString("TABLE_SCHEM"),
                    rs.getString("TABLE_NAME"));
            //以ActualTableName为键获取值
            List<IntrospectedColumn> columns = answer.get(atn);
            if (columns == null) {
                columns = new ArrayList<IntrospectedColumn>();
                answer.put(atn, columns);
            }
            columns.add(introspectedColumn);
            if (logger.isDebugEnabled()) {
                logger.debug(getString("Tracing.2", introspectedColumn.getActualColumnName(), 
                		Integer.toString(introspectedColumn.getJdbcType()), atn.toString()));
            }
        }

        closeResultSet(rs);
        
        //如果超过一个表就添加警告信息
        if (answer.size() > 1 && !stringContainsSQLWildcard(localSchema) && !stringContainsSQLWildcard(localTableName)) {
            ActualTableName inputAtn = new ActualTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName());
            StringBuilder sb = new StringBuilder();
            boolean comma = false;
            for (ActualTableName atn : answer.keySet()) {
                if (comma) {
                    sb.append(',');
                } else {
                    comma = true;
                }
                sb.append(atn.toString());
            }
            warnings.add(getString("Warning.25", inputAtn.toString(), sb.toString()));
        }
        return answer;
    }
    
    //-----------------------------------------------------------------------------------------------------------
    
    //增强逆向表
    private void enhanceIntrospectedTable(IntrospectedTable introspectedTable) {
        try {
            FullyQualifiedTable fqt = introspectedTable.getFullyQualifiedTable();
            ResultSet rs = databaseMetaData.getTables(fqt.getIntrospectedCatalog(), fqt.getIntrospectedSchema(),
                    fqt.getIntrospectedTableName(), null);
            //设置备注和表类型
            if (rs.next()) {
                String remarks = rs.getString("REMARKS"); 
                String tableType = rs.getString("TABLE_TYPE"); 
                introspectedTable.setRemarks(remarks);
                introspectedTable.setTableType(tableType);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            warnings.add(getString("Warning.27", e.getMessage())); 
        }
    }
    
    //计算并添加主键
    private void calculatePrimaryKey(FullyQualifiedTable table, IntrospectedTable introspectedTable) {
        ResultSet rs = null;
        try {
            rs = databaseMetaData.getPrimaryKeys(table.getIntrospectedCatalog(), 
                    table.getIntrospectedSchema(), table.getIntrospectedTableName());
        } catch (SQLException e) {
            closeResultSet(rs);
            warnings.add(getString("Warning.15")); 
            return;
        }
        try {
            Map<Short, String> keyColumns = new TreeMap<Short, String>();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME"); 
                short keySeq = rs.getShort("KEY_SEQ"); 
                keyColumns.put(keySeq, columnName);
            }
            for (String columnName : keyColumns.values()) {
                introspectedTable.addPrimaryKeyColumn(columnName);
            }
        } catch (SQLException e) {
            // ignore
        } finally {
            closeResultSet(rs);
        }
    }

    //报告逆向转换的警告信息
    private void reportIntrospectionWarnings(IntrospectedTable introspectedTable, TablesConfiguration tableConfiguration, FullyQualifiedTable table) {
        for (ColumnOverride columnOverride : tableConfiguration.getColumnOverrides()) {
            if (introspectedTable.getColumn(columnOverride.getColumnName()) == null) {
                warnings.add(getString("Warning.3", columnOverride.getColumnName(), table.toString()));
            }
        }
        for (String string : tableConfiguration.getIgnoredColumnsInError()) {
            warnings.add(getString("Warning.4", string, table.toString()));
        }
        GeneratedKey generatedKey = tableConfiguration.getGeneratedKey();
        if (generatedKey != null && introspectedTable.getColumn(generatedKey.getColumn()) == null) {
            if (generatedKey.isIdentity()) {
                warnings.add(getString("Warning.5", generatedKey.getColumn(), table.toString()));
            } else {
                warnings.add(getString("Warning.6", generatedKey.getColumn(), table.toString()));
            }
        }
        for (IntrospectedColumn ic : introspectedTable.getAllColumns()) {
            if (JavaReservedWords.containsWord(ic.getJavaProperty())) {
                warnings.add(getString("Warning.26", ic.getActualColumnName(), table.toString()));
            }
        }
    }
    
    //判断两个列是否匹配
    private boolean isMatchedColumn(IntrospectedColumn introspectedColumn, GeneratedKey gk) {
        if (introspectedColumn.isColumnNameDelimited()) {
            return introspectedColumn.getActualColumnName().equals(gk.getColumn());
        } else {
            return introspectedColumn.getActualColumnName().equalsIgnoreCase(gk.getColumn());
        }
    }
    
    //关闭ResultSet
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
    
}
