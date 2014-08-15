package com.likg.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.User;
import com.likg.auth.service.UserService;
import com.likg.common.Constants;
import com.likg.core.controller.BaseGenericController;
import com.likg.security.AuthenticationHelper;

/**
 * @springmvc.view value="userFormView" url="view/auth/user/user_form.jsp"
 * @springmvc.view value="resourceDetailView" url="view/auth/user/resource_detail.jsp"
 * @springmvc.view value="allotRoleView" url="view/auth/user/allot_role.jsp"
 *
 */
@Controller
@RequestMapping("/UserController.do")
public class UserController extends BaseGenericController<User>  {
	
	@Resource(name="userServiceImpl")
	private UserService userService;
	
	@RequestMapping(params="method=toIndexView")
	public ModelAndView toIndexView(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		return new ModelAndView("/view/auth/portal/index.jsp");
	}
	
	/**
	 * 跳转到用户表单页面，若objId为null则为新增操作，否则为修改操作
	 * @param objId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toUserFormView")
	public ModelAndView toUserFormView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User user = null;
		//新增
		if(StringUtils.isBlank(objId)) {
			user = new User();
		}
		else {
			user = userService.get(objId);
		}
		model.put("user", user);
		
		return new ModelAndView("userFormView", model);
	}
	
	
	/**
	 * 保存菜单信息
	 * @param User 菜单对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public ModelAndView save(User user, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//保存用户信息
		userService.saveUser(user);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 修改用户密码
	 * @param objId 用户id
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=updatePassword")
	public ModelAndView updatePassword(String objId, String oldPassword, String newPassword, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//获取用户信息
		User user = AuthenticationHelper.getCurrentUser();
		if(user.getPassword().equals(oldPassword)) {
			//修改用户密码
			userService.updatePassword(user.getObjId(), newPassword);
			model.put(Constants.SUCCESS, true);
		}
		//原密码输入错误
		else {
			model.put(Constants.RESULT, "原密码输入错误!");
			model.put(Constants.SUCCESS, false);
		}
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}

}
