package com.scnu.lab.general.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {

	/**
	 * 读取resources/conifg.properties 配置文件
	 */
	private static final String CONFIG_PATH="config.properties";
	private static Logger logger=Logger.getLogger(ConfigUtil.class);
	
	/**
	 * 根据key值，获取property值
	 * @param key
	 * @return
	 */
	public static String readPropertyValue(String key){
		Properties pro=new Properties();
		InputStream is=ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
		try {
			pro.load(is);
			return pro.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
