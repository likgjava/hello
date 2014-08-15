package com.likg.auth.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.likg.auth.dao.RoleDao;
import com.likg.auth.domain.Role;
import com.likg.core.dao.hibernate.BaseGenericDaoHibernate;

@Repository
public class RoleDaoHibernate extends BaseGenericDaoHibernate<Role> implements RoleDao {
	
	/**
	 * 根据角色名称获取角色对象信息
	 * @param roleName 角色英文名称
	 * @return
	 * @throws Exception
	 */
	public Role getRoleByName(final String roleName) throws Exception {
		return this.getHibernateTemplate().execute(new HibernateCallback<Role>(){
			@SuppressWarnings("unchecked")
			public Role doInHibernate(Session session) throws HibernateException, SQLException {
				Role role = null;
				
				String hql = "from Role r where r.roleName=:roleName";
				Query query = session.createQuery(hql);
				query.setParameter("roleName", roleName);
				List<Role> roleList = query.list();
				if(roleList!=null && !roleList.isEmpty()) {
					role = roleList.get(0);
				}
				
				return role;
			}
		});
	}

	/**
	 * 获取指定用户的角色列表数据
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoleListByUser(final String userId) throws Exception {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Role>>(){
			@SuppressWarnings("unchecked")
			public List<Role> doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "select r from Role r, User u where r.objId in elements (u.roles) and u.objId=:userId";
				
				Query query = session.createQuery(hql);
				query.setParameter("userId", userId);
				
				return query.list();
			}
		});
	}

	/**
	 * 分配角色
	 * @param userId 用户id
	 * @param roleIds 角色ids
	 * @throws Exception
	 */
	public void allotRole(final String userId, final String[] roleIds) throws Exception {
		this.getHibernateTemplate().execute(new HibernateCallback<List<Role>>(){
			public List<Role> doInHibernate(Session session) throws HibernateException, SQLException {
				//删除原有角色
				String sql = "delete from AUTH_USER_ROLE where USER_ID=:userId";
				Query query = session.createSQLQuery(sql);
				query.setParameter("userId", userId);
				query.executeUpdate();
				
				//添加新角色
				sql = "insert into AUTH_USER_ROLE(user_id, role_id) values(:userId, :roleId)";
				for(String roleId : roleIds) {
					query = session.createSQLQuery(sql);
					query.setParameter("userId", userId);
					query.setParameter("roleId", roleId);
					query.executeUpdate();
				}
				
				return null;
			}
		});
	}

	
}
