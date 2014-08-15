package com.likg.core.dao.hibernate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.likg.core.dao.hibernate.query.HibernateTypeConvertor;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.dao.hibernate.query.QueryParam;
import com.likg.core.dao.hibernate.query.QueryProjections;
import com.likg.core.util.BeanUtils;
import com.likg.core.util.RandomGenerator;


/**
 * @author xiaojf
 *
 */
public class HibernateDAOHelper {
    
    private static Log logger = LogFactory.getLog(HibernateDAOHelper.class);
    
    private volatile static HibernateDAOHelper hibernateDAOHelper;
    
    //@SuppressWarnings("unchecked")
	//private ConcurrentMap aliasesMap;//废除单例模式下，线程不安全变量 edit by wangcl
    
    protected final static int DEFAULT_BATCH_SIZE = 20;
    
    protected final static String ALIAS_PREFIX = "alias_";
    
    public final static String COUNT_ALIAS = "c";
    
    private SessionFactory sessionFactory;
    
    private HibernateDAOHelper() {
    }

    public static HibernateDAOHelper getInstance(SessionFactory sessionFactory) {
    	if(hibernateDAOHelper == null){
    		synchronized (HibernateDAOHelper.class) {
    			hibernateDAOHelper = new HibernateDAOHelper();
        		hibernateDAOHelper.setSessionFactory(sessionFactory);
			}
    	}
    	return hibernateDAOHelper;
    }
    
    public HibernateDAOHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    /**
     * 检验QueryObject是否有效.
     */
    @SuppressWarnings("unchecked")
	public static void validateQueryObject(QueryObject queryObject) {
    	if(queryObject == null) {
    		throw new RuntimeException("queryObject is null!");
    	}
    	else if (queryObject.getEntityClass() == null) {
    		throw new RuntimeException("queryObject.entityClass is null!");
        }
    }
    
