package com.likg.core.util;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.domain.BaseObject;

/** 
  *  Comments: <strong>ObjectUtils</strong>            		
  *	 <br/>   sql  hql指定列查询之后																		
  *  <br/>CopyRright (c)2008-xxxx:   珠海政采软件技术有限公司    		
  *  <br/>Project:   srplatform                    					          
  *  <br/>Module ID: 权限平台     		
  *  <br/>Create Date：2010-4-7 下午03:53:52 by 未知    					                            
  *  <br/>Modified Date:  2010-4-7 下午03:53:52 by wangsw                                   
  *<p>@since 0.5
  *   @version: 0.5 
  */ 
public final class ObjectUtils {

	public ObjectUtils() {
	}

	public static Serializable getObjId(BaseObject obj) {
		// PropertyDescriptor propertyDescs[];
		// int i;
		// propertyDescs = BeanUtils.getPropertyDescriptors(obj.getClass());
		// i = 0;
		// if
		// (!propertyDescs[i].getName().toUpperCase().equals(Constants.PRIMARY_KEY_NAME.toUpperCase()))
		// continue; /* Loop/switch isn't completed */
		// return (Serializable)propertyDescs[i].getReadMethod().invoke(obj, new
		// Object[0]);
		return null;
	}

	/**
	 * 判断属性是否为BaseObject类型
	 * @param propDesc 要判断的属性
	 * @return
	 */
	public static boolean isObject(PropertyDescriptor propDesc) {
		Object temp = null;
		try {
			temp = propDesc.getPropertyType().newInstance();
		} catch (Exception e) {
			//忽略异常信息
		}
		return (temp instanceof BaseObject);
	}

	// public static boolean i
	public static boolean isNull(Object object) {
		if(object==null){
			return true;
		}else{
			try{
				Method method=null;
				method=object.getClass().getMethod("getObjId", new Class[]{});
				String objId=(String)method.invoke(object,  new Object[]{});
				if(!StringUtils.isEmpty(objId)){
					return false;
				}
			}catch(Exception e){
				
			}
		}
		//if(Constants.PRIMARY_KEY_NAME)
		return true;
	}
	
	public static String getObjId(Object object) throws Exception{
		if(object==null){
			return null;
		}else{
			try{
				Method method=null;
				method=object.getClass().getMethod("getObjId", new Class[]{});
				String objId=(String)method.invoke(object,  new Object[]{});
				if(!StringUtils.isEmpty(objId)){
					return objId;
				}
			}catch(Exception e){
				throw new Exception("该对象无ObjId属性");
			}
		}
		//if(Constants.PRIMARY_KEY_NAME)
		return null;
	}
	/**
	 * 
	 * @param dataList 查询出来的 Object[]
	 * @param query   查询条件
	 * @param asNames  查询列的别名 
	 * @param convertClass  强转的类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map ConvertQueryList(List dataList,QueryObject query,String[] asNames,Class convertClass) throws Exception{
		String[] columns = query.getQueryProjections().getProperty();
		
		
		if(asNames==null||asNames.length==0){
			asNames=columns;
		}
		Map map= new HashMap();
		if(dataList==null||dataList.size()==0){
			return map;
		}
		if(columns.length==1){
			return map;
		}
		for(int i=0;i<dataList.size();i++){
			//Object o=getJson(asNames,(Object[])dataList.get(i),convertClass,map);
			//dataList.set(i, o);
		}
		
		return map;
	}
 
	/** 
	 * Description :  解析json		sql时间解析格式有个bug
	 * 
	 * Create Date: 2010-4-7下午04:14:29 by wangsw  Modified Date: 2010-4-7下午04:14:29 by wangsw
	 * @param   
	 * @return  
	 * @throws ParseException 
	 * @Exception   
	 */
	
	/**
	 * 递归方法将 name 和value 绑定为 json对象 带点列自动递归
	 * @param name
	 * @param value
	 * @param parent
	 * @param parentKey
	 * @param map
	 * @return
	 */
	
}