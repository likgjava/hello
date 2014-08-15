package com.likg.auth.service;

import com.likg.auth.domain.Resource;
import com.likg.core.service.BaseTreeService;

public interface ResourceService extends BaseTreeService<Resource> {
	
	/**
	 * 保存资源对象
	 * @param Resource 资源对象
	 * @throws Exception
	 */
	void saveResource(Resource Resource) throws Exception;
	
	/**
	 * 根据父节点id获取xml格式的子节点列表数据
	 * @param parentId 父id
	 * @return XML格式的子节点数据
	 * @throws Exception
	 */
	String listChildrenXml(String parentId) throws Exception;

	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	void removeAll(String objId) throws Exception;

	/**
	 * 根据角色获取XML格式的资源树
	 * @param roleId 角色id
	 * @return XML格式的资源树
	 * @throws Exception
	 */
	String getTreeXmlByRole(String roleId) throws Exception;

	/**
	 * 为角色分配资源
	 * @param roleId 角色id
	 * @param resIds 资源ids
	 * @throws Exception
	 */
	void allotResource(String roleId, String[] resIds) throws Exception;
}
