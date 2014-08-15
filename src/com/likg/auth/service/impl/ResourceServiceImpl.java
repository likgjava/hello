package com.likg.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.likg.auth.dao.ResourceDao;
import com.likg.auth.domain.Resource;
import com.likg.auth.service.ResourceService;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.dao.hibernate.query.QueryObjectBase;
import com.likg.core.service.impl.BaseTreeServiceImpl;
import com.likg.core.util.TreeSequenceUtil;

@Service
public class ResourceServiceImpl extends BaseTreeServiceImpl<Resource> implements ResourceService {
	
	@javax.annotation.Resource
	private ResourceDao resourceDaoHibernate;
	
	@javax.annotation.Resource
	private TreeSequenceUtil treeSequenceUtil;

	/**
	 * 保存资源信息
	 * @param Resource 资源对象
	 * @throws Exception
	 */
	public void saveResource(Resource resource) throws Exception {
		//新增
		if(StringUtils.isBlank(resource.getObjId())) {
			//修改父节点isLeaf的属性值
			String parentId = resource.getParent().getObjId();
			if(!StringUtils.isBlank(parentId)) {
				Resource parentResource = resourceDaoHibernate.get(parentId);
				if(parentResource.getIsLeaf()) {
					parentResource.setIsLeaf(false);
					resourceDaoHibernate.save(parentResource);
				}
			}
			
			//保存节点对象
			resource.setIsLeaf(true);
			if(StringUtils.isBlank(parentId)) {
				resource.setParent(null);
			}
			String prefix = (StringUtils.isBlank(parentId) ? TreeSequenceUtil.RESOURCE_PREFIX : parentId);
			resource.setObjId(treeSequenceUtil.getSequenceNo(Resource.class, prefix));
			resourceDaoHibernate.save(resource);
			
		}
		//修改
		else {
			Resource oldResource = resourceDaoHibernate.get(resource.getObjId());
			oldResource.setResName(resource.getResName());
			oldResource.setResDesc(resource.getResDesc());
			oldResource.setResUrl(resource.getResUrl());
			resourceDaoHibernate.save(oldResource);
		}
	}

	/**
	 * 根据节点id获取XML格式的子节点列表数据
	 * @param objId 节点id
	 * @return XML格式的子节点数据
	 * @throws Exception
	 */
	public String listChildrenXml(String objId) throws Exception {
		
		//获取资源列表数据
		List<Resource> ResourceList = resourceDaoHibernate.listChildrenById("-1".equals(objId) ? null : objId);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='"+(StringUtils.isBlank(objId) ? 0 : objId)+"'>");
		if(StringUtils.isBlank(objId)) {
			xml.append("<item text='资源树' id='-1' open='1'>");
		}
		for(Resource Resource : ResourceList) {
			xml.append("<item text='"+Resource.getResName()+"' id='"+Resource.getObjId()+"'  child='"+(Resource.getIsLeaf()?0:1)+"' />");
		}
		if(StringUtils.isBlank(objId)) {
			xml.append("</item>");
		}
		xml.append("</tree>");
		
		return xml.toString();
	}

	/**
	 * 删除节点及其所有子孙节点
	 * @param objId
	 */
	public void removeAll(String objId) throws Exception {
		//删除节点及其所有子孙节点
		resourceDaoHibernate.removeAll(objId);
		
	}

	/**
	 * 根据角色获取XML格式的资源树
	 * @param roleId 角色id
	 * @return XML格式的资源树
	 * @throws Exception
	 */
	public String getTreeXmlByRole(String roleId) throws Exception {
		List<Resource> parentResList = new ArrayList<Resource>();
		
		//获取所有资源列表数据
		QueryObject<Resource> queryObject = new QueryObjectBase<Resource>();
		queryObject.setEntityClass(Resource.class);
		queryObject.getQueryProjections().setOrderProperty("objId");
		queryObject.getQueryProjections().setDescFlag(false);
		List<Resource> resourceList = resourceDaoHibernate.findListByQuery(queryObject);
		
		//组装数据
		int preIdLength = 0;
		Resource preRes = null;
		for(Resource r : resourceList) {
			//一级
			if(r.getParent() == null) {
				parentResList.add(r);
			}
			//是前一个的儿子
			else if(r.getObjId().length() > preIdLength) {
				r.setParent(preRes);
				preRes.getChildren().add(r);
			}
			//是前一个的兄弟
			else if(r.getObjId().length() == preIdLength) {
				r.setParent(preRes.getParent());
				preRes.getParent().getChildren().add(r);
			}
			//是前一个的长辈
			else if(r.getObjId().length() < preIdLength) {
				//找到当前节点的父亲
				Resource p = preRes.getParent();
				for(int i=0; i<(preIdLength-r.getObjId().length())/2-1; i++) {
					p = p.getParent();
				}
				r.setParent(p);
				p.getChildren().add(r);
			}
			
			preIdLength = r.getObjId().length();
			preRes = r;
		}
		
		//根据角色获取资源id列表
		List<String> selectedResIds = resourceDaoHibernate.getResIdListByRole(roleId);
		
		//拼装XML字符串
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tree id='0'>");
		for(Resource r : parentResList) {
			xml.append(transferObject2Xml(r, selectedResIds));
		}
		xml.append("</tree>");
		
		return xml.toString();
	}
	
	/**
	 * 把资源对象转化为XML格式的字符串
	 * @param res 对象
	 * @return
	 */
	private String transferObject2Xml(Resource res, List<String> selectedResIds) {
		StringBuilder xml = new StringBuilder();
		xml.append("<item text='"+res.getResName()+"' id='"+res.getObjId()+"' open='1' child='"+(res.getIsLeaf()?0:1)+"' "+(selectedResIds.contains(res.getObjId())?"checked='1'":"")+">");
		for(Resource r : res.getChildren()) {
			xml.append(transferObject2Xml(r, selectedResIds));
		}
		xml.append("</item>");
		return xml.toString();
	}

	/**
	 * 为角色分配资源
	 * @param roleId 角色id
	 * @param resIds 资源ids
	 * @throws Exception
	 */
	public void allotResource(String roleId, String[] resIds) throws Exception {
		resourceDaoHibernate.allotResource(roleId, resIds);
	}



}
