package com.likg.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.likg.core.dao.BaseTreeDao;
import com.likg.core.service.BaseTreeService;

@Service
public class BaseTreeServiceImpl<T> extends BaseGenericServiceImpl<T> implements BaseTreeService<T> {
	
	@Resource(name="baseTreeDaoHibernate")
	private BaseTreeDao<T> baseTreeDaoHibernate;
	
	/**
	 * 获取节点对象，包含所有祖先节点对象信息
	 * @param objId 节点id
	 * @return
	 * @throws Exception
	 */
	public T getTreeObject(String objId) throws Exception {
		return baseTreeDaoHibernate.getTreeObject(objId, this.getPersistClass());
	}
	
	/**
	 * 获取当前节点的所有父节点，从上到下顺序排列，包含当前节点
	 * @param objId 当前节点id
	 * @return
	 * @throws Exception
	 */
	public List<T> getParentList(String objId) throws Exception {
		return baseTreeDaoHibernate.getParentList(objId, this.getPersistClass());
	}

	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param objId 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 * @throws Exception
	 */
	public List<T> listChildrenById(String objId) throws Exception {
		return baseTreeDaoHibernate.listChildrenById(objId, this.getPersistClass());
	}

	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	public void removeAll(String objId) throws Exception {
		baseTreeDaoHibernate.removeAll(objId, this.getPersistClass());
	}

	
	

}
