package com.likg.auth.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.likg.auth.dao.ResourceDao;
import com.likg.auth.domain.Resource;
import com.likg.auth.domain.Role;
import com.likg.core.dao.hibernate.BaseTreeDaoHibernate;

@Repository
public class ResourceDaoHibernate extends BaseTreeDaoHibernate<Resource> implements ResourceDao {

	/**
	 * 根据角色获取资源id列表
	 * @param roleId 角色id
	 * @return
	 */
	public List<String> getResIdListByRole(final String roleId) throws Exception {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<String>>(){
			@SuppressWarnings("unchecked")
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "select rr.RES_ID from AUTH_ROLE_RESOURCE rr where rr.ROLE_ID=:roleId";
				
				Query query = session.createSQLQuery(sql);
				query.setParameter("roleId", roleId);
				
				return query.list();
			}

		});
	}

	/**
	 * 为角色分配资源
	 * @param roleId 角色id
	 * @param resIds 资源ids
	 * @throws Exception
	 */
	public void allotResource(final String roleId, final String[] resIds) throws Exception {
		this.getHibernateTemplate().execute(new HibernateCallback<List<Role>>(){
			public List<Role> doInHibernate(Session session) throws HibernateException, SQLException {
				//删除原有资源
				String sql = "delete from AUTH_ROLE_RESOURCE where ROLE_ID=:roleId";
				Query query = session.createSQLQuery(sql);
				query.setParameter("roleId", roleId);
				query.executeUpdate();
				
				//添加新资源
				sql = "insert into AUTH_ROLE_RESOURCE(role_id, res_id) values(:roleId, :resId)";
				for(String resId : resIds) {
					query = session.createSQLQuery(sql);
					query.setParameter("roleId", roleId);
					query.setParameter("resId", resId);
					query.executeUpdate();
				}
				
				return null;
			}
		});
	}

}
