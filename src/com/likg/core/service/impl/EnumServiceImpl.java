package com.likg.core.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.likg.core.context.FrameMessageSource;
import com.likg.core.service.EnumService;

/**
 * 获取在资源文件中定义的枚举<br/>
 * 枚举在资源文件中的定义格式为：auditStatus=00:未审核;01:审核中;02:审核通过;03:审核不通过<br/>
 * 例如，如果获取auditStatus的01所对应的值，则可以通过enumService.get("auditStatus", "01");获得“审核中”
 * @author likg
 */
@Service
public class EnumServiceImpl implements EnumService {
	
	private static Logger log = Logger.getLogger(EnumServiceImpl.class);
	
	/**自己的bean对象名称*/
	public static final String BEAN_NAME = "enumServiceImpl";
	
	@Resource(name="frameMessageSource")
	protected FrameMessageSource messageSource;

	/**
	 * 根据key获取指定枚举的信息
	 * @param enumName 枚举名称
	 * @param key 键
	 * @return
	 */
	public String get(String enumName, String key) {
		//枚举名称不能为空
		if(StringUtils.isBlank(enumName)) {
			log.error("the enumName cannot be empty!");
			return null;
		}
		
		//获取枚举字符串
		String enumValues = messageSource.getMessage(enumName);
		
		//枚举字符串不能为空
		if(StringUtils.isBlank(enumValues)) {
			log.error("the enum value is empty of the [" + enumName + "]");
			return null;
		}
		
		//拆分枚举字符串
		String[] enumList = enumValues.split(";");
		for(String enums : enumList) {
			String[] keyAndValue = enums.split(":");
			//枚举格式定义不正确
			if(keyAndValue.length <= 2) {
				log.error("Enumeration format definition is not correct --> [" + enumValues + "]");
				return null;
			} else if(keyAndValue[0].equals(key)) {
				return keyAndValue[1];
			}
		}
		
		return null;
	}
	

}
