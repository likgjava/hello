package com.likg.auth.service;

import java.util.List;

import com.likg.auth.domain.Menu;
import com.likg.core.service.BaseTreeService;

public interface MenuService extends BaseTreeService<Menu> {
	
	/**
	 * 保存菜单对象
	 * @param menu 菜单对象
	 * @throws Exception
	 */
	void saveMenu(Menu menu) throws Exception;
	
	/**
	 * 根据节点id获取xml格式的子节点列表数据
	 * @param objId 节点id
	 * @return XML格式的子节点数据
	 * @throws Exception
	 */
	String listChildrenXml(String objId) throws Exception;

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
