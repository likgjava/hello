package com.likg.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
@SuppressWarnings("unchecked")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils{

	protected static final Log logger = LogFactory.getLog(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Class clazz, String propertyName) throws NoSuchFieldException {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue)
			throws NoSuchFieldException {
		Assert.notNull(object);
		Assert.hasText(propertyName);

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.debug("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchMethodException 如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		Assert.notNull(object);
		Assert.hasText(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 按Filed的类型取得Field列表.
	 */
	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 按FiledName获得Field的类型.
	 */
	public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String getGetterName(Class type, String fieldName) {
		Assert.notNull(type, "Type required");
		Assert.hasText(fieldName, "FieldName required");

		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
 
	public static void copyPropertiesFilterEmptyNew( Object target,Object source) {
		/*try {
	    	PropertyDescriptor[] propertyDescriptors=org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(source);
	    	for (int i = 0; i < propertyDescriptors.length; i++) {
	    		PropertyDescriptor descriptor=propertyDescriptors[i];
	    		Object aValue = org.apache.commons.beanutils.PropertyUtils.getProperty(source, descriptor.getName());
	    		if(aValue instanceof GpcBaseObject){
		    		if(!ObjectUtils.isNull(aValue)){
		    			if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
		    				copySimpleGpcPropertiesFilterEmpty(org.apache.commons.beanutils.PropertyUtils.getProperty(target, descriptor.getName()), aValue);
		    			}    			
		    		}
	    		}else{
	    			if(aValue!=null){
	    				if( aValue instanceof Set){
	    					Set sourceSetSub=((Set)aValue);//前台传过来的Set集合
	    					if(sourceSetSub.size() > 0){
		    					//by wangsw	集合拷贝
		    					PropertyDescriptor targetPropertyDescriptor=PropertyUtils.getPropertyDescriptor(target, descriptor.getName());
		    					if(targetPropertyDescriptor!=null){
			    					Set targetSetSub=(Set) targetPropertyDescriptor.getReadMethod().invoke(target, new Object[]{});//数据库里的Set集合
									Iterator it1=((Set)targetSetSub).iterator();
									Iterator it2=((Set)sourceSetSub).iterator();
			    					if(sourceSetSub!=null){
			        					if( sourceSetSub.size()!=0 ){//如果前台的Set不为空
			    							Type[] type = descriptor.getWriteMethod().getGenericParameterTypes();
			    							ParameterizedType parameterizedType=(ParameterizedType) type[0];
			    							Type[] parameterType=parameterizedType.getActualTypeArguments();
			    							if(parameterType.length==1){
			    								Class entityClass = (Class) parameterType[0];	 
			    								if(entityClass.newInstance() instanceof GpcBaseObject) {//带有泛型的Set
			    									List<String> idsList=new ArrayList<String>();//记录下回填过的
			        								for(;it2.hasNext();){
			        									GpcBaseObject obj2=(GpcBaseObject) it2.next();
			        									it1=((Set)targetSetSub).iterator();//重新遍历
			        									for(;it1.hasNext();){
			        										GpcBaseObject obj1 = (GpcBaseObject)it1.next();
			        										if(obj2.getObjId()!=null && obj1.getObjId().equals(obj2.getObjId())){
				        										idsList.add(obj1.getObjId());  
				        										copySimpleGpcPropertiesFilterEmpty(obj1, obj2);//再拷贝集合里的一层GpcBaseObject	  by wangsw
			        										}
			        									}
			        								}
			    									it1=((Set)targetSetSub).iterator();//重新遍历
			        								for(;it1.hasNext();){//把没有回填复制的删除
			        									GpcBaseObject obj=(GpcBaseObject) it1.next();
			        									if(!idsList.contains(obj.getObjId()) )
			        										it1.remove();
			        								}
			        								it2=((Set)sourceSetSub).iterator();
			        								for(;it2.hasNext();){//把新增的加入旧的集合
			        									GpcBaseObject obj=(GpcBaseObject) it2.next();
			        									if(obj.getObjId() == null)
			        										targetSetSub.add(obj);
			        								}
			    								}
			    							}
			        					}
			    					}
		    					}
	    					}
	    				}else if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
		    				org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(target, descriptor.getName(), aValue);
		    			}    			
		    		}
	    		}
	    		//System.out.println(descriptor.getName()+":"+org.apache.commons.beanutils.PropertyUtils.getProperty(source, descriptor.getName())+",类型:"+descriptor.getPropertyType().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PropertyCopySessionBean2EntityBeanException(source.getClass().getName()+"调用copyPropertiesFilterEmpty( Object target,Object source)函数时发生绑定数据格式有错误! 属性拷贝异常:在做修改时需要拷贝数据库里的值来补充完成你的Entity bean时, 由于绑定的数据格式或者值非法造成的异常");
		}*/
    }  
    
	/**
     * 拷贝属性值，过滤非empty的值
     * @param target
     * @param source
     * @throws Exception
     */
    public static void copyPropertiesFilterEmpty( Object target,Object source)throws Exception {
    	/*PropertyDescriptor[] propertyDescriptors=org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(source);
    	for (int i = 0; i < propertyDescriptors.length; i++) {
    		PropertyDescriptor descriptor=propertyDescriptors[i];
    		Object aValue=org.apache.commons.beanutils.PropertyUtils.getProperty(source, descriptor.getName());
    		if(aValue instanceof GpcBaseObject){
	    		if(!ObjectUtils.isNull(aValue)){
	    			if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
	    				copySimpleGpcPropertiesFilterEmpty(org.apache.commons.beanutils.PropertyUtils.getProperty(target, descriptor.getName()), aValue);
	    			}    			
	    		}
    		}else{
    			if(aValue!=null){
	    			if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
	    				org.apache.commons.beanutils.PropertyUtils.setProperty(target, descriptor.getName(), aValue);
	    			}    			
	    		}
    		}
    		
    		//System.out.println(descriptor.getName()+":"+org.apache.commons.beanutils.PropertyUtils.getProperty(source, descriptor.getName())+",类型:"+descriptor.getPropertyType().getName());
		}*/
    }    
    
	/** 
	 * Description : 拷贝集合里的1层GpcBaseObject子对象 	 暂不包含集合    暂不包含第2层 以及以上
	 * Create Date: 2010-4-29下午01:59:36 by wangsw  Modified Date: 2010-4-29下午01:59:36 by wangsw
	 * @param	目标对象、	源对象   
	 * @Exception   
	 */
	@SuppressWarnings("unused")
	private static void copySimpleGpcPropertiesFilterEmpty(Object target,Object source)throws Exception {
    	/*PropertyDescriptor[] propertyDescriptors=org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(source);
    	for (int i = 0; i < propertyDescriptors.length; i++) {
    		PropertyDescriptor descriptor=propertyDescriptors[i];
    		Object aValue=org.apache.commons.beanutils.PropertyUtils.getProperty(source, descriptor.getName());
    		if(aValue instanceof GpcBaseObject){
	    		if(!ObjectUtils.isNull(aValue)){
	    			if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
	    				org.apache.commons.beanutils.PropertyUtils.setProperty(target, descriptor.getName(), aValue);
	    			}    			
	    		}
    		}else 
    		if(aValue!=null && !(aValue instanceof HashSet)){
				if(org.apache.commons.beanutils.PropertyUtils.isWriteable(target, descriptor.getName())){
    				org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(target, descriptor.getName(), aValue);
				}
			}    			
		}*/
    }   

}
