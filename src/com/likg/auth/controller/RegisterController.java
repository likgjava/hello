package com.likg.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.User;
import com.likg.auth.service.UserService;
import com.likg.common.Constants;
import com.likg.common.util.ResponseUtils;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * @springmvc.view value="registerView" url="view/common/register.jsp"
 *
 */
@Controller
@RequestMapping("/RegisterController.do")
public class RegisterController {
	
	@Resource(name="userServiceImpl")
	private UserService userService;
	
	@Resource
	private ImageCaptchaService imageCaptchaService;
	
	
	/** 
	 * 跳转到注册页面
	 * @return  ModelAndView
	 */
	@RequestMapping(params="method=toRegisterView")
	public ModelAndView toRegisterView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		return new ModelAndView("registerView", model);
	}
	
	//保存用户的注册信息
	@RequestMapping(params="method=saveRegisterUser")
	public ModelAndView saveRegisterUser(User user, String captcha, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		boolean registerResult = true;
		
		//判断验证码是否正确
		if (!imageCaptchaService.validateResponseForID(request.getSession().getId(), captcha)) {
			model.put("result", "验证码不正确");
			registerResult = false;
		} else {
			userService.saveRegisterUser(user);
		}
		
		model.put(Constants.SUCCESS, registerResult);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	
	/** 
	 * 用户名唯一性验证
	 * @param   userName 要验证的用户名
	 * @param   ignoreObjId 忽略验证的userId
	 */
	@RequestMapping(params="method=userNameUnique")
	public void userNameUnique(String userName, String ignoreObjId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//用户名为空，返回false。
		if (StringUtils.isBlank(userName)) {
			ResponseUtils.renderJson(response, "false");
		}
		//用户名存在，返回false。
		else if (userService.userNameExist(userName, ignoreObjId)) {
			ResponseUtils.renderJson(response, "false");
		}
		else {
			ResponseUtils.renderJson(response, "true");
		}
	}
	
	/** 
	 * 邮箱地址唯一性验证
	 * @param   email 要验证的邮箱地址
	 * @param   ignoreObjId 忽略验证的userId
	 */
	@RequestMapping(params="method=emailUnique")
	public void emailUnique(String email, String ignoreObjId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//用户名为空，返回false。
		if (StringUtils.isBlank(email)) {
			ResponseUtils.renderJson(response, "false");
		}
		//用户名存在，返回false。
		else if (userService.emailExist(email, ignoreObjId)) {
			ResponseUtils.renderJson(response, "false");
		}
		else {
			ResponseUtils.renderJson(response, "true");
		}
	}
	
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-beans.xml");
		User user = (User) ac.getBean("user");
		
		System.out.println(user.getUserName());
		
	}

}
