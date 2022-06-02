package com.revature.contract.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
	private static ConnectionUtil connUtil;
	private Properties props;
	
	private ConnectionUtil() {
		InputStream propsFile = this.getClass()
				.getClassLoader().getResourceAsStream("database.properties");
		props = new Properties();
		try {
			props.load(propsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ConnectionUtil getConnectionUtil() {
		if (connUtil == null)
			connUtil = new ConnectionUtil();
		return connUtil;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					props.getProperty("url"),
					props.getProperty("usr"), 
					props.getProperty("psw"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
