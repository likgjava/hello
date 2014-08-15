package com.likg.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.Menu;
import com.likg.auth.service.MenuService;
import com.likg.common.Constants;
import com.likg.common.util.ResponseUtils;
import com.likg.core.controller.BaseGenericController;
import com.likg.security.AuthenticationHelper;

/**
 * @springmvc.view value="menuFormView" url="view/auth/menu/menu_form.jsp"
 * @springmvc.view value="menuDetailView" url="view/auth/menu/menu_detail.jsp"
 * @springmvc.view value="secondLevelMenuListView" url="/view/auth/desktop/left.jsp"
 *
 */
@Controller
@RequestMapping("/MenuController.do")
public class MenuController extends BaseGenericController<Menu> {
	
	@Resource(name="menuServiceImpl")
	private MenuService menuService;
	
	/**
	 * 跳转到菜单表单页面，若objId为null则为新增操作，否则为修改操作
	 * @param objId 菜单对象id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toMenuFormView")
	public ModelAndView toMenuFormView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Menu menu = null;
		//新增
		if(StringUtils.isBlank(objId)) {
			menu = new Menu();
			//获取父节点信息，若父节点id为空则增加一级节点
			String parentId = request.getParameter("parentId");
			if(!StringUtils.isBlank(parentId)) {
				Menu parentMenu = menuService.get(parentId);
				menu.setParent(parentMenu);
			}
			
			//设置层级
			menu.setTreeLevel(Short.valueOf(request.getParameter("menuLevel")));
		}
		else {
			menu = menuService.get(objId);
		}
		model.put("menu", menu);
		
		return new ModelAndView("menuFormView", model);
	}
	
	/**
	 * 跳转到菜单详情页面
	 * @param objId 菜单对象id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toMenuDetailView")
	public ModelAndView toMenuDetailView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//获取菜单信息
		Menu menu = menuService.get(objId);
		model.put("menu", menu);
		
		return new ModelAndView("menuDetailView", model);
	}
	
	/**
	 * 保存菜单信息
	 * @param menu 菜单对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public ModelAndView save(Menu menu, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//保存菜单信息
		menuService.saveMenu(menu);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 删除节点及其所有子孙节点
	 * @param objId 节点id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=removeAll")
	public ModelAndView removeAll(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//删除节点及其所有子孙节点
		menuService.removeAll(objId);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 根据id获取子节点列表数据
	 * @param id 节点id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getChildrenById")
	public ModelAndView getChildrenById(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//获取子节点列表数据
		List<Menu> menuList = menuService.listChildrenById(id);
		model.put("menuList", menuList);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 根据id获取XML格式的子节点列表数据
	 * @param id 节点id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getChildrenXmlById")
	public void getChildrenXmlById(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取XML格式的子节点列表数据
		String xml = menuService.listChildrenXml(id);
		
		ResponseUtils.renderXml(response, xml);
	}
	
	/**
	 * 根据用户获取菜单数据
	 * @param objId 菜单id，若为空则获取一级菜单
	 * @param userId 用户id，若为空则取当前用户id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=getMenuListByUser")
	public ModelAndView getMenuListByUser(String objId, String userId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//若为空则取当前用户id
		if(StringUtils.isBlank(userId)) {
			userId = AuthenticationHelper.getCurrentUser().getObjId();
		}
		
		//根据用户获取菜单数据
		String queryType = (StringUtils.isBlank(objId) ? "firstLevel" : "byParentId");
		List<Menu> menuList = menuService.getMenuListByUser(userId, queryType, objId);
		model.put("menuList", menuList);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 根据用户获取菜单数据
	 * @param objId 菜单id，若为空则获取一级菜单
	 * @param userId 用户id，若为空则取当前用户id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toSecondLevelMenuListView")
	public ModelAndView toSecondLevelMenuListView(String objId, String userId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//若为空则取当前用户id
		if(StringUtils.isBlank(userId)) {
			userId = AuthenticationHelper.getCurrentUser().getObjId();
		}
		
		//根据用户获取菜单数据
		List<Menu> menuList = menuService.getMenuListByUser(userId, "byParentId", objId);
		model.put("menuList", menuList);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView("secondLevelMenuListView", model);
	}
	

}
