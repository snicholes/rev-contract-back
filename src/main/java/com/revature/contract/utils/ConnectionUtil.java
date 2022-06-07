package com.revature.contract.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectionUtil {
	public static Map<ConnectionType, PropKeys> propKeys;
	private static ConnectionUtil connUtil;
	private Properties props;
	private ConnectionType connType;
	
	static {
		propKeys = new HashMap<>();
		PropKeys dev = new PropKeys("url", "usr", "psw");
		PropKeys test = new PropKeys("test-url", "test-usr", "test-psw");
		propKeys.put(ConnectionType.DEV, dev);
		propKeys.put(ConnectionType.TEST, test);
		propKeys.put(ConnectionType.PROD, dev); // TODO
	}
	
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
	
	public static ConnectionUtil getConnectionUtil(ConnectionType option) {
		if (connUtil == null) {
			connUtil = new ConnectionUtil();
			connUtil.connType = option;
		}
		return connUtil;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(
					props.getProperty(propKeys.get(connType).url),
					props.getProperty(propKeys.get(connType).usr), 
					props.getProperty(propKeys.get(connType).psw));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}

class PropKeys {
	String url;
	String usr;
	String psw;
	
	PropKeys(String url, String usr, String psw) {
		this.url=url;
		this.usr=usr;
		this.psw=psw;
	}
}
