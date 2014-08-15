package com.likg.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.Role;
import com.likg.auth.service.RoleService;
import com.likg.common.Constants;
import com.likg.core.controller.BaseGenericController;

/**
 * @springmvc.view value="roleFormView" url="view/auth/role/role_form.jsp"
 * @springmvc.view value="allotRoleView" url="view/auth/role/allot_role.jsp"
 */
@Controller
@RequestMapping("/RoleController.do")
public class RoleController extends BaseGenericController<Role>  {
	
	@Resource(name="roleServiceImpl")
	private RoleService roleService;
	
	/**
	 * 跳转到角色表单页面，若objId为null则为新增操作，否则为修改操作
	 * @param objId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toRoleFormView")
	public ModelAndView toRoleFormView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Role role = null;
		//新增
		if(StringUtils.isBlank(objId)) {
			role = new Role();
		}
		else {
			role = roleService.get(objId);
		}
		model.put("role", role);
		
		return new ModelAndView("roleFormView", model);
	}
	
	
	/**
	 * 保存菜单信息
	 * @param Role 菜单对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public ModelAndView save(Role role, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//保存用户信息
		roleService.saveRole(role);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 跳转到分配角色页面
	 * @param userId 用户id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toAllotRoleView")
	public ModelAndView toAllotRoleView(String userId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//获取全部角色数据
		List<Role> allRoleList = roleService.getAll();
		model.put("allRoleList", allRoleList);
		
		//获取已分配的角色
		List<String> allottedRoleIds = new ArrayList<String>();
		List<Role> roleList = roleService.getRoleListByUser(userId);
		for(Role r : roleList) {
			allottedRoleIds.add(r.getObjId());
		}
		model.put("allottedRoleIds", allottedRoleIds);
		model.put("userId", userId);
		
		return new ModelAndView("allotRoleView", model);
	}
	
	
	/**
	 * 分配角色，保存用户角色信息
	 * @param userId 用户id
	 * @param roleIds 角色ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=allotRole")
	public ModelAndView allotRole(String userId, String[] roleIds, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//分配角色
		roleService.allotRole(userId, roleIds);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}

}
