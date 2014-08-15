package com.likg.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.likg.auth.dao.MenuDao;
import com.likg.auth.domain.Menu;
import com.likg.auth.service.MenuService;
import com.likg.core.service.impl.BaseTreeServiceImpl;
import com.likg.core.util.TreeSequenceUtil;

@Service
public class MenuServiceImpl extends BaseTreeServiceImpl<Menu> implements MenuService {
	
	@Resource(name="menuDaoHibernate")
	private MenuDao menuDaoHibernate;
	
	@Resource
	private TreeSequenceUtil treeSequenceUtil;

	/**
	 * 保存菜单信息
	 * @param menu 菜单对象
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception {
		//新增
		if(StringUtils.isBlank(menu.getObjId())) {
			//修改父节点isLeaf的属性值
			String parentId = menu.getParent().getObjId();
			if(!StringUtils.isBlank(parentId)) {
				Menu parentMenu = menuDaoHibernate.get(parentId);
				if(parentMenu.getIsLeaf()) {
					parentMenu.setIsLeaf(false);
					menuDaoHibernate.save(parentMenu);
				}
			}
			
			//保存节点对象
			menu.setIsLeaf(true);
			if(StringUtils.isBlank(parentId)) {
				menu.setParent(null);
			}
			String prefix = (StringUtils.isBlank(parentId) ? TreeSequenceUtil.MENU_PREFIX : parentId);
			menu.setObjId(treeSequenceUtil.getSequenceNo(Menu.class, prefix));
			menuDaoHibernate.save(menu);
			
		}
		//修改
		else {
			Menu oldMenu = menuDaoHibernate.get(menu.getObjId());
			
			oldMenu.setMenuName(menu.getMenuName());
			oldMenu.setMenuDesc(menu.getMenuDesc());
			oldMenu.setResource(menu.getResource());
			
			menuDaoHibernate.save(oldMenu);
		}
		
	}

	/**
	 * 根据节点id获取XML格式的子节点列表数据
	 * @param objId 节点id
	 * @return XML格式的子节点数据
	 * @throws Exception
	 */
	public String listChildrenXml(String objId) throws Exception {
		//获取菜单列表数据
		List<Menu> menuList = menuDaoHibernate.listChildrenById("-1".equals(objId) ? null : objId);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='"+(StringUtils.isBlank(objId) ? 0 : objId)+"'>");
		if(StringUtils.isBlank(objId)) {
			xml.append("<item text='菜单树' id='-1' open='1'>");
		}
		for(Menu menu : menuList) {
			xml.append("<item text='"+menu.getMenuName()+"' id='"+menu.getObjId()+"'  child='"+(menu.getIsLeaf()?0:1)+"' />");
		}
		if(StringUtils.isBlank(objId)) {
			xml.append("</item>");
		}
		xml.append("</tree>");
		
		return xml.toString();
	}

	/**
	 * 根据用户获取菜单列表
	 * @param userId 用户id
	 * @param queryType 查询类型（all：查询全部;firstLevel:查询一级菜单；byParentId:查询指定节点下的所有子节点）
	 * @param parentId 父节点id
	 * @return
	 * @throws Exception
	 */
	public List<Menu> getMenuListByUser(String userId, String queryType, String parentId) throws Exception {
		List<Menu> menuList = menuDaoHibernate.getMenuListByUser(userId, queryType, parentId);
		
		//如果是查询全部菜单数据，则格式化菜单列表数据
		if("all".equals(queryType)) {
			List<Menu> parentMenuList = new ArrayList<Menu>();
			Menu preMenu = null;
			for(Menu menu : menuList) {
				//一级
				if(menu.getObjId().length() <= 3) {
					parentMenuList.add(menu);
				}
				//是前一个的儿子
				else if(menu.getObjId().length() > preMenu.getObjId().length()) {
					menu.setParent(preMenu);
					preMenu.getChildren().add(menu);
				}
				//是前一个的兄弟
				else if(menu.getObjId().length() == preMenu.getObjId().length()) {
					menu.setParent(preMenu.getParent());
					preMenu.getParent().getChildren().add(menu);
				}
				//是前一个的长辈
				else if(menu.getObjId().length() < preMenu.getObjId().length()) {
					//找到当前节点的父亲
					Menu p = preMenu.getParent();
					for(int i=0; i<(preMenu.getObjId().length()-menu.getObjId().length())/2-1; i++) {
						p = p.getParent();
					}
					menu.setParent(p);
					p.getChildren().add(menu);
				}
				
				preMenu = menu;
			}
			
			menuList = parentMenuList;
		}
		
		return menuList;
	}


	

}
