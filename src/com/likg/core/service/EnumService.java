package com.likg.core.service;


public interface EnumService {

	/**
	 * 根据key获取指定枚举的信息
	 * @param enumName 枚举名称
	 * @param key 键
	 * @return
	 */
	String get(String enumName, String key);
	
}
