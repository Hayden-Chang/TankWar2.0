package com;

import java.io.IOException;
import java.util.Properties;

public class PropertiesManag {
	static Properties prop= new Properties();
	static {
		try {//配置文件中不能有空格
			prop.load(PropertiesManag.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String getValue(String key) {
		return prop.getProperty(key);
	}
	
}