    /**
     * Assemble the Hibernate Criteria instance by QueryProjections which is in the QueryObject.
     * 
     * @param Hibernate Criteria's instance
     * @param QueryObject's instance
     * @return Assembled Hibernate Criteria's instance
     */
    @SuppressWarnings("unchecked")
	public Criteria buildCriteria(Criteria criteria, QueryObject queryObject) throws Exception {
    	ConcurrentMap aliasesMap = new ConcurrentHashMap();
        if (queryObject == null) {
            return criteria;
        }
        
        String aliasKey = RandomGenerator.randomString(12);
        if (queryObject.getQueryProjections() != null) {
	        QueryProjections queryProjections = queryObject.getQueryProjections();
	        ProjectionList projectionList = Projections.projectionList();
	        //因为此help作为单例调用, 在别名部分需要用不同的key以区分.
	        if (queryProjections.isRowCount()) {
	            if (queryProjections.isDistinctFlag()) {
	                projectionList.add(Projections.countDistinct(getSessionFactory().getClassMetadata(queryObject.getEntityClass()).getIdentifierPropertyName()));
	            } else {
	                projectionList.add(Projections.rowCount());
	            }
	        }
	        
	        if (queryProjections.isDistinctFlag() && !queryProjections.isRowCount()) {
	            /* 取记录时过滤重复数据 */
	            String identityPropertyName = getIdentityPropertyName(queryObject.getEntityClass());
	            projectionList.add(Projections.distinct(Projections.property(identityPropertyName)), ALIAS_PREFIX + identityPropertyName);
	            projectionList.add(getClassProjectionList(queryObject.getEntityClass()));
	            // criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	            // 用hibernate提供的方式在分页时候会有问题
	        } else if (!queryProjections.isRowCount()) {
	            if (queryProjections.getDistinct() != null) {
	                for (String distinct : queryProjections.getDistinct()) {
	                	projectionList.add(Projections.distinct(Projections.property(generateAlias(aliasesMap,criteria, distinct, aliasKey))));
	                }
	            }
	            
	            if (queryProjections.getProperty() != null) {
	                for (String property : queryProjections.getProperty()) {
	                    projectionList.add(Projections.property(generateAlias(aliasesMap,criteria, property, aliasKey)));
	                }
	            }
	            
	            if (queryProjections.getCountDistinct() != null) {
	                for (String countDistinct : queryProjections.getCountDistinct()) {
	                    projectionList.add(Projections.countDistinct(generateAlias(aliasesMap,criteria, countDistinct, aliasKey)));
	                }
	            }
	
	            if (queryProjections.getMax() != null) {
	                for (String max : queryProjections.getMax()) {
	                    projectionList.add(Projections.max(generateAlias(aliasesMap,criteria, max, aliasKey)));
	                }
	            }
	            
	            if (queryProjections.getMin() != null) {
	            	for (String min : queryProjections.getMin()) {
	                    projectionList.add(Projections.min(generateAlias(aliasesMap,criteria, min, aliasKey)));
	                }
	            }
	            
	            if (queryProjections.getCount() != null) {
	            	for (String count : queryProjections.getCount()) {
	                    projectionList.add(Projections.count(generateAlias(aliasesMap,criteria, count, aliasKey)));
	                }
	            }
	            
	            if (queryProjections.getAvg() != null) {
	            	for (String avg : queryProjections.getAvg()) {
	                    projectionList.add(Projections.avg(generateAlias(aliasesMap,criteria, avg, aliasKey)));
	                }
	            }
	            
	            if (queryProjections.getSum() != null) {
	            	for (String sum : queryProjections.getSum()) {
	                    projectionList.add(Projections.sum(generateAlias(aliasesMap,criteria, sum, aliasKey)));
	                }
	            }
	        }
	        
	        if (queryProjections.getGroupProperty() != null) {
	        	for (String groupProperty : queryProjections.getGroupProperty()) {
	                projectionList.add(Projections.groupProperty(generateAlias(aliasesMap,criteria, groupProperty, aliasKey)));
	            }
	        }
	        
	        if (queryProjections.getOrderProperty() != null) {
	            String[] orderProperty = queryProjections.getOrderProperty();
	            boolean[] descFlag = queryProjections.getDescFlag();
	            boolean descFlagState = descFlag != null && descFlag.length == orderProperty.length;
	            for (int i = 0, max = orderProperty.length; i < max; i++) {
	                if (descFlagState && descFlag[i]) {
	                    criteria.addOrder(Order.desc(generateAlias(aliasesMap,criteria, orderProperty[i], aliasKey)));
	                } else {
	                    criteria.addOrder(Order.asc(generateAlias(aliasesMap,criteria, orderProperty[i], aliasKey)));
	                }
	            }
	        }
	        
	        if (projectionList.getLength() > 0) {
	            criteria.setProjection(projectionList);
	            if (queryProjections.isDistinctFlag() && !queryProjections.isRowCount()) {
	                criteria.setResultTransformer(new PrefixAliasToBeanResultTransformer(queryObject.getEntityClass()));
	            }
	        }
        }
        QueryParam queryParam = queryObject.getQueryParam();
        if (queryParam != null) {
            criteria.add(processParameter(aliasesMap,criteria, queryParam, aliasKey));
            clearAlias(aliasesMap,aliasKey);
        }
        return criteria;
    }
    
    /**
     * Assemble the SQL which carry out the sum of results.
     * 
     * @param SQL
     * @param the summed column's name
     * @param alias
     * @return assembled SQL
     */
    public static String getCountSql(String sql, String paginationKey, String alias) {
        String trueAlias = (alias == null) ? COUNT_ALIAS : alias;
        if (StringUtils.isEmpty(sql)) {
            return null;
        } else {
            sql = StringUtils.lowerCase(sql);
        }
        return (new StringBuilder()).append("select count(").append(paginationKey).append(") as ").append(trueAlias).append(" ").append(sql.substring(sql.indexOf("from"))).toString();
    }

