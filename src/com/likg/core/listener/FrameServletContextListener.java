package com.likg.core.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.likg.common.Constants;
import com.likg.core.context.FrameBeanFactory;

public class FrameServletContextListener extends ContextLoaderListener implements ServletContextListener, HttpSessionAttributeListener, HttpSessionListener {
	
	static Logger log = Logger.getLogger(FrameServletContextListener.class);
	
	@Override
	public synchronized void contextInitialized(ServletContextEvent event) {
		log.info("context init...");
		
		//此处必须调用父类的contextInitialized(event)方法，否则需在web.xml中单独配置ContextLoaderListener监听器。
		super.contextInitialized(event);
		
		ServletContext servletContext = event.getServletContext();
		
		//获取applicationContext对象，并设置到FrameBeanFactory对象中
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		FrameBeanFactory.setApplicationContext(applicationContext);
		
		//获取项目根目录路径
		String rootpath = servletContext.getRealPath("/");
		if(rootpath != null) {
			rootpath = rootpath.replaceAll("\\\\", "/");
		} else {
			rootpath = "/";
		}
		if(!rootpath.endsWith("/")) {
			rootpath = rootpath + "/";
		}
		Constants.ROOTPATH = rootpath;
		//获取应用上下文
		Constants.INITPATH = servletContext.getContextPath();
		
		//启动发布程序
		//new ReleaseArticle().start();
	}

	public void attributeAdded(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
}