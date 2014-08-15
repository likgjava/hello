package com.likg.auth.service;

import com.likg.auth.domain.User;
import com.likg.common.page.Page;
import com.likg.core.service.BaseGenericService;

public interface UserService extends BaseGenericService<User> {

	/**
	 * 保存注册用户信息
	 * @param user 用户对象
	 * @throws Exception
	 */
	void saveRegisterUser(User user) throws Exception;
	
	/**
	 * 保存用户信息
	 * @param user 用户对象
	 * @throws Exception
	 */
	void saveUser(User user) throws Exception;
	
	/** 
	 * 根据用户名获取用户对象
	 * @param   userName 用户名
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	User getUserByUserName(String userName) throws Exception;
	
	/** 
	 * 验证用户名是否存在
	 * @param   userName 要检查的用户名
	 * @param   ignoreObjId 忽略验证的userId
	 * @return  
	 * @Exception   
	 */
	boolean userNameExist(String userName, String ignoreObjId) throws Exception;

	/** 
	 * 验证邮箱地址是否存在
	 * @param   email 要验证的邮箱地址
	 * @param   ignoreObjId 忽略验证的userId
	 * @return  如果存在返回true，否则返回false
	 * @Exception   
	 */
	boolean emailExist(String email, String ignoreObjId) throws Exception;
	
	/**
	 * 获取用户分页列表数据
	 * @param page 分页信息
	 * @return 带有分页信息的列表数据
	 * @throws Exception
	 */
	Page<User> list(Page<User> page) throws Exception;

	/**
	 * 修改用户密码
	 * @param objId 用户id
	 * @param password 新密码
	 * @throws Exception
	 */
	void updatePassword(String objId, String password) throws Exception;

	
}
