package com.liuyun.builder.internal;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import com.liuyun.builder.api.ConnectionFactory;
import com.liuyun.builder.config.label.JDBCConnectionConfiguration;

//JDBC连接工厂
public class JDBCConnectionFactory implements ConnectionFactory {

    private String userId;
    private String password;
    private String connectionURL;
    private String driverClass;
    private Properties otherProperties;
    
    public JDBCConnectionFactory(JDBCConnectionConfiguration config) {
        super();
        userId = config.getUserId();
        password = config.getPassword();
        connectionURL = config.getConnectionURL();
        driverClass = config.getDriverClass();
        otherProperties = config.getProperties();
    }
    
    public JDBCConnectionFactory() {
        super();
    }

    //获取连接
    @Override
    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        if (stringHasValue(userId)) {
            props.setProperty("user", userId); 
        }
        if (stringHasValue(password)) {
            props.setProperty("password", password); 
        }
        props.putAll(otherProperties);
        Driver driver = getDriver();
        Connection conn = driver.connect(connectionURL, props);
        if (conn == null) {
            throw new SQLException(getString("RuntimeError.7")); 
        }
        return conn;
    }

    private Driver getDriver() {
        Driver driver;
        try {
            Class<?> clazz = ObjectFactory.externalClassForName(driverClass);
            driver = (Driver) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.8"), e); 
        }
        return driver;
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        userId = properties.getProperty("userId"); 
        password = properties.getProperty("password"); 
        connectionURL = properties.getProperty("connectionURL"); 
        driverClass = properties.getProperty("driverClass"); 

        otherProperties = new Properties();
        otherProperties.putAll(properties);
        
        otherProperties.remove("userId"); 
        otherProperties.remove("password"); 
        otherProperties.remove("connectionURL"); 
        otherProperties.remove("driverClass"); 
    }
}