    /**
     * Check whether write operations are allowed on the given Session.
     * <p>Default implementation throws an InvalidDataAccessApiUsageException in
     * case of <code>FlushMode.NEVER/MANUAL</code>.
     * 
     * @param the instance of template
     * @param current hibernate session
     * @throws InvalidDataAccessApiUsageException if write operations are not allowed
     * @see #setCheckWriteOperations
     * @see #getFlushMode()
     * @see #FLUSH_EAGER
     * @see org.hibernate.Session#getFlushMode()
     * @see org.hibernate.FlushMode#NEVER
     * @see org.hibernate.FlushMode#MANUAL
     */
    public static void checkWriteOperationAllowed(HibernateTemplate template, Session session) throws InvalidDataAccessApiUsageException {
        if (template.isCheckWriteOperations() && template.getFlushMode() != HibernateTemplate.FLUSH_EAGER &&
                session.getFlushMode().lessThan(FlushMode.COMMIT)) {
            throw new InvalidDataAccessApiUsageException(
                    "Write operations are not allowed in read-only mode (FlushMode.NEVER/MANUAL): " +
                    "Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.");
        }
    }
    

    

    
    /**
     * 根据name生成获得别名（多个别名以|分割）并以当前的aliasKey保存在HashMap中 因为该DAO为单例，所以需要一个进入query时唯一的aliasKey作为区分
     * 
     * @param criteria
     * @param name
     * @param aliasKey
     * @return
     
    private String generateAlias(Criteria criteria, String name, String aliasKey) {
        int aliasPos = StringUtils.indexOf(name, ".");
        if (aliasPos != -1) {
            String alias = StringUtils.substringBefore(name, ".");
            if(aliasesMap==null){
            	aliasesMap = new HashMap();
            }
            if(aliasesMap.get(alias)==null){
            	criteria = criteria.createAlias(alias, alias);
            	aliasesMap.put(alias, alias);
            }

        }
        return name;
    }*/
    
    @SuppressWarnings("unchecked")
	private String generateAlias(ConcurrentMap aliasesMap,Criteria criteria, String name, String aliasKey) throws Exception{
    	Class<?> o;
    	o =Class.forName(BeanUtils.getProperty(criteria, "entityOrClassName"));
    	
        int aliasPos = StringUtils.indexOf(name, ":");
        if (aliasPos != -1) {
        	int joinType = name.charAt(aliasPos - 1) == '[' ? (name.charAt(aliasPos + 1) == ']' ? 
        			CriteriaSpecification.FULL_JOIN : CriteriaSpecification.LEFT_JOIN) : CriteriaSpecification.INNER_JOIN;
            String trueName = (joinType == CriteriaSpecification.FULL_JOIN) ? StringUtils.substring(name, aliasPos + 2) : StringUtils.substring(name, aliasPos + 1);
            if (StringUtils.indexOf(trueName, ".") == -1) {
                logger.error("ERROR:" + name + " -- the parameter name with alias should contain a . char!");
                return null;
            }
            String alias = StringUtils.substringBefore(trueName, ".");
            if (aliasesMap == null) {
                aliasesMap = new ConcurrentHashMap();
            }
            String aliases = aliasesMap.get(aliasKey).toString();
            if (aliases == null)
                aliases = "";
            if (StringUtils.indexOf(aliases, alias + '|') == -1) {
            	if(aliasesMap.get(alias)==null){
	                criteria = joinType == CriteriaSpecification.INNER_JOIN ? criteria.createAlias(StringUtils.substringBefore(name, ":"), alias, joinType)
	                		: criteria.createAlias(alias, alias, joinType);
	                aliases = aliases + alias + '|';
	                aliasesMap.put(aliasKey, aliases);
            	}
            }
            name = trueName;
            if (StringUtils.lastIndexOf(name, ":") != -1) {
                name = generateAlias(aliasesMap,criteria, name, aliasKey);
            }
        }
        else{
        	aliasPos = StringUtils.indexOf(name, ".");
            if (aliasPos != -1) {
                String alias = StringUtils.substringBefore(name, ".");
                
                if(aliasesMap==null){
                	aliasesMap = new ConcurrentHashMap();
                }
                Class type;
            	try{
            		type =BeanUtils.getPropertyType(o, alias);
            	}catch(Exception e){
            		throw new RuntimeException("未找到查询字段");
            	}
                if(aliasesMap.get(type.getName())==null||(aliasesMap.get(type.getName())!=null&&((Map)aliasesMap.get(type.getName())).get(alias)==null)){
                	criteria = criteria.createAlias(alias, alias,CriteriaSpecification.LEFT_JOIN);
                	if(aliasesMap.get(type.getName())==null){
                	   HashMap map = new HashMap();
                	   map.put(alias, alias);
                	   aliasesMap.put(type.getName(), map);
                	}else{
                		((Map)aliasesMap.get(type.getName())).put(alias, alias);
                	}
                	if(name.split("[.]").length>2){
                		return	generateAliasChild(aliasesMap,criteria,name,StringUtils.substring(name, aliasPos+1),0,type,name.split("[.]")[0]);
                	}
                }else{
//                	if(aliasesMap.get(type.getName())!=null&&!alias.equals(aliasesMap.get(type.getName()))){
//                		criteria = criteria.createAlias(alias, aliasesMap.get(type.getName()).toString(),CriteriaSpecification.LEFT_JOIN);
//                		if(name.split("[.]").length>2){
//                    		return	generateAliasChild(criteria,name,StringUtils.substring(name, aliasPos+1),0,o);
//                    	}
//                	}
                	if(name.split("[.]").length>2){
                		return	generateAliasChild(aliasesMap,criteria,name,StringUtils.substring(name, aliasPos+1),0,type,name.split("[.]")[0]);
                	}
                }

            }
        }
        	
        return name;
    }
    
