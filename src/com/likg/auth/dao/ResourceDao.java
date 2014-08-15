package com.likg.auth.dao;

import java.util.List;

import com.likg.auth.domain.Resource;
import com.likg.core.dao.BaseTreeDao;

public interface ResourceDao extends BaseTreeDao<Resource> {

	/**
	 * 根据角色获取资源id列表
	 * @param roleId 角色id
	 * @return
	 */
	List<String> getResIdListByRole(String roleId) throws Exception;

	/**
	 * 为角色分配资源
	 * @param roleId 角色id
	 * @param resIds 资源ids
	 * @throws Exception
	 */
	void allotResource(String roleId, String[] resIds) throws Exception;

}
