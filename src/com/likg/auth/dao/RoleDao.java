package com.likg.auth.dao;

import java.util.List;

import com.likg.auth.domain.Role;
import com.likg.core.dao.BaseGenericDao;

public interface RoleDao extends BaseGenericDao<Role> {
	
	/**
	 * 根据角色名称获取角色对象信息
	 * @param roleName 角色英文名称
	 * @return
	 * @throws Exception
	 */
	Role getRoleByName(String roleName) throws Exception;

	/**
	 * 获取指定用户的角色列表数据
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	List<Role> getRoleListByUser(String userId) throws Exception;

	/**
	 * 分配角色
	 * @param userId 用户id
	 * @param roleIds 角色ids
	 * @throws Exception
	 */
	void allotRole(String userId, String[] roleIds) throws Exception;

}