    @SuppressWarnings("unchecked")
	private String generateAliasChild(ConcurrentMap aliasesMap,Criteria criteria,String fistName, String name, int index,Class o,String part){
    	Class<?> type;
    	String alias = StringUtils.substringBefore(name, ".");
    	try{
    		type =BeanUtils.getPropertyType(o, alias);
    	}catch(Exception e){
    		throw new RuntimeException("未找到查询字段");
    	}
    	String [] names=fistName.split("[.]");
    	int aliasPos = StringUtils.indexOf(name, ".");
    	 StringBuffer key1=new StringBuffer("");
         for(int i=0;i<=index+1;i++){
         	key1.append(names[i]).append(".");
         }
         key1.setLength(key1.length()-1);
        if (aliasPos != -1) {
            
            if(aliasesMap==null){
            	aliasesMap = new ConcurrentHashMap();
            }
            StringBuffer key=new StringBuffer("");
            for(int i=index;i<=index+1;i++){
            	key.append(names[i]).append(".");
            }
            key.setLength(key.length()-1);
            //.replaceAll("[.]", "_")改为.replaceAll("[.]", "_")
            if(aliasesMap.get(type.getName())==null||(aliasesMap.get(type.getName())!=null&&((Map)aliasesMap.get(type.getName())).get(part+"."+alias)==null)){
            	criteria = criteria.createAlias(part+"."+alias,key1.toString().replaceAll("[.]", "_"),CriteriaSpecification.LEFT_JOIN);
            	if(aliasesMap.get(type.getName())==null){
             	   HashMap map = new HashMap();
             	   map.put(part+"."+alias, alias);
             	   aliasesMap.put(type.getName(), map);
             	}else{
             		((Map)aliasesMap.get(type.getName())).put(part+"."+alias, alias);
             	}
            	if(name.split("[.]").length>2){
            		String rname= generateAliasChild(aliasesMap,criteria,fistName,StringUtils.substring(name, aliasPos+1),index+1,type,key1.toString().replaceAll("[.]", "_"));
            		if(rname.equals(StringUtils.substring(name, aliasPos+1))&&rname.indexOf(".")==-1){
            			return name.replace(alias, key1.toString().replaceAll("[.]", "_"));
            		}
            		return rname;
            	}
            }else{
//            	if(aliasesMap.get(type.getName())!=null&&!alias.equals(aliasesMap.get(type.getName()))){
//            		criteria = criteria.createAlias(key.toString(),aliasesMap.get(type.getName()).toString(),CriteriaSpecification.LEFT_JOIN);
//                	aliasesMap.put(type.getName(), key.toString());
//                	if(name.split("[.]").length>2){
//                		String rname= generateAliasChild(criteria,fistName,StringUtils.substring(name, aliasPos+1),index+1,type);
//                		if(rname.equals(StringUtils.substring(name, aliasPos+1))&&rname.indexOf(".")==-1){
//                			return name;
//                		}
//                		return rname;
//                	}
//            	}
            	if(name.split("[.]").length>2){
            		String rname= generateAliasChild(aliasesMap,criteria,fistName,StringUtils.substring(name, aliasPos+1),index+1,type,key1.toString().replaceAll("[.]", "_"));
            		if(rname.equals(StringUtils.substring(name, aliasPos+1))&&rname.indexOf(".")==-1){
            			return name.replace(alias, key1.toString().replaceAll("[.]", "_"));
            		}
            		return rname;
            	}
            }

        }
    	return name.replace(alias+".", key1.toString().replaceAll("[.]", "_")+".");//增加"."区别goods.goodsClass.goodsClassName.    Clazz
    }
    /**
     * 将queryParam转化为Hibernate的Criterion
     * 
     * @param criteria
     * @param queryParam
     * @param aliasKey
     * @return
     * @throws HibernateException
     */
    @SuppressWarnings("unchecked")
    public Criterion processParameter(ConcurrentMap aliasesMap,Criteria criteria, QueryParam queryParam, String aliasKey) throws Exception{
        Criterion criterion = null;
        if (queryParam.getMode() == QueryParam.BASIC) {
            if (queryParam.getSql() != null) {
                criterion = Restrictions.sqlRestriction(queryParam.getSql());
            } else if (queryParam.getName() != null) {
                boolean notFlag = false;
                boolean ansiNull = true;
                String operator = queryParam.getOperator();
                String name = generateAlias(aliasesMap,criteria, queryParam.getName(), aliasKey);
                if (operator.startsWith("!")) {
                    notFlag = true;
                    if (StringUtils.equals(QueryParam.OPERATOR_NE_ANSINULL_OFF, operator)) {
                        ansiNull = false;
                        operator = "=";
                    } else {
                        operator = operator.substring(1);
                    }
                }
                if (queryParam.getValue() == null) {
                    criterion = Restrictions.isNull(name);
                } else if (queryParam.getType() != -1) {
                    criterion = Restrictions.sqlRestriction((new StringBuffer()).append(name).append(' ').append(operator).append(  " ?").toString(), queryParam.getValue(), HibernateTypeConvertor.getTypeBySqlType(queryParam.getType()));
                } else if (QueryParam.FETCH.equals(operator)) {
                	if (queryParam.getValue() != null && queryParam.getValue() instanceof FetchMode) {
                		criteria.setFetchMode(name, (FetchMode)queryParam.getValue());
                	} else {
                		logger.error("error fetch mode for :" + name);
                	}
                	
                } else {
                    if (QueryParam.OPERATOR_EQ.equals(operator)) {
                        criterion = Restrictions.eq(name, queryParam.getValue());
                    } else if (QueryParam.OPERATOR_GE.equals(operator)) {
                        criterion = Restrictions.ge(name, queryParam.getValue());
                    } else if (QueryParam.OPERATOR_GT.equals(operator)) {
                        criterion = Restrictions.gt(name, queryParam.getValue());
                    } else if (QueryParam.OPERATOR_LE.equals(operator)) {
                        criterion = Restrictions.le(name, queryParam.getValue());
                    } else if (QueryParam.OPERATOR_LT.equals(operator)) {
                        criterion = Restrictions.lt(name, queryParam.getValue());
                    } else if (QueryParam.OPERATOR_LIKE.equals(operator)) {
                        if (queryParam.getValue() instanceof String) {
                            criterion = Restrictions.like(name, (String) queryParam.getValue());
                        } else {
                            logger.error("The value type -- " + queryParam.getValue().getClass().getName()
                                    + " not match the operator:like");
                        }
                    } else if (QueryParam.OPERATOR_INCLUDE.equals(operator)) {
                        if (queryParam.getValue() instanceof String) {
                            criterion = Restrictions.like(name, '%' + (String) queryParam.getValue() + '%');
                        } else {
                            logger.error("The value type -- " + queryParam.getValue().getClass().getName()
                                    + " not match the operator:like");
                        }
                    } else if (QueryParam.OPERATOR_ILIKE.equals(operator)) {
                        if (queryParam.getValue() instanceof String) {
                            criterion = Restrictions.ilike(name, (String) queryParam.getValue());
                        } else {
                            logger.error("The value type -- " + queryParam.getValue().getClass().getName()
                                    + " not match the operator:like");
                        }
                    } else if (QueryParam.OPERATOR_IINCLUDE.equals(operator)) {
                        if (queryParam.getValue() instanceof String) {
                            criterion = Restrictions.ilike(name, '%' + (String) queryParam.getValue() + '%');
                        } else {
                            logger.error("The value type -- " + queryParam.getValue().getClass().getName()
                                    + " not match the operator:like");
                        }
                    } else if (QueryParam.OPERATOR_IN.equals(operator) && queryParam.getValue() instanceof Collection) {
                    	Collection ins = (Collection) queryParam.getValue();
                    	if (ins.size() > 0) {
	                        criterion = Restrictions.in(name, ins);
                    	} else {
                    		criterion = Restrictions.sqlRestriction("1 != 1");
                    	}
                    }else {
                        logger.error("invalid operator!");
                    }
                }
                if (notFlag) {
                    if (!ansiNull) {
                        criterion = Restrictions.or(Restrictions.not(criterion), Restrictions.isNull(name));
                    } else {
                        criterion = Restrictions.not(criterion);
                    }
                }
            }
        } else {
            // and each
            List<QueryParam> andParams = queryParam.getAndParams();
            if (andParams != null && andParams.size() > 0) {
                Conjunction conjunction = Restrictions.conjunction();
                for (QueryParam innerQueryParam : andParams) {
                    conjunction.add(processParameter(aliasesMap,criteria, innerQueryParam, aliasKey));
                }
                criterion = conjunction;
            }
            // or each
            List<QueryParam> orParams = queryParam.getOrParams();
            if (orParams != null && orParams.size() > 0) {
                for (QueryParam innerQueryParam : orParams) {
                    if (criterion == null) {
                        criterion = processParameter(aliasesMap,criteria, innerQueryParam, aliasKey);
                    } else {
                        criterion = Restrictions.or(criterion, processParameter(aliasesMap,criteria, innerQueryParam, aliasKey));
                    }
                }
            }
            // not each
            List<QueryParam> notParams = queryParam.getNotParams();
            if (notParams != null && notParams.size() > 0) {
                for (QueryParam innerQueryParam : notParams) {
                    if (criterion == null) {
                        criterion = Restrictions.not(processParameter(aliasesMap,criteria, innerQueryParam, aliasKey));
                    } else {
                        criterion = Restrictions.and(criterion, Restrictions.not(processParameter(aliasesMap,criteria,
                                innerQueryParam, aliasKey)));
                    }
                }
            }
        }
        return criterion == null ? Restrictions.sqlRestriction("1 = 1") : criterion;
    }
    
