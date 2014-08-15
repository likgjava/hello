package com.likg.core.dao.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.likg.common.page.Page;
import com.likg.core.dao.BaseGenericDao;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.dao.hibernate.query.QueryProjections;
import com.likg.core.domain.BaseObject;

@Repository
public class BaseGenericDaoHibernate<T> extends HibernateDaoSupport implements BaseGenericDao<T> {

	/**
	 * 把sessionFactory对象自动注入到HibernateDaoSupport对象中
	 * @param sessionFactory 该对象的配置来自于配置文件
	 */
	@Resource
	public void setSessionFactoryee(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 根据泛型获取持久化对象的类型
	 * @return 持久化对象的类型
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getPersistClass() {
		if (super.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			return (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return null;
	}

	/**
	 * 根据id获取对象信息
	 * @param objId 记录号
	 * @return
	 */
	public T get(String objId) {
		return this.get(objId, this.getPersistClass());
	}

	/**
	 * 根据记录号和持久化对象类型获取对象数据
	 * @param objId 记录号
	 * @param persistClass 持久化对象类型
	 * @return
	 */
	public T get(String objId, Class<T> persistClass) {
		return (T) super.getHibernateTemplate().get(persistClass, objId);
	}
	
	/**
	 * 获取全部列表数据
	 * @param persistClass 持久化对象类型
	 * @return
	 */
	public List<T> getAll(Class<T> persistClass) {
		return super.getHibernateTemplate().loadAll(persistClass);
	}

	/**
	 * 保存对象信息
	 * @param object 要保存的对象
	 * @return 保存后的对象
	 */
	public T save(T object) {
		this.preSave(object);
		super.getHibernateTemplate().saveOrUpdate(object);
		return object;
	}

	/**
	 * 保存对象之前做的操作
	 * @param object 要保存的对象
	 */
	private void preSave(Object object) {
		BaseObject baseObject = (BaseObject) object;
		if (StringUtils.isBlank(baseObject.getObjId())) {
			baseObject.setObjId(null);

			// 设置对象的创建时间
			if (baseObject.getCreateTime() == null) {
				try {
					Method method = baseObject.getClass().getMethod("setCreateTime", Date.class);
					method.invoke(baseObject, new Date());
				} catch (Exception e) {
					throw new RuntimeException(baseObject.getClass().getName() + "类中没有setCreateTime方法");
				}
			}
		}
	}

	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 */
	public void remove(String objId) {
		if (!StringUtils.isBlank(objId)) {
			this.remove(new String[] { objId });
		}
	}

	/**
	 * 根据id数组删除多个对象
	 * 
	 * @param objIds
	 *            对象id数组
	 */
	@SuppressWarnings("unchecked")
	public void remove(String[] objIds) {
		if (objIds == null || objIds.length == 0) {
			return;
		}

		final String clazzName = this.getPersistClass().getName();
		final String[] ids = objIds;

		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String hql = "delete from " + clazzName + " where objId in (:ids)";
				Query query = session.createQuery(hql);
				query.setParameterList("ids", ids);
				return null;
			}
		});
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param objId
	 *            对象id
	 */
	public void remove(String objId, Class<T> persistClass) {
		if (!StringUtils.isBlank(objId)) {
			this.remove(new String[] { objId }, persistClass);
		}
	}

	/**
	 * 根据id数组删除多个对象
	 * 
	 * @param objIds
	 *            对象id数组
	 */
	@SuppressWarnings("unchecked")
	public void remove(String[] objIds, Class<T> persistClass) {
		if (objIds == null || objIds.length == 0) {
			return;
		}

		final String clazzName = persistClass.getName();
		final String[] ids = objIds;

		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "delete from " + clazzName
						+ " where objId in (:ids)";
				Query query = session.createQuery(hql);
				query.setParameterList("ids", ids);
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * 根据QueryObject查询分页列表数据
	 * 
	 * @param queryObject
	 *            查询对象
	 * @param cacheable
	 *            是否使用缓存
	 * @param start
	 *            本页开始记录索引
	 * @param pageSize
	 *            每页显示的记录数
	 * @return
	 */
	public Page<T> findByQuery(QueryObject<T> queryObject, boolean cacheable, int start, int pageSize) {
		return findByQuery(queryObject, start, pageSize, this.getPersistClass());
	}
	
	/**
	 * 根据queryObject获取所有满足条件的数据列表
	 * @param queryObject 查询对象
	 * @return
	 */
	public List<T> findListByQuery(QueryObject<T> queryObject) {
		return findListByQueryObject(queryObject, true, -1, -1);
	}

	@SuppressWarnings("unchecked")
	public Page<T> findByQuery(QueryObject<T> queryObject, int start, int pageSize, Class clazz) {
		appendDefaulOrder(queryObject);// 默认排序
		long totalCount = -1;
		List<T> result = null;
		QueryProjections backupProjections = null;
		if (queryObject.getQueryProjections() == null) {
			queryObject.setQueryProjections(new QueryProjections());
			backupProjections = queryObject.getQueryProjections();
		}
		if (backupProjections == null) {
			backupProjections = queryObject.getQueryProjections().clone();
		}
		backupProjections.setRowCount(true);
		queryObject.setQueryProjections(backupProjections);
		if (clazz != null)
			queryObject.setEntityClass(clazz);

		//先查询总记录数
		List countResult = findListByQueryObject(queryObject, true, -1, -1);
		if (countResult == null) {
			totalCount = 0;
		} else {
			if (countResult.get(0) instanceof Object[]) {
				totalCount = (Integer) ((Object[]) countResult.get(0))[0];
			} else {
				totalCount = (Long) (countResult.get(0));
			}
		}
		
		//查询结果集
		if (totalCount > 0) {
			backupProjections.setRowCount(false);
			queryObject.setQueryProjections(backupProjections);
			result = findListByQueryObject(queryObject, true, start, pageSize);
		}
		// 如果为空 gt-gird 无法解析 不处理返回数据
		if (result == null) {
			result = new ArrayList<T>();
		}
		
		return new Page<T>(result, totalCount, pageSize, (start+1)/pageSize+1);
	}

	@SuppressWarnings("unchecked")
	private List<T> findListByQueryObject(final QueryObject<T> queryObject, final boolean cacheable, final int start, final int pageSize) {
		//检验QueryObject是否有效.
		HibernateDAOHelper.validateQueryObject(queryObject);
		return (List<T>) this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Criteria criteria = session.createCriteria(queryObject.getEntityClass());
				Cache cache = getCacheAnnotation(queryObject.getEntityClass());
				if (cache != null) {
					criteria.setCacheable(true);
					criteria.setCacheRegion(cache.region());
				}
				try {
					criteria = HibernateDAOHelper.getInstance(getSessionFactory()).buildCriteria(criteria, queryObject);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}

				if (pageSize > 0) {
					criteria.setMaxResults(pageSize);
				}
				if (start > 0) {
					criteria.setFirstResult(start);
				}
				return criteria.list();
			}
		});
	}

	/**
	 * 通过实体对象的注解获取缓存对象
	 * @param persistClass 实体对象类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Cache getCacheAnnotation(Class persistClass) {
		List<Annotation> annotationList = Arrays.asList(persistClass.getAnnotations());
		if (annotationList != null) {
			for (Annotation annotation : annotationList) {
				//如果domain有缓存注解
				if (annotation.annotationType() == Cache.class) {
					Cache cache = (Cache) annotation;
					return cache;
				}
			}
		}
		return null;
	}

	private void appendDefaulOrder(final QueryObject<T> queryObject) {
		/*
		 * Class clazz=queryObject.getEntityClass(); QueryProjections
		 * queryProjections=queryObject.getQueryProjections(); List<String>
		 * orderPropertyList=queryProjections.getOrderProperty()==null?new
		 * ArrayList<String>():new
		 * ArrayList<String>(Arrays.asList(queryProjections
		 * .getOrderProperty())); List<Boolean> orderFlagList=new
		 * ArrayList<Boolean>(); if(queryProjections.getDescFlag()!=null){
		 * for(boolean flag:queryProjections.getDescFlag())
		 * orderFlagList.add(flag); } List<Annotation>
		 * annotationList=Arrays.asList(clazz.getAnnotations()); for(Annotation
		 * annotation:annotationList){
		 * if(annotation.annotationType()==OrderProperty
		 * .class){//如果domain有OrderProperty注解 OrderProperty
		 * orderProperty=(OrderProperty) annotation; String[]
		 * propertys=orderProperty.property().split(","); String[]
		 * flags=orderProperty.flag().split(",");
		 * if(propertys.length!=flags.length) throw new
		 * IllegalDomainConfigException
		 * (clazz.getName()+"类的OrderProperty注解配置错误! 请检查property和flag属性的长度是否一致!"
		 * ); if(propertys!=null){ for(int i=0; i<propertys.length; i++){
		 * if(!orderPropertyList.contains(propertys[i])){
		 * orderPropertyList.add(propertys[i]);//添加注解指定的排序
		 * orderFlagList.add(Boolean.valueOf(flags[i]));//添加注解指定的排序的升降 } } } try
		 * { clazz.getDeclaredField("createTime");
		 * if(!orderPropertyList.contains("createTime")){
		 * orderPropertyList.add(orderProperty.defaultProperty());
		 * orderFlagList.add(Boolean.valueOf(orderProperty.defaultFlag())); } }
		 * catch (NoSuchFieldException e) { } } }
		 * 
		 * List interfaceList=Arrays.asList(clazz.getInterfaces());
		 * if(interfaceList.contains(IPropertyCTime.class) ||
		 * interfaceList.contains(IPropertyCUserTime.class) ) { try {
		 * clazz.getDeclaredField("createTime"); } catch (NoSuchFieldException
		 * e) { //throw new
		 * RequiredDomainPropertyNotFoundException(clazz.getName
		 * ()+"缺少createTime属性, 平台约定的Domain属性未提供!"); } //按创建时间排序
		 * if(!orderPropertyList.contains("createTime")){
		 * orderPropertyList.add("createTime"); orderFlagList.add(true); }; }
		 * 
		 * boolean[] orderFlags=new boolean[orderFlagList.size()]; int i=0;
		 * for(Boolean flag:orderFlagList){ orderFlags[i++]=flag; }
		 * queryProjections.setOrderProperty(orderPropertyList.toArray(new
		 * String[]{})); queryProjections.setDescFlag(orderFlags);
		 * queryObject.setQueryProjections(queryProjections);
		 */
	}


}
