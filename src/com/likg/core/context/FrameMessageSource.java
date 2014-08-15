package com.likg.core.context;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * 国际化资源文件工具类，自动从spring里获取了local <br/>
 * Controller、Service、Dao中已经在基类中注入了，可以直接使用。
 * @author likg
 */
@Repository
public class FrameMessageSource implements InitializingBean {
	
	/** 自己的bean的名字 */
	public static final String BEAN_NAME = "frameMessageSource";
	
	@Resource(name="resourceBundleMessageSource")
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * 获取资源文件
	 * @param messagesKey 键 <br/>
	 * 	例如：system.properties文件中定义了lucenePath=/lucene/index <br/>
	 * 	java文件中使用messageSource.getMessage("lucenePath");返回/lucene/index
	 * @return
	 */
	public String getMessage(String messagesKey){
		return messageSource.getMessage(messagesKey, null, "undefined", LocaleContextHolder.getLocale());
	}
	
	/** 
	 * Description :  获取资源文件
	 * Create Date: 2009-12-8下午05:27:01 by wangsw  Modified Date: 2009-12-8下午05:27:01 by wangsw
	 * @param   key 参数数组</br>  
	 * 			例如:mail.properties文件mail.to=the mail addreass is {0}   </br>
	 * 			java文件:messageSource.getMessage("mail", new Object["sinlff@13.com"])   </br>  
	 * 			返回the mail addreass is sinlff@13.com
	 * @return  匹配的资源
	 * @Exception   
	 */
	public String getMessage(String messagesKey, Object[] args){
		return messageSource.getMessage(messagesKey, args, "undefined", LocaleContextHolder.getLocale());
	}
	
	/** 
	 * Description :  获取资源文件
	 * Create Date: 2009-12-8下午05:27:01 by wangsw  Modified Date: 2009-12-8下午05:27:01 by wangsw
	 * @param   key 找不到的默认值</br>  
	 * 			例如:mail.properties文件mail.to=the mail addreass is    </br>
	 * 			java文件:messageSource.getMessage("mail", '替换值')   </br>  
	 * 			找到返回 the mail addreass is 
	 * 	        找不到返回 替换值
	 * @return  匹配的资源
	 * @Exception   
	 */
	public String getMessage(String messagesKey, String defaultMessage){
		return messageSource.getMessage(messagesKey, null, defaultMessage, LocaleContextHolder.getLocale());
	}
	
	/** 
	 * Description :  获取资源文件
	 * Create Date: 2009-12-8下午05:27:01 by wangsw  Modified Date: 2009-12-8下午05:27:01 by wangsw
	 * @param   key 参数数组 找不到的默认值</br>  
	 * 			例如:mail.properties文件mail.to=the mail addreass is {0}   </br>
	 * 			java文件:messageSource.getMessage("mail", new Object["sinlff@13.com"])   </br>  
	 * 			返回the mail addreass is sinlff@13.com
	 * @return  匹配的资源
	 * @Exception   
	 */
	public String getMessage(String messagesKey, Object[] args, String defaultMessage){
		return messageSource.getMessage(messagesKey, args, defaultMessage, LocaleContextHolder.getLocale());
	}
	
	/** 
	 * Description :  获取资源文件
	 * Create Date: 2009-12-8下午05:27:01 by wangsw  Modified Date: 2009-12-8下午05:27:01 by wangsw
	 * @param   key 参数数组 找不到的默认值 自定义强制指定的框架之外的Locale</br>  
	 * 			例如:mail.properties文件mail.to=the mail addreass is {0}   </br>
	 * 			java文件:messageSource.getMessage("mail", new Object["sinlff@13.com"])   </br>  
	 * 			返回the mail addreass is sinlff@13.com
	 * @return  匹配的资源
	 * @Exception   
	 */
	public String getMessage(String messagesKey, Object[] args, String defaultMessage, Locale locale){
		return messageSource.getMessage(messagesKey, args, defaultMessage, locale);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(messageSource, "the messageSource must be not null.");
	}
	
}
