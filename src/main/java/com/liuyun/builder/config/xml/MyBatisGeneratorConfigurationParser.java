package com.liuyun.builder.config.xml;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.liuyun.builder.config.Configuration;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.config.label.JDBCConnectionConfiguration;
import com.liuyun.builder.config.label.JavaMapperConfiguration;
import com.liuyun.builder.config.label.JavaModelConfiguration;
import com.liuyun.builder.config.label.TablesConfiguration;
import com.liuyun.builder.config.label.XmlMapperConfiguration;
import com.liuyun.builder.exception.XMLParserException;
import com.liuyun.builder.internal.ObjectFactory;

//配置解析器
public class MyBatisGeneratorConfigurationParser {
	
	//额外属性
    private Properties extraProperties;
    //配置属性
    private Properties configurationProperties;

    public MyBatisGeneratorConfigurationParser(Properties extraProperties) {
        super();
        if (extraProperties == null) {
            this.extraProperties = new Properties();
        } else {
            this.extraProperties = extraProperties;
        }
        configurationProperties = new Properties();
    }

    //解析配置文件
    public Configuration parseConfiguration(Element rootNode) throws XMLParserException {
        //新建Configuration对象
    	Configuration configuration = new Configuration();
    	//解析<generatorConfiguration>的子标签
        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("properties".equals(childNode.getNodeName())) { 
            	//解析<properties>
                parseProperties(configuration, childNode);
            } else if ("classPathEntry".equals(childNode.getNodeName())) {
            	//解析<classPathEntry>
                parseClassPathEntry(configuration, childNode);
            } else if ("context".equals(childNode.getNodeName())) {
            	//解析<context>
                parseContext(configuration, childNode);
            }
        }
        return configuration;
    }

    //解析properties标签配置
    protected void parseProperties(Configuration configuration, Node node) throws XMLParserException {
        Properties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource"); 
        String url = attributes.getProperty("url"); 
        if (!stringHasValue(resource) && !stringHasValue(url)) {
            throw new XMLParserException(getString("RuntimeError.14")); 
        }
        if (stringHasValue(resource) && stringHasValue(url)) {
            throw new XMLParserException(getString("RuntimeError.14")); 
        }
        URL resourceUrl;
        try {
            if (stringHasValue(resource)) {
            	//生成配置文件资源路径
                resourceUrl = ObjectFactory.getResource(resource);
                if (resourceUrl == null) {
                    throw new XMLParserException(getString("RuntimeError.15", resource)); 
                }
            } else {
                resourceUrl = new URL(url);
            }
            InputStream inputStream = resourceUrl.openConnection().getInputStream();
            //从资源文件中加载配置属性
            configurationProperties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            if (stringHasValue(resource)) {
                throw new XMLParserException(getString("RuntimeError.16", resource)); 
            } else {
                throw new XMLParserException(getString("RuntimeError.17", url)); 
            }
        }
    }

    //解析<context>
    private void parseContext(Configuration configuration, Node node) {
    	Context context = new Context();
    	configuration.addContext(context);
    	Properties attributes = parseAttributes(node);
    	//设置id属性
        String id = attributes.getProperty("id"); 
        context.setId(id);
        //解析context的子结点
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("jdbcConnection".equals(childNode.getNodeName())) {
            	//解析<jdbcConnection>
                parseJdbcConnection(context, childNode);
            } else if ("tables".equals(childNode.getNodeName())) {
            	//解析<tables>
                parseTables(context, childNode);
            }
        }
    }
    
    //解析<tables>
    protected void parseTables(Context context, Node node) {
        TablesConfiguration tc = new TablesConfiguration(context);
        context.addTableConfiguration(tc);
        Properties attributes = parseAttributes(node);
        //设置catalog
        String catalog = attributes.getProperty("catalog"); 
        if (stringHasValue(catalog)) {
            tc.setCatalog(catalog);
        }
        //设置schema
        String schema = attributes.getProperty("schema"); 
        if (stringHasValue(schema)) {
            tc.setSchema(schema);
        }
        //设置表名
        String tableName = attributes.getProperty("tableName"); 
        if (stringHasValue(tableName)) {
            tc.setTableName(tableName);
        }
        //设置生成路径
        String target = attributes.getProperty("target"); 
        if (stringHasValue(target)) {
            tc.setTableName(target);
        }
        //解析子结点
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("javaModel".equals(childNode.getNodeName())) { 
                parseJavaModel(tc, childNode);
            } else if ("javaMapper".equals(childNode.getNodeName())) { 
                parseJavaMapper(tc, childNode);
            } else if ("mapperXML".equals(childNode.getNodeName())) {
            	parseMapperXML(tc, childNode);
            }
        }
    }

    private void parseJavaModel(TablesConfiguration tc, Node node) {
    	JavaModelConfiguration javaModelConfiguration = new JavaModelConfiguration();
    	tc.setJavaModelConfiguration(javaModelConfiguration);
    	Properties attributes = parseAttributes(node);
    	javaModelConfiguration.setTarget(attributes.getProperty("target"));
	}
    
    private void parseJavaMapper(TablesConfiguration tc, Node node) {
    	JavaMapperConfiguration javaMapperConfiguration = new JavaMapperConfiguration();
    	tc.setJavaMapperConfiguration(javaMapperConfiguration);
    	Properties attributes = parseAttributes(node);
    	javaMapperConfiguration.setTarget(attributes.getProperty("target"));
	}

	private void parseMapperXML(TablesConfiguration tc, Node node) {
		XmlMapperConfiguration xmlMapperConfiguration = new XmlMapperConfiguration();
    	tc.setXmlMapperConfiguration(xmlMapperConfiguration);
    	Properties attributes = parseAttributes(node);
    	xmlMapperConfiguration.setTarget(attributes.getProperty("target"));
	}

	//解析jdbcConnection
    protected void parseJdbcConnection(Context context, Node node) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        Properties attributes = parseAttributes(node);
        //设置driverClass
        String driverClass = attributes.getProperty("driverClass");
        jdbcConnectionConfiguration.setDriverClass(driverClass);
        //设置connectionURL
        String connectionURL = attributes.getProperty("connectionURL"); 
        jdbcConnectionConfiguration.setConnectionURL(connectionURL);
        //设置userId
        String userId = attributes.getProperty("userId"); 
        if (stringHasValue(userId)) {
            jdbcConnectionConfiguration.setUserId(userId);
        }
        //设置password
        String password = attributes.getProperty("password"); 
        if (stringHasValue(password)) {
            jdbcConnectionConfiguration.setPassword(password);
        }
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
    }

    //解析<classPathEntry>
    protected void parseClassPathEntry(Configuration configuration, Node node) {
        Properties attributes = parseAttributes(node);
        configuration.addClasspathEntry(attributes.getProperty("location")); 
    }

    //将结点属性解析为Properties
    protected Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }
        return attributes;
    }

    //解析属性标记
    private String parsePropertyTokens(String string) {
        final String OPEN = "${";
        final String CLOSE = "}";
        String newString = string;
        if (newString != null) {
        	//获取开始标记的位置
            int start = newString.indexOf(OPEN);
            //获取结束标记的位置
            int end = newString.indexOf(CLOSE);
            while (start > -1 && end > start) {
            	//截取前缀
                String prepend = newString.substring(0, start);
                //截取后缀
                String append = newString.substring(end + CLOSE.length());
                //截取属性名
                String propName = newString.substring(start + OPEN.length(), end);
                //根据属性名解析属性内容
                String propValue = resolveProperty(propName);
                if (propValue != null) {
                    newString = prepend + propValue + append;
                }
                start = newString.indexOf(OPEN, end);
                end = newString.indexOf(CLOSE, end);
            }
        }
        return newString;
    }

    //根据属性名解析属性值
    private String resolveProperty(String key) {
        String property = null;
        //先获取系统属性
        property = System.getProperty(key);
        //再获取配置属性
        if (property == null) {
            property = configurationProperties.getProperty(key);
        }
        //再获取额外属性
        if (property == null) {
            property = extraProperties.getProperty(key);
        }
        return property;
    }
    
}
