package com.liuyun.builder.config.label;

import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.liuyun.builder.api.ConnectionFactory;
import com.liuyun.builder.api.GeneratedJavaFile;
import com.liuyun.builder.api.GeneratedXmlFile;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.JavaTypeResolver;
import com.liuyun.builder.api.Plugin;
import com.liuyun.builder.api.ProgressCallback;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.PropertyHolder;
import com.liuyun.builder.config.PropertyRegistry;
import com.liuyun.builder.internal.JDBCConnectionFactory;
import com.liuyun.builder.internal.PluginAggregator;
import com.liuyun.builder.internal.db.DatabaseIntrospector;

//context标签配置
public class Context extends PropertyHolder {
	
    private String id;
    
    private String tableName;
    
    private String target;
    
    /*子结点*/
    private JDBCConnectionConfiguration jdbcConnectionConfiguration;
    
    private PluginAggregator pluginAggregator;
    
    private PluginConfiguration pluginConfiguration;

    private ArrayList<TablesConfiguration> tablesConfigurations;

    private String beginningDelimiter = "\"";

    private String endingDelimiter = "\"";

    public Context() {
        super();
        tablesConfigurations = new ArrayList<TablesConfiguration>();
    }
    
    public Plugin getPlugins() {
        return pluginAggregator;
    }

	public void addTableConfiguration(TablesConfiguration tc) {
    	tablesConfigurations.add(tc);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JDBCConnectionConfiguration getJdbcConnectionConfiguration() {
        return jdbcConnectionConfiguration;
    }
    
    public void setJdbcConnectionConfiguration(JDBCConnectionConfiguration jdbcConnectionConfiguration) {
        this.jdbcConnectionConfiguration = jdbcConnectionConfiguration;
    }
    
    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("context"); 
        xmlElement.addAttribute(new Attribute("id", id)); 
        addPropertyXmlElements(xmlElement);
        if (jdbcConnectionConfiguration != null) {
            xmlElement.addElement(jdbcConnectionConfiguration.toXmlElement());
        }
        for (TablesConfiguration tablesConfiguration : tablesConfigurations) {
            xmlElement.addElement(tablesConfiguration.toXmlElement());
        }
        return xmlElement;
    }

    public List<TablesConfiguration> getTableConfigurations() {
        return tablesConfigurations;
    }

    public String getBeginningDelimiter() {
        return beginningDelimiter;
    }

    public String getEndingDelimiter() {
        return endingDelimiter;
    }

    @Override
    public void addProperty(String name, String value) {
        super.addProperty(name, value);
        if (PropertyRegistry.CONTEXT_BEGINNING_DELIMITER.equals(name)) {
            beginningDelimiter = value;
        } else if (PropertyRegistry.CONTEXT_ENDING_DELIMITER.equals(name)) {
            endingDelimiter = value;
        }
    }
    
    //所有要生成的表
    private List<IntrospectedTable> introspectedTables;

    public int getIntrospectionSteps() {
        int steps = 0;
        steps++;
        steps += tablesConfigurations.size() * 1;
        return steps;
    }

    //根据配置对表进行逆向
    public void introspectTables(ProgressCallback callback, List<String> warnings, 
    		Set<String> fullyQualifiedTableNames) throws SQLException, InterruptedException {
        introspectedTables = new ArrayList<IntrospectedTable>();
        //获取JavaType转换器
//      JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this, warnings);
        JavaTypeResolver javaTypeResolver = null;
        Connection connection = null;
        try {
            callback.startTask(getString("Progress.0"));
            //获取数据库连接
            connection = getConnection();
            //获取数据库逆向
            DatabaseIntrospector databaseIntrospector = new DatabaseIntrospector(this, connection.getMetaData(), javaTypeResolver, warnings);
            //对context下的所有table配置进行处理
            for (TablesConfiguration tc : tablesConfigurations) {
            	//获取唯一限定表名
                String tableName = composeFullyQualifiedTableName(tc.getCatalog(), tc.getSchema(), tc.getTableName(), '.');
                //验证该表是否需要生成
                if (fullyQualifiedTableNames != null && fullyQualifiedTableNames.size() > 0 && !fullyQualifiedTableNames.contains(tableName)) {
                    continue;
                }
                //设置回调状态为：开始执行任务
                callback.startTask(getString("Progress.1", tableName)); 
                //获取IntrospectedTable集合
                List<IntrospectedTable> tables = databaseIntrospector.introspectTables(tc);
                if (tables != null) {
                    introspectedTables.addAll(tables);
                }
                //调用回调器检查是否需要取消操作
                callback.checkCancel();
            }
        } finally {
            closeConnection(connection);
        }
    }

    public int getGenerationSteps() {
        int steps = 0;
        if (introspectedTables != null) {
            for (IntrospectedTable introspectedTable : introspectedTables) {
                steps += introspectedTable.getGenerationSteps();
            }
        }
        return steps;
    }

    //生成代码文件
    public void generateFiles(ProgressCallback callback,
            List<GeneratedJavaFile> generatedJavaFiles,
            List<GeneratedXmlFile> generatedXmlFiles, List<String> warnings) throws InterruptedException {
        //如果逆向表集合不为空
        if (introspectedTables != null) {
            for (IntrospectedTable introspectedTable : introspectedTables) {
            	//检查是否要取消操作
                callback.checkCancel();
                //对逆向表进行初始化
                introspectedTable.initialize();
                //生成所有的生成器
                introspectedTable.calculateGenerators(warnings, callback);
                //添加要生成的java文件
                generatedJavaFiles.addAll(introspectedTable.getGeneratedJavaFiles());
                //添加要生成的xml文件
                generatedXmlFiles.addAll(introspectedTable.getGeneratedXmlFiles());
            }
        }
    }

    //获取连接
    private Connection getConnection() throws SQLException {
        ConnectionFactory connectionFactory;
        connectionFactory = new JDBCConnectionFactory(jdbcConnectionConfiguration);
        return connectionFactory.getConnection();
    }

    //关闭连接
    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
    
    //验证方法
    public void validate(List<String> errors) {
        if (!stringHasValue(id)) {
            errors.add(getString("ValidationError.16")); 
        }
        if (jdbcConnectionConfiguration != null) {
            jdbcConnectionConfiguration.validate(errors);
        }
        if (tablesConfigurations.size() == 0) {
            errors.add(getString("ValidationError.3", id)); 
        } else {
            for (int i = 0; i < tablesConfigurations.size(); i++) {
                TablesConfiguration tc = tablesConfigurations.get(i);
                tc.validate(errors, i);
            }
        }
    }
    
}
