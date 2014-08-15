package com.likg.auth.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.likg.auth.dao.MenuDao;
import com.likg.auth.domain.Menu;
import com.likg.auth.domain.Resource;
import com.likg.core.dao.hibernate.BaseTreeDaoHibernate;

@Repository
public class MenuDaoHibernate extends BaseTreeDaoHibernate<Menu> implements MenuDao {
	
	/**
	 * 根据用户获取菜单列表
	 * @param userId 用户id
	 * @param queryType 查询类型（all：查询全部;firstLevel:查询一级菜单；byParentId:查询指定节点下的所有子节点）
	 * @param parentId 父节点id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getMenuListByUser(final String userId, final String queryType, final String parentId) throws Exception {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Menu>>(){
			public List<Menu> doInHibernate(Session session) throws HibernateException, SQLException {
				String querySql = ""; //查询条件
				//查询全部
				if("all".equals(queryType)) {
				}
				//查询一级菜单
				else if("firstLevel".equals(queryType)) {
					querySql = "and m.menu_parent_id is null";
				}
				//查询指定节点下的所有子节点
				else if("byParentId".equals(queryType) && StringUtils.isNotBlank(parentId)) {
					querySql = "and m.menu_parent_id = :parentId";
				}
				
				String sql = "select m.menu_id, m.menu_name, m.menu_css, s.res_url " +
						" from auth_menu m" +
						" join auth_resource s on m.res_id=s.res_id " + querySql +
						" join auth_role_resource rs on s.res_id=rs.res_id" +
						" join auth_user_role ur on rs.role_id=ur.role_id and ur.user_id=:userId" +
						" order by m.menu_id";
				Query query = session.createSQLQuery(sql);
				query.setParameter("userId", userId);
				if("byParentId".equals(queryType) && StringUtils.isNotBlank(parentId)) {
					query.setParameter("parentId", parentId);
				}
				List<Object[]> list = query.list();
				
				//拼装成对象
				List<Menu> menuList = new ArrayList<Menu>();
				for(Object[] objs : list) {
					Menu menu = new Menu();
					menu.setObjId((String)objs[0]);
					menu.setMenuName((String)objs[1]);
					menu.setMenuCss((String)objs[2]);
					Resource resource = new Resource();
					resource.setResUrl((String)objs[3]);
					menu.setResource(resource);
					menuList.add(menu);
				}
				
				return menuList;
			}

		});
	}



	
	
	
}
