package com.likg.core.util;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;

import com.likg.core.dao.BaseGenericDao;
import com.likg.core.domain.BaseObject;

@Service
public class TreeSequenceUtil {
	
	/**菜单记录号前缀字符*/
	public static final String MENU_PREFIX = "M";
	/**资源记录号前缀字符*/
	public static final String RESOURCE_PREFIX = "R";
	/**商品分类记录号前缀字符*/
	public static final String GOODS_CLASS_PREFIX = "G";
	/**栏目记录号前缀字符*/
	public static final String CHANNEL_PREFIX = "C";

	@Resource(name="baseGenericDaoHibernate")
	private BaseGenericDao<BaseObject> baseGenericDaoHibernate;
	
	@SuppressWarnings("unchecked")
	public String getSequenceNo(final Class clazz, final String prefix) {
		return baseGenericDaoHibernate.getHibernateTemplate().execute(new HibernateCallback<String>(){
			public String doInHibernate(Session session) throws HibernateException, SQLException {
				String sequenceNo = prefix;
				String hql = "select MAX(SUBSTRING(objId, "+(prefix.length()+1)+", 2)) from "+clazz.getName()+" where objId like '"+prefix+"%' and LENGTH(objId) = "+(prefix.length()+2);
				Query query = session.createQuery(hql);
				Object result = query.uniqueResult();
				if(result!=null) {
					int num = Integer.parseInt(result.toString()) + 1;
					sequenceNo += (num<9 ? "0" : "") + num;
				} else {
					sequenceNo += "01";
				}
				
				return sequenceNo;
			}
			
		});
	}
}