package com.likg.core.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.likg.common.page.Page;
import com.likg.core.context.FrameMessageSource;
import com.likg.core.dao.BaseGenericDao;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.service.BaseGenericService;

@Service
public class BaseGenericServiceImpl<T> implements BaseGenericService<T> {
	
	@Resource
	private BaseGenericDao<T> baseGenericDaoHibernate;
	
	@Resource(name="frameMessageSource")
	protected FrameMessageSource messageSource;
	
	/**
	 * 根据泛型获取持久化对象的类型
	 * @return 持久化对象的类型
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getPersistClass() {
		if(super.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			return (Class) ((ParameterizedType)super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return null;
	}
	
	/**
	 * 根据id获取对象信息
	 * @param objId 记录号
	 * @return
	 */
	public T get(String objId) {
		return baseGenericDaoHibernate.get(objId, this.getPersistClass());
	}
	
	/**
	 * 获取全部列表数据
	 * @return
	 */
	public List<T> getAll() {
		return baseGenericDaoHibernate.getAll(this.getPersistClass());
	}
	
	/**
	 * 保存对象信息
	 * @param object 要保存的对象
	 * @return 保存后的对象
	 */
	public T save(T object) {
		return baseGenericDaoHibernate.save(object);
	}
	
	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 */
	public void remove(String objId) {
		this.remove(objId, this.getPersistClass());
	}

	/**
	 * 根据id数组删除多个对象
	 * @param objIds 对象id数组
	 */
	public void remove(String[] objIds) {
		this.remove(objIds, this.getPersistClass());
	}
	
	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 */
	public void remove(String objId, Class<T> persistClass) {
		baseGenericDaoHibernate.remove(objId, persistClass);
	}
	
	/**
	 * 根据id数组删除多个对象
	 * @param objIds 对象id数组
	 */
	public void remove(String[] objIds, Class<T> persistClass) {
		baseGenericDaoHibernate.remove(objIds, persistClass);
	}
	
	/**
	 * 根据QueryObject查询分页列表数据
	 * @param queryObject 查询对象
	 * @param cacheable 是否使用缓存
	 * @param start 本页开始记录索引
	 * @param pageSize 每页显示的记录数
	 * @return
	 */
	public Page<T> findByQuery(QueryObject<T> queryObject, boolean cacheable, int start, int pageSize) {
    	return baseGenericDaoHibernate.findByQuery(queryObject, cacheable, start, pageSize);
    }

	/**
	 * 根据queryObject获取所有满足条件的数据列表
	 * @param queryObject 查询对象
	 * @return
	 */
	public List<T> findListByQuery(QueryObject<T> queryObject) {
		return baseGenericDaoHibernate.findListByQuery(queryObject);
	}

	
}