    /**
     * 内部类，服务于distinct，实现distinct整个类，但关联数据则不能获得
     * 
     * @author leon
     */
    @SuppressWarnings({ "unchecked", "serial" })
    class PrefixAliasToBeanResultTransformer implements ResultTransformer {
        
        private final Class resultClass;
        
        private Setter[] setters;
        
        private PropertyAccessor propertyAccessor;
        
        public PrefixAliasToBeanResultTransformer(Class resultClass) {
            if (resultClass == null)
                throw new IllegalArgumentException("resultClass cannot be null");
            this.resultClass = resultClass;
            propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] {
                    PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
                    PropertyAccessorFactory.getPropertyAccessor("field") });
        }
        
        public Object transformTuple(Object[] tuple, String[] aliases) {
            Object result;
            
            try {
                if (setters == null) {
                    setters = new Setter[aliases.length];
                    
                    for (int i = 0; i < aliases.length; i++) {
                        String alias = aliases[i];
                        
                        if (alias != null) {
                            if (alias.startsWith(ALIAS_PREFIX)) {
                                alias = alias.substring(ALIAS_PREFIX.length());
                            }
                            
                            setters[i] = propertyAccessor.getSetter(resultClass, alias);
                        }
                    }
                }
                
                result = resultClass.newInstance();
                
                for (int i = 0; i < aliases.length; i++) {
                    if (setters[i] != null) {
                        setters[i].set(result, tuple[i], null);
                    }
                }
            } catch (InstantiationException e) {
                throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
            } catch (IllegalAccessException e) {
                throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
            }
            
            return result;
        }
        
        public List transformList(List collection) {
            return collection;
        }
        
    }
    
    /**
     * 获取持久类主键属性名
     * 
     * @param entityClass
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getIdentityPropertyName(Class entityClass) {
        ClassMetadata classMetadata = getSessionFactory().getClassMetadata(entityClass);
        return classMetadata.getIdentifierPropertyName();
    }
    
    /**
     * 获取所有属性名的投影
     * 
     * @param entityClass
     * @return
     */
    @SuppressWarnings("unchecked")
    private Projection getClassProjectionList(Class entityClass) {
        ClassMetadata classMetadata = getSessionFactory().getClassMetadata(entityClass);
        // String identityPropertyName =
        // classMetadata.getIdentifierPropertyName();
        String[] properties = classMetadata.getPropertyNames();
        ProjectionList list = Projections.projectionList();
        // list.add(Projections.property(identityPropertyName),
        // identityPropertyName);
        for (int i = 0; i < properties.length; ++i) {
            list.add(Projections.property(properties[i]), ALIAS_PREFIX + properties[i]);
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
	public void clearAlias(ConcurrentMap aliasesMap,String alias) {
        if (aliasesMap != null) {
            /* 清除别名用到的临时变量 */
            aliasesMap.remove(alias);
        }
    }
    

}
