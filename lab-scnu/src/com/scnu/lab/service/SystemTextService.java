package com.scnu.lab.service;

public interface SystemTextService {

	/**
	 * 根据字符串name 找到系统字符串内容
	 * @param name
	 * @return
	 */
	public String findFirstSystemTextByName(String name);
}
