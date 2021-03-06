package com.likg.core.service;

import java.util.List;

import com.likg.common.page.Page;
import com.likg.core.dao.hibernate.query.QueryObject;

public interface BaseGenericService<T> {

	/**
	 * 根据id获取对象信息
	 * @param objId 记录号
	 * @return
	 */
	T get(String objId);
	
	/**
	 * 获取全部列表数据
	 * @return
	 */
	List<T> getAll();
	
	/**
	 * 保存对象信息
	 * @param object 要保存的对象
	 * @return 保存后的对象
	 */
	T save(T object);
	
	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 */
	void remove(String objId);
	
	/**
	 * 根据id数组删除多个对象
	 * @param objIds 对象id数组
	 */
	void remove(String[] objIds);
	
	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 */
	void remove(String objId, Class<T> persistClass);
	
	/**
	 * 根据id数组删除多个对象
	 * @param objIds 对象id数组
	 */
	void remove(String[] objIds, Class<T> persistClass);
	
	/**
	 * 根据QueryObject查询分页列表数据
	 * @param queryObject 查询对象
	 * @param cacheable 是否使用缓存
	 * @param start 本页开始记录索引
	 * @param pageSize 每页显示的记录数
	 * @return
	 */
	Page<T> findByQuery(QueryObject<T> queryObject, boolean cacheable, int start, int pageSize);
	
	/**
	 * 根据queryObject获取所有满足条件的数据列表
	 * @param queryObject 查询对象
	 * @return
	 */
	List<T> findListByQuery(QueryObject<T> queryObject);

}
