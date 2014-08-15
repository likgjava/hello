package com.likg.core.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
*  Comments: <strong>查询结果转换工具类</strong><中文描述>				<br/>
  			<中文描述> 											<br/>
                      											<br/>
*  Project:   srplatform                    					<br/>           
*  Module ID: <(模块)类编号，可以引用系统设计中的类编号>     		<br/>
                                           
*  Create Date：2009-10-23    									<br/>
*  Modified By：lic   										<br/>
*  CopyRright (c)2008-xxxx:   <珠海政采 >    						<br/>                                 
*  Modified Date:  2009-10-23                                    
       <修改的内容>  
       
*  @author lic       
*  @since 0.4
*  @version: 0.5
 */
@SuppressWarnings("unchecked")
public class HqlResultConvertUtils {
	
   // private static EnumService enumService;  
	
	public static List hqlResultConvert(List hqlList, String[] hqlElements, Map... enumMap){
    	return hqlResultConvert(hqlList, hqlElements, null, null, enumMap);
    }
    
	/**
	 * 
	 * Description :  查询结果转换，  枚举转换后的值在js里要加  _value 后缀获取， 例如  n[sex_value]
	 * Create Date: 2009-10-23 by lic  Modified Date: 2009-10-23 by lic 	wangsw支持null undefined  日期	枚举等转义	
	 * @param： hqlList 待转换结果  
	 * @param： hqlElements 指定列
	 * @param： 枚举map<属性列,资源key>(例如map.put("usrIsLocked", "srplatform.auth.lock"))
	 * @return：   转换后结果List<Map>     alias是现实属性别名
	 * @Exception:
	 */
	public static List hqlResultConvert(List hqlList, String[] hqlElements, String[] alias, Class clazz, Map... enumMap){
		if(null == hqlList || 0 == hqlList.size()){
			return hqlList;
		}
		if(null == hqlElements || 0 == hqlElements.length){
			return hqlList;
		}
		List result = new ArrayList();
		boolean hasAlias=(alias==null?false:true);
		Object newObj=(clazz!=null?org.springframework.beans.BeanUtils.instantiateClass(clazz):null);
		for(Object obj : hqlList){
			Map map = new HashMap();
			if(obj instanceof Object[]){
				Object[] object = (Object[])obj;
				for(int i=0;i<hqlElements.length;i++){
					if(object[i]!=null){
						String property=hqlElements[i];
						if(enumMap!=null && enumMap.length > 0 && !enumMap[0].isEmpty()){
							if((enumMap[0].get(property)!=null)){//不为空则是需要枚举转义的部分  
								if(object[i] instanceof Boolean)//boolean类型转换
									object[i]=((Boolean) object[i]?"1":"0");		
								//map.put(property, getEnumService().getDescByValue(enumMap[0].get(property).toString(), object[i].toString()));
								map.put(property+"_value", object[i]);
							}else{
								map.put(property, object[i]);
							}
						}else if(newObj!=null  && hasAlias && !(property.equals(alias[i])) && !"".equals(alias[i])){
							property=alias[i];
							try {
								if(property.indexOf(".") < 0){
									BeanUtils.invokePrivateMethod(newObj, "set"+String.valueOf(hqlElements[i].charAt(0)).toUpperCase()+hqlElements[i].substring(1), new Object[]{object[i]});
									map.put(hqlElements[i], object[i]);
									object[i]=BeanUtils.invokePrivateMethod(newObj, "get"+String.valueOf(alias[i].charAt(0)).toUpperCase()+alias[i].substring(1), new Object[]{});
									map.put(property, object[i]);
								}else if(property.indexOf(".") == property.lastIndexOf(".")){ //只含一个"."
									//获得“.”之前的对象
									String firstObjStr = property.substring(0,property.indexOf("."));
									Class firstObjClazz = (clazz.getDeclaredField(firstObjStr)).getType();
									Object firstObj = (firstObjClazz!=null?org.springframework.beans.BeanUtils.instantiateClass(firstObjClazz):null);
									
									//获得“.”之后的属性
									String lastObjStr =  hqlElements[i].substring(hqlElements[i].indexOf(".")+1);
									String lastObjAlias =  alias[i].substring(alias[i].indexOf(".")+1);
									BeanUtils.invokePrivateMethod(firstObj, "set"+String.valueOf(lastObjStr.charAt(0)).toUpperCase()+lastObjStr.substring(1), new Object[]{object[i]});
									map.put(hqlElements[i], object[i]);
									object[i]=BeanUtils.invokePrivateMethod(firstObj, "get"+String.valueOf(lastObjAlias.charAt(0)).toUpperCase()+lastObjAlias.substring(1), new Object[]{});
									map.put(property, object[i]);
								}
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							}
						}else if(object[i] instanceof Date){//日期解析转换
							Date date=(Date) object[i];
							Calendar c=Calendar.getInstance();
							c.setTime(date); 
							c.get(java.util.Calendar.HOUR_OF_DAY);
							String pattern="yyyy-MM-dd";
							if(c.get(java.util.Calendar.HOUR_OF_DAY)!=0){
								pattern="yyyy-MM-dd HH:mm:ss";
							}		
							map.put(property, DateUtil.format(date, pattern));
							if(hasAlias)map.put(hqlElements[i], DateUtil.format(date, pattern));
						}else{
							map.put(property, object[i]);
							if(hasAlias)map.put(hqlElements[i], object[i]);
						}	
					}else{
						map.put(hqlElements[i], "");//为null的结果改为""空格
					}
				}
			}else{
				map.put(hqlElements[0], obj!=null?obj:"");
			}
			result.add(map);
		}
		return result;
	}
	
	/**
	 * 
	 * Description :  查询结果转换
	 * Create Date: 2009-10-23 by lic  Modified Date: 2009-10-23 by lic
	 * @param：    hqlList 待转换结果
	 * @param：    hqlElements 指定列
	 * @return：   转换后结果<Object>
	 * @Exception:
	 */
	public static List hqlResultConvert(List hqlList, String[] hqlElements, Class clazz){
		if(null == hqlList || 0 == hqlList.size()){
			return hqlList;
		}
		if(null == hqlElements || 0 == hqlElements.length){
			return hqlList;
		}
		List result = new ArrayList();
		for(Object obj : hqlList){
			Object domain;
			try {
				domain = clazz.newInstance();
				if(obj instanceof Object[]){
					Object[] object = (Object[])obj;
					for(int i=0;i<hqlElements.length;i++){
						BeanUtils.forceSetProperty(domain, hqlElements[i], object[i]);
					}
				}else{
					BeanUtils.forceSetProperty(domain, hqlElements[0], obj);
				}
				result.add(domain);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @gpcsoft.property title="enumService<中文解释>"
	 */
//	public static EnumService getEnumService() {
//		if(enumService==null)
//			return (EnumService) FrameBeanFactory.getBean("enumServiceImpl");
//		return enumService;
//	}

}
