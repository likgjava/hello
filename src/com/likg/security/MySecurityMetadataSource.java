package com.likg.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.likg.auth.dao.ResourceDao;
import com.likg.auth.domain.Resource;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.dao.hibernate.query.QueryObjectBase;

/**
 * 加载资源与权限的对应关系
 * 此类在初始化时，应该取到所有资源及其对应角色的定义
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(MySecurityMetadataSource.class);

	private ResourceDao resourceDaoHibernate;
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public MySecurityMetadataSource(ResourceDao resourceDaoHibernate) {
		this.resourceDaoHibernate = resourceDaoHibernate;
		loadResourceDefine();
	}


	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}

	// 加载所有资源与权限的关系
	private void loadResourceDefine() {
		if (resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			List<Resource> resourceList = new ArrayList<Resource>();
			try {
				//获取所有资源列表数据
				QueryObject<Resource> queryObject = new QueryObjectBase<Resource>();
				queryObject.setEntityClass(Resource.class);
				resourceList = resourceDaoHibernate.findListByQuery(queryObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Resource resource : resourceList) {
				Collection<ConfigAttribute> configAttributeList = new ArrayList<ConfigAttribute>();
				// 以权限名封装为Spring的security Object
				ConfigAttribute configAttribute = new SecurityConfig(resource.getResName());
				configAttributeList.add(configAttribute);
				resourceMap.put("/" + resource.getResUrl(), configAttributeList);
			}
		}

		//Set<Entry<String, Collection<ConfigAttribute>>> resourceSet = resourceMap.entrySet();
		//Iterator<Entry<String, Collection<ConfigAttribute>>> iterator = resourceSet.iterator();
	}

	/**
	 * 返回所请求资源所需要的权限
	 * @param object FilterInvocation
	 * @return 
	 */
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		//System.out.println("requestUrl is " + requestUrl);
		if (resourceMap == null) {
			loadResourceDefine();
		}
		
		Collection<ConfigAttribute> s =resourceMap.get(requestUrl);
		if("/view/auth/sys/desktop/index.jsp".equals(requestUrl)) {///view/auth/sys/desktop/index.jsp
			//throw new AccessDeniedException(" 没有权限访问！ ");
		}
		return s;
	}

}