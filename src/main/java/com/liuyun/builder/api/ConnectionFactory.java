package com.liuyun.builder.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

//连接工厂
public interface ConnectionFactory {
	
	//获取连接
    Connection getConnection() throws SQLException;
    
    //添加配置属性
    void addConfigurationProperties(Properties properties);
    
}
