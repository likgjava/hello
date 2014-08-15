package com.likg.core.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础对象接口，系统中所有实体类都要实现该接口
 * @author likg
 */
public interface BaseObject extends Serializable {

	/**
	 * 获取记录号
	 * @return 记录号
	 */
	String getObjId();
	
	/**
	 * 设置对象记录号
	 * @param objId 记录号
	 */
	void setObjId(String objId);
	
	/**
	 * 获取对象创建时间
	 * @return 对象创建时间
	 */
	Date getCreateTime();
	
	
	/**
	 * 设置对象创建时间
	 * @param createTime 对象创建时间
	 */
	void setCreateTime(Date createTime);
}
