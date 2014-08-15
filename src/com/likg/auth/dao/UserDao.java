package com.likg.auth.dao;

import com.likg.auth.domain.User;
import com.likg.common.page.Page;
import com.likg.core.dao.BaseGenericDao;

public interface UserDao extends BaseGenericDao<User> {

	/** 
	 * 根据用户名获取用户对象
	 * @param   userName 用户名
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	User getUserByUserName(String userName) throws Exception;

	/** 
	 * 根据邮箱获取用户对象
	 * @param   email 邮件地址
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	User getUserByEmail(String email) throws Exception;

	/**
	 * 获取用户分页列表数据
	 * @param page 分页信息
	 * @return 带有分页信息的列表数据
	 * @throws Exception
	 */
	Page<User> list(Page<User> page) throws Exception;
}
