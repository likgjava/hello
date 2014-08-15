package com.likg.core.dao.hibernate.query;

import java.lang.reflect.ParameterizedType;

/**
 * QueryObject的实现,   queryProjections和queryParam的属性在声明时new了一下
 * 
 * @author likg
 *
 * @param <T>
 */
public class QueryObjectBase<T> implements QueryObject<T> {
	
	protected static final String CACHE_KEY = "QueryObject_cache";

    /**实体对象类型*/
    private Class<T> entityClass;

    /**
     * the instance of QueryProjections
     */
    private QueryProjections queryProjections = new QueryProjections();

    /**查询参数对象*/
    private QueryParam queryParam = new QueryParam();

    /**
     * Return the instance of entity's class type.
     * 
     * @return class type
     */
    @SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		if(this.entityClass==null)
			return (Class) ((ParameterizedType)super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return this.entityClass;
    }
    
    /**
     * Set the instance of entity's class type.
     * 
     * @param class type
     */
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Return the instance of QueryProjections.
     * 
     * @return the instance of QueryProjections
     */
    public QueryProjections getQueryProjections() {
        return queryProjections;
    }

    /**
     * Set the instance of QueryProjections.
     * 
     * @param the instance of QueryProjections
     */
    public void setQueryProjections(QueryProjections queryProjections) {
        this.queryProjections = queryProjections;
    }
    
    /**
     * Return the instance of QueryParam.
     * 
     * @return the instance of QueryParam
     */
    public QueryParam getQueryParam() {
        return queryParam;
    }

    /**
     * Set the instance of QueryParam.
     * 
     * @param the instance of QueryParam
     */
    public void setQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
    }

}
