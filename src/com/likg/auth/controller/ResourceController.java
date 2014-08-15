package com.likg.auth.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.auth.domain.Resource;
import com.likg.auth.service.ResourceService;
import com.likg.common.Constants;
import com.likg.common.util.ResponseUtils;
import com.likg.core.controller.BaseGenericController;

/**
 * @springmvc.view value="resourceFormView" url="view/auth/resource/resource_form.jsp"
 * @springmvc.view value="resourceDetailView" url="view/auth/resource/resource_detail.jsp"
 *
 */

@Controller
@RequestMapping("/ResourceController.do")
public class ResourceController extends BaseGenericController<Resource> {
	
	@javax.annotation.Resource(name="resourceServiceImpl")
	private ResourceService resourceService;
	
	/**
	 * 跳转到资源表单页面，若objId为null则为新增操作，否则为修改操作
	 * @param objId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toResourceFormView")
	public ModelAndView toResourceFormView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Resource resource = null;
		//新增
		if(StringUtils.isBlank(objId)) {
			resource = new Resource();
			//获取父节点信息，若父节点id为空则增加一级节点
			String parentId = request.getParameter("parentId");
			if(!StringUtils.isBlank(parentId)) {
				Resource parentResource = resourceService.get(parentId);
				resource.setParent(parentResource);
			}
			
			//设置层级
			resource.setTreeLevel(Short.valueOf(request.getParameter("resourceLevel")));
		}
		else {
			resource = resourceService.get(objId);
		}
		model.put("resource", resource);
		
		return new ModelAndView("resourceFormView", model);
	}
	
	/**
	 * 跳转到资源详情页面
	 * @param objId 资源对象id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toResourceDetailView")
	public ModelAndView toResourceDetailView(String objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Resource resource = resourceService.get(objId);
		model.put("resource", resource);
		
		return new ModelAndView("resourceDetailView", model);
	}
	
	/**
	 * 保存资源信息
	 * @param Resource 资源对象
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public ModelAndView save(Resource Resource, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//保存资源信息
		resourceService.saveResource(Resource);
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
		resourceService.removeAll(objId);
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
		String xml = resourceService.listChildrenXml(id);
		
		ResponseUtils.renderXml(response, xml);
	}
	
	/**
	 * 根据角色获取XML格式的资源树
	 * @param roleId 角色id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getTreeXmlByRole")
	public void getTreeXmlByRole(String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//根据角色获取XML格式的资源树
		String xml = resourceService.getTreeXmlByRole(roleId);
		
		ResponseUtils.renderXml(response, xml);
	}
	
	/**
	 * 分配角色，保存用户角色信息
	 * @param userId 用户id
	 * @param roleIds 角色ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=allotResource")
	public ModelAndView allotResource(String roleId, String[] resIds, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//分配资源
		resourceService.allotResource(roleId, resIds);
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	

}
