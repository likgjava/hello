package com.likg.auth.service;

import java.util.List;

import com.likg.auth.domain.Role;
import com.likg.core.service.BaseGenericService;

public interface RoleService extends BaseGenericService<Role> {

	/**
	 * 保存角色信息
	 * @param role 角色对象
	 * @throws Exception
	 */
	void saveRole(Role role) throws Exception;

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
