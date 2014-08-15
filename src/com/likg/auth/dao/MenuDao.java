package com.likg.auth.dao;

import java.util.List;

import com.likg.auth.domain.Menu;
import com.likg.core.dao.BaseTreeDao;

public interface MenuDao extends BaseTreeDao<Menu> {

	/**
	 * 根据用户获取菜单列表
	 * @param userId 用户id
	 * @param queryType 查询类型（all：查询全部;firstLevel:查询一级菜单；byParentId:查询指定节点下的所有子节点）
	 * @param parentId 父节点id
	 * @return
	 * @throws Exception
	 */
	List<Menu> getMenuListByUser(String userId, String queryType, String parentId) throws Exception;

}
