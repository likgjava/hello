package com.likg.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.Menu;
import com.likg.auth.domain.User;
import com.likg.auth.service.MenuService;
import com.likg.security.AuthenticationHelper;

/**
 * @springmvc.view value="manageCenterView" url="view/auth/desktop/index.jsp"
 */
@Controller
@RequestMapping("/MemberController.do")
public class MemberController {
	
	@Resource(name="menuServiceImpl")
	private MenuService menuService;
	
	/**
	 * 跳转到会员中心页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toMemberCenterView")
	public ModelAndView toMemberCenterView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User user = AuthenticationHelper.getCurrentUser();
		model.put("user", user);
		
		return new ModelAndView("memberCenterView", model);
	}
	
	/**
	 * 跳转到管理中心页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toManageCenterView")
	public ModelAndView toManageCenterView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User user = AuthenticationHelper.getCurrentUser();
		model.put("user", user);
		
		return new ModelAndView("manageCenterView", model);
	}
	
	/** 
	 * 获取会员菜单导航页面
	 * @return  ModelAndView
	 */
	@RequestMapping(params="method=getMemberNavgaitorView")
	public ModelAndView getMemberNavgaitorView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//根据用户获取菜单数据
		List<Menu> menuList = menuService.getMenuListByUser(AuthenticationHelper.getCurrentUser().getObjId(), "all", null);
		model.put("menuList", menuList);
		
		return new ModelAndView("memberNavgaitorView", model);
	}
	
	/** 
	 * 跳转到会员信息修改页面
	 * @return  ModelAndView
	 */
	@RequestMapping(params="method=toMemberUpdateView")
	public ModelAndView toMemberUpdateView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//获取用户信息
		model.put("user", AuthenticationHelper.getCurrentUser());
		
		return new ModelAndView("memberUpdateView", model);
	}
	

}
