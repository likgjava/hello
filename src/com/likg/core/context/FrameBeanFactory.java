package com.likg.core.context;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

public class FrameBeanFactory {

	private static ApplicationContext context;
	
	/**
	 * 获取ApplicationContext对象
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * 设置applicationContext对象（系统启动的时候进行设置，在FrameServletContextListener中）
	 * @param applicationContext
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		if (context == null) {
			context = applicationContext;
		}
	}

	/**
	 * 根据bean名称获取对象
	 * @param beanName 名称
	 * @return
	 */
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/**
	 * 根据bean类型获取对象
	 * @param clazz 类型
	 * @return
	 */
	public static Object getBean(Class<?> clazz) {
		return context.getBeansOfType(clazz);
	}

	public static Object getBean(Class<?> clazz, boolean includeNonSingletons, boolean allowEagerInit) {
		return BeanFactoryUtils.beanOfType(context, clazz, includeNonSingletons, allowEagerInit);
	}

	
}
