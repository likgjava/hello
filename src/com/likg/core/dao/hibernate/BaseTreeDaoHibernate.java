package com.likg.core.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.likg.core.dao.BaseTreeDao;
import com.likg.core.domain.BaseObject;
import com.likg.core.domain.BaseTree;

@Repository
public class BaseTreeDaoHibernate<T> extends BaseGenericDaoHibernate<T> implements BaseTreeDao<T> {

	/**
	 * 获取节点对象，包含所有祖先节点对象信息
	 * @param objId 节点id
	 * @return
	 * @throws Exception
	 */
	public T getTreeObject(final String objId) throws Exception {
		return this.getTreeObject(objId, this.getPersistClass());
	}
	/**
	 * 获取节点对象，包含所有祖先节点对象信息
	 * @param objId 节点id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T getTreeObject(final String objId, Class<T> persistClass) throws Exception {
		//获取当前实体类的类名
		final String className = persistClass.getSimpleName();
		return this.getHibernateTemplate().execute(new HibernateCallback<T>(){
			public T doInHibernate(Session session) throws HibernateException, SQLException {
				BaseTree obj = null;
				
				String hql = "from "+className+" t where INSTR(:id, t.objId)>0 order by t.objId desc";
				Query query = session.createQuery(hql);
				query.setString("id", objId);
				List<BaseTree> objList = query.list();
				BaseTree currentPartent = null;
				for(BaseTree baseTree : objList) {
					if(obj == null) {
						obj = baseTree;
					} else if(currentPartent == null) {
						currentPartent = baseTree;
						obj.setParent((BaseObject) currentPartent);
					} else {
						currentPartent.setParent((BaseObject) baseTree);
						currentPartent = baseTree;
					}
				}
				
				return (T) obj;
			}
			
		});
	}
	
	/**
	 * 获取当前节点的所有父节点，从上到下顺序排列，包含当前节点
	 * @param objId 当前节点id
	 * @return
	 * @throws Exception
	 */
	public List<T> getParentList(final String objId) throws Exception {
		return this.getParentList(objId, this.getPersistClass());
	}
	/**
	 * 获取当前节点的所有父节点，从上到下顺序排列，包含当前节点
	 * @param objId 当前节点id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> getParentList(final String objId, Class<T> persistClass) throws Exception {
		//获取当前实体类的类名
		final String className = persistClass.getSimpleName();
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>(){
			
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "from "+className+" t where INSTR(:id, t.objId)>0 order by t.objId asc";
				Query query = session.createQuery(hql);
				query.setString("id", objId);
				return query.list();
			}
			
		});
	}
	
	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param objId 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 * @throws Exception
	 */
	public List<T> listChildrenById(final String objId) throws Exception {
		return this.listChildrenById(objId, this.getPersistClass());
	}
	/**
	 * 根据节点id获取该节点下的子节点列表数据
	 * @param objId 节点id，若为null则获取第一级节点
	 * @return 子节点列表
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> listChildrenById(final String objId, Class<T> persistClass) throws Exception {
		//获取当前实体类的类名
		final String className = persistClass.getSimpleName();
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>(){
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql = null;
				if(StringUtils.isBlank(objId)) {
					hql = "from "+className+" m where m.parent.objId is null";
				} else {
					hql = "from "+className+" m where m.parent.objId = '" + objId + "'";
				}
				
				Query query = session.createQuery(hql);
				
				return query.list();
			}
			
		});
	}

	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	public void removeAll(final String objId) throws Exception {
		this.removeAll(objId, this.getPersistClass());
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	@SuppressWarnings("unchecked")
	public void removeAll(final String objId, Class<T> persistClass) throws Exception {
		//获取当前实体类的类名
		final String className = persistClass.getSimpleName();
		
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				//删除节点及其所有子孙节点
				String hql = "delete from "+className;
				if(!StringUtils.isBlank(objId)) {
					hql += " where objId like '" + objId + "%'";
				}
				Query query = session.createQuery(hql);
				query.executeUpdate();
				
				//如果该节点无兄弟节点，则修改父节点的isLeaf属性的值
				if(!StringUtils.isBlank(objId) && objId.length()>3){
					String parentId = objId.substring(0, objId.length()-2);
					query = session.createQuery("select count(m.objId) from "+className+" m where m.parent.objId = :pid");
					query.setParameter("pid", parentId);
					Object result = query.uniqueResult();
					if(result!=null && Integer.parseInt(result.toString())==0) {
						query = session.createQuery("update "+className+" m set m.isLeaf = 1 where m.objId = :id");
						query.setParameter("id", parentId);
						query.executeUpdate();
					}
				}
				
				return null;
			}
			
		});
	}

	

}
