package com.likg.common;

public class Constants {
	
	/**项目根目录的绝对路径，在系统启动时被赋值(在FrameServletContextListener中)*/
	public static String ROOTPATH;
	
	/**应用的上下文，在系统启动时被赋值(在FrameServletContextListener中)*/
	public static String INITPATH;
	
	/** 标记spring mvc返回json视图 */
	public static final String JSON_VIEW = "jsonView";
	
	/** 标记操作成功 */
	public static final String SUCCESS = "success";
	
	/** 记录操作结果 */
	public static final String RESULT = "result";
	
	

}
