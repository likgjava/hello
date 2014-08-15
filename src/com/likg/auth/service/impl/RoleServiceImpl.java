package com.likg.auth.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.likg.auth.dao.RoleDao;
import com.likg.auth.domain.Role;
import com.likg.auth.service.RoleService;
import com.likg.core.service.impl.BaseGenericServiceImpl;

@Service
public class RoleServiceImpl extends BaseGenericServiceImpl<Role> implements RoleService {
	
	@Resource(name="roleDaoHibernate")
	private RoleDao roleDaoHibernate;

	
	/**
	 * 保存角色信息
	 * @param role 角色对象
	 * @throws Exception
	 */
	public void saveRole(Role role) throws Exception {
		//新增
		if(StringUtils.isBlank(role.getObjId())) {
			roleDaoHibernate.save(role);
		}
		//修改
		else {
			Role old = roleDaoHibernate.get(role.getObjId());
			old.setRoleName(role.getRoleName());
			old.setRoleChName(role.getRoleChName());
			old.setRoleDesc(role.getRoleDesc());
			roleDaoHibernate.save(old);
		}
	}

	/**
	 * 获取指定用户的角色列表数据
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleListByUser(String userId) throws Exception {
		return roleDaoHibernate.getRoleListByUser(userId);
	}

	/**
	 * 分配角色
	 * @param userId 用户id
	 * @param roleIds 角色ids
	 * @throws Exception
	 */
	public void allotRole(String userId, String[] roleIds) throws Exception {
		roleDaoHibernate.allotRole(userId, roleIds);
	}

	

	
	

}
