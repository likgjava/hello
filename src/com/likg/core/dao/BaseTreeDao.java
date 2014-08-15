package com.likg.core.dao;

import java.util.List;

public interface BaseTreeDao<T> extends BaseGenericDao<T> {
	
	/**
	 * 获取节点对象，包含所有祖先节点对象信息
	 * @param objId 节点id
	 * @return
	 * @throws Exception
	 */
	T getTreeObject(String objId) throws Exception;
	
	/**
	 * 获取节点对象，包含所有祖先节点对象信息
	 * @param objId 节点id
	 * @return
	 * @throws Exception
	 */
	T getTreeObject(String objId, Class<T> persistClass) throws Exception;
	
	/**
	 * 获取当前节点的所有父节点，从上到下顺序排列，包含当前节点
	 * @param objId 当前节点id
	 * @return
	 * @throws Exception
	 */
	List<T> getParentList(String objId) throws Exception;
	
	/**
	 * 获取当前节点的所有父节点，从上到下顺序排列，包含当前节点
	 * @param objId 当前节点id
	 * @param persistClass 实体类类型，在service层调用该方法时，需要指定类型
	 * @return
	 * @throws Exception
	 */
	List<T> getParentList(String objId, Class<T> persistClass) throws Exception;

	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param objId 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 * @throws Exception
	 */
	List<T> listChildrenById(String objId) throws Exception;
	
	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param objId 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 * @throws Exception
	 */
	List<T> listChildrenById(String objId, Class<T> persistClass) throws Exception;

	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	void removeAll(String objId) throws Exception;
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	void removeAll(String objId, Class<T> persistClass) throws Exception;

	

}
