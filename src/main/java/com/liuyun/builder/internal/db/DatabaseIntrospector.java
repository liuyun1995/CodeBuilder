package com.liuyun.builder.internal.db;

import static com.liuyun.builder.internal.utils.JavaBeansUtil.getCamelCaseString;
import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
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
import java.util.TreeMap;

import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.JavaTypeResolver;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.JavaReservedWords;
import com.liuyun.builder.config.GeneratedKey;
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
    	//根据配置获取表和列的映射集合
        Map<ActualTableName, List<IntrospectedColumn>> columns = getColumns(tc);
        if (columns.isEmpty()) {
            warnings.add(getString("Warning.19", tc.getCatalog(), tc.getSchema(), tc.getTableName()));
            return null;
        }
        //执行类型转换
        executeTypeConvert(tc, columns);
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

    //执行类型转换
    private void executeTypeConvert(TablesConfiguration tc, Map<ActualTableName, List<IntrospectedColumn>> columns) {
        for (Map.Entry<ActualTableName, List<IntrospectedColumn>> entry : columns.entrySet()) {
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
            	
            	//转换成驼峰命名
                introspectedColumn.setJavaProperty(getCamelCaseString(introspectedColumn.getActualColumnName(), false));

                //获取该列对应的java类型(使用传入的类型转换器)
                FullyQualifiedJavaType fullyQualifiedJavaType = javaTypeResolver.calculateJavaType(introspectedColumn);
                
                if (fullyQualifiedJavaType != null) {
                    introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    introspectedColumn.setJdbcTypeName(javaTypeResolver.calculateJdbcTypeName(introspectedColumn));
                } else {
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
                    atn.getCatalog(),
                    atn.getSchema(),
                    atn.getTableName(),
                    tc.getDomainObjectName(),
                    tc.getAlias(),
                    delimitIdentifiers,
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
    	String localCatalog = tc.getCatalog();
        String localSchema = tc.getSchema();
        String localTableName = tc.getTableName();
        Map<ActualTableName, List<IntrospectedColumn>> answer = new HashMap<ActualTableName, List<IntrospectedColumn>>();
        
        if (logger.isDebugEnabled()) {
            String fullTableName = composeFullyQualifiedTableName(localCatalog, localSchema, localTableName, '.');
            logger.debug(getString("Tracing.1", fullTableName)); 
        }
        
        ResultSet rs = databaseMetaData.getColumns(null, "%", localTableName.toUpperCase(), "%");
        
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
                    table.getIntrospectedSchema(), table.getIntrospectedTableName().toUpperCase());
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
