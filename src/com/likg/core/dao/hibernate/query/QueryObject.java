package com.likg.core.dao.hibernate.query;

public interface QueryObject<T> {
	
    /**
     * 获取实体对象类型
     * @return class type
     */
	public Class<T> getEntityClass();
    
	/**
	 * 设置实体对象类型
	 * @param entityClass
	 */
	public void setEntityClass(Class<T> entityClass);
    
    /**
     * Return the instance of QueryProjections.
     * 
     * @return the instance of QueryProjections
     */
	public QueryProjections getQueryProjections();
    
    /**
     * Set the instance of QueryProjections.
     * 
     * @param the instance of QueryProjections
     */
    public void setQueryProjections(QueryProjections queryProjection);
    
    /**
     * 获取查询参数对象
     * @return
     */
    public QueryParam getQueryParam();
    
    /**
     * 设置查询参数对象
     * @param queryParam
     */
    public void setQueryParam(QueryParam queryParam);
    
}
