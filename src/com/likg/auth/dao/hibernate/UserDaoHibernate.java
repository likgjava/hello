package com.likg.auth.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.likg.auth.dao.UserDao;
import com.likg.auth.domain.User;
import com.likg.common.page.Page;
import com.likg.core.dao.hibernate.BaseGenericDaoHibernate;

@Repository
public class UserDaoHibernate extends BaseGenericDaoHibernate<User> implements UserDao {
	
	/** 
	 * 根据用户名获取用户对象
	 * @param   userName 用户名
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	@SuppressWarnings("unchecked")
	public User getUserByUserName(String userName) throws Exception {
		User user = null;
		//String hql = "select u from User u join Role r on u.objId in elements(r.users) join Resource s on r.objId in elements(s.roles) where u.userName=?";
		//String hql = "select u from User u join fetch u.roles, Role r join fetch r.resources, Resource s  where u.objId in elements(r.users) and r.objId in elements(s.roles) and u.userName=?";
		String hql = "select u from User u join fetch u.roles r join fetch r.resources where  u.userName=?";
		List<User> userList = this.getHibernateTemplate().find(hql, userName);
		if(userList!=null && !userList.isEmpty()) {
			user = userList.get(0);
		}
		
		return user;
	}

	/** 
	 * 根据邮箱获取用户对象
	 * @param   email 邮件地址
	 * @return  如果存在则返回用户对象，否则返回null
	 * @Exception   
	 */
	@SuppressWarnings("unchecked")
	public User getUserByEmail(String email) throws Exception {
		User user = null;
		
		List<User> userList = this.getHibernateTemplate().find("from User u where u.email=?", email);
		if(userList!=null && !userList.isEmpty()) {
			user = userList.get(0);
		}
		
		return user;
	}

	/**
	 * 获取用户分页列表数据
	 * @param page 分页信息
	 * @return 带有分页信息的列表数据
	 * @throws Exception
	 */
	public Page<User> list(final Page<User> page) throws Exception {
		return this.getHibernateTemplate().execute(new HibernateCallback<Page<User>>(){
			@SuppressWarnings("unchecked")
			public Page<User> doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql = "from User u ";
				Query query = session.createQuery(hql);
				query.setFirstResult(page.getFirstResult());
				query.setMaxResults(page.getPageSize());
				
				List<User> userList = query.list();
				
				query = session.createQuery("select count(*) from User u " );
				Long totalRecord = (Long) query.uniqueResult();
				
				Page<User> pageData = new Page<User>(userList, totalRecord, page.getPageSize(), page.getPageIndex());
				
				//for(User u : pageData.getData()) {
					//u.setRoles(null);186 3820 9662
				//}
				return pageData;
			}
			
		});
		
		
		//return null;
	}
	
}
