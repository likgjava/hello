package com.likg.auth.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.likg.auth.dao.RoleDao;
import com.likg.auth.dao.UserDao;
import com.likg.auth.domain.Role;
import com.likg.auth.domain.User;
import com.likg.auth.service.UserService;
import com.likg.common.page.Page;
import com.likg.core.service.impl.BaseGenericServiceImpl;

@Service
public class UserServiceImpl extends BaseGenericServiceImpl<User> implements UserService {
	
	@Resource
	@Qualifier("userDaoHibernate")
	private UserDao userDaoHibernate;
	
	@Resource(name="roleDaoHibernate")
	private RoleDao roleDaoHibernate;

	/**
	 * 保存注册用户信息
	 * @param user 用户对象
	 * @throws Exception
	 */
	public void saveRegisterUser(User user) throws Exception {
		//为新注册用户分配‘普通会员’角色
		Role role = roleDaoHibernate.getRoleByName("member");
		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		user.setRoles(roles);
		
		userDaoHibernate.save(user);
	}
	
	/**
	 * 保存用户信息
	 * @param user 用户对象
	 * @throws Exception
	 */
	public void saveUser(User user) throws Exception {
		//新增
		if(StringUtils.isBlank(user.getObjId())) {
			userDaoHibernate.save(user);
		}
		//修改
		else {
			User oldUser = userDaoHibernate.get(user.getObjId());
			oldUser.setUserName(user.getUserName());
			oldUser.setEmail(user.getEmail());
			userDaoHibernate.save(oldUser);
		}
	}

	
	/** 
	 * 根据用户名获取用户对象
	 * @param   userName 用户名
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	public User getUserByUserName(String userName) throws Exception {
		return userDaoHibernate.getUserByUserName(userName);
	}

	/** 
	 * 验证用户名是否存在
	 * @param   userName 要检查的用户名
	 * @param   ignoreObjId 忽略验证的userId
	 * @return  
	 * @Exception   
	 */
	public boolean userNameExist(String userName, String ignoreObjId) throws Exception {
		boolean isExist = true;
		User user = userDaoHibernate.getUserByUserName(userName);
		if(user == null) {
			isExist = false;
		}
		else if(StringUtils.isNotBlank(ignoreObjId) && ignoreObjId.equals(user.getObjId())) {
			isExist = false;
		}
		return isExist;
	}

	/** 
	 * 验证邮箱地址是否存在
	 * @param   email 要验证的邮箱地址
	 * @param   ignoreObjId 忽略验证的userId
	 * @return  如果存在返回true，否则返回false
	 * @Exception   
	 */
	public boolean emailExist(String email, String ignoreObjId) throws Exception {
		boolean isExist = true;
		User user = userDaoHibernate.getUserByEmail(email);
		if(user == null) {
			isExist = false;
		}
		else if(StringUtils.isNotBlank(ignoreObjId) && ignoreObjId.equals(user.getObjId())) {
			isExist = false;
		}
		return isExist;
	}

	/**
	 * 获取用户分页列表数据
	 * @param page 分页信息
	 * @return 带有分页信息的列表数据
	 * @throws Exception
	 */
	public Page<User> list(Page<User> page) throws Exception {
		return userDaoHibernate.list(page);
	}

	/**
	 * 修改用户密码
	 * @param objId 用户id
	 * @param password 新密码
	 * @throws Exception
	 */
	public void updatePassword(String objId, String password) throws Exception {
		User user = userDaoHibernate.get(objId);
		user.setPassword(password);
		userDaoHibernate.save(user);
	}

	
	

}
