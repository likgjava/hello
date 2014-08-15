package com.likg.core.dao.hibernate.query;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.likg.core.util.CollectionUtils;
import com.likg.core.util.DateUtil;
import com.likg.core.util.ObjectUtils;

/**
 * @author xiaojf
 * 
 */
public class QueryWebUtils {

	private static final Log logger = LogFactory.getLog(QueryWebUtils.class);

	private static final String INTEGER = "Integer";
	private static final String LONG = "Long";
	private static final String DOUBLE = "Double";
	private static final String FLOAT = "Float";
	private static final String BOOLEAN = "Boolean";
	private static final String STRING = "String";
	private static final String BIGDECIMAL = "BigDecimal";
	private static final String DATE = "Date";

	public static int queryCount = 3; //查询层次

	//private static final String TOLIST = "%List";
	//private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/**
	 * 根据request传递的参数组装queryObject对象
	 * @param request
	 * @param commandclass 实体对象类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static QueryObject getQuery(HttpServletRequest request, Class commandclass) throws Exception {
		QueryObject query = new QueryObjectBase();
		QueryParam queryParam = new QueryParam();
		List<String> searchName = new ArrayList<String>(); //查询名称
		List<String> searchOperator = new ArrayList<String>(); //查询操作符
		List<Object> actualValues = new ArrayList<Object>(); //实际参数值
		List<String> searchRelative = new ArrayList<String>(); //
		PropertyDescriptor[] propertyDescs = BeanUtils.getPropertyDescriptors(commandclass);
		for (int i=0; i<propertyDescs.length; i++) {
			transferQuery(propertyDescs[i], searchName, searchOperator, actualValues, searchRelative, request, propertyDescs[i].getName());
		}

		//转换操作符
		for (int i=0; i<searchOperator.size(); i++) {
			Object operator = searchOperator.get(i);
			if ("ge".equals(operator))
				searchOperator.set(i, ">=");
			else if ("le".equals(searchOperator.get(i)))
				searchOperator.set(i, "<=");
			else if ("gt".equals(operator))
				searchOperator.set(i, ">");
			else if ("lt".equals(operator))
				searchOperator.set(i, "<");
		}

		if (!searchName.isEmpty() && !searchOperator.isEmpty() && !actualValues.isEmpty() && !searchRelative.isEmpty()) {
			queryParam = generateQueryParam(CollectionUtils.toStringArray(searchName), CollectionUtils.toStringArray(searchOperator), 
					actualValues.toArray(), CollectionUtils.toStringArray(searchRelative));
		}
		query.setEntityClass(commandclass);
		query.setQueryParam(queryParam);

		String sortname = request.getParameter("sortname");// grid排序列名参数
		String sortorder = request.getParameter("sortorder");// grid排序符号参数
		String distinct = request.getParameter("distinct");// 排除唯一 by wangsw
		// 2010.4.23
		if (distinct != null) {
			if (query.getQueryProjections() == null) {
				query.setQueryProjections(new QueryProjections());
			}
			query.getQueryProjections().setDistinct(distinct.split(","));
		}

		String order = request.getParameter("order");
		if (sortname != null && !sortname.equals(""))
			order = order == null ? sortname : sortname + "," + order;// 追加排序
		if (order != null) {
			if (query.getQueryProjections() == null) {
				query.setQueryProjections(new QueryProjections());
			}
			query.getQueryProjections().setOrderProperty(order.split(","));
		}
		String flag = request.getParameter("order_flag");
		if (sortorder != null && !sortorder.equals("")) {
			sortorder = sortorder.equals("asc") ? "false" : "true";
			flag = flag == null ? sortorder : sortorder + "," + flag;// 追加排序
		}
		if (flag != null) {
			if (query.getQueryProjections() == null) {
				query.setQueryProjections(new QueryProjections());
			}
			boolean[] flags = new boolean[flag.split(",").length];
			for (int i = 0; i < flag.split(",").length; i++) {
				flags[i] = Boolean.valueOf(flag.split(",")[i]);
			}
			query.getQueryProjections().setDescFlag(flags);
		}
		return query;
	}

	/**
	 * 转换查询
	 * @param propertyDescs 属性描述符
	 * @param searchName 查询名称
	 * @param searchOperator 查询操作符
	 * @param actualValues
	 * @param searchRelative
	 * @param request
	 * @param propertyName
	 * @throws Exception
	 */
	private static void transferQuery(PropertyDescriptor propertyDescs, List<String> searchName, List<String> searchOperator, List<Object> actualValues,
			List<String> searchRelative, HttpServletRequest request, String propertyName) throws Exception {

		//属性为BaseObject类型的对象
		if (ObjectUtils.isObject(propertyDescs)) {
			String obj = request.getParameter(propertyDescs.getName());
			if (StringUtils.isNotBlank(obj)) {
				String op = request.getParameter(propertyDescs.getName()+"_op");
				if (op!=null && (QueryParam.OPERATOR_IS.equals(op) || QueryParam.OPERATOR_NIS.equals(op))) {
					String joinType = request.getParameter(propertyDescs.getName()+"_joinType");
					if (joinType == null) {
						joinType = "";
					}
					searchName.add(propertyDescs.getName() + joinType);
					searchOperator.add(op);
					String relative = request.getParameter(propertyDescs.getName()+"_relative");
					if (StringUtils.isBlank(relative)) {
						relative = "and";
					}
					actualValues.add(obj);
					searchRelative.add(relative);
				}
			}
			
			PropertyDescriptor[] innerpropertyDescs = BeanUtils.getPropertyDescriptors(propertyDescs.getPropertyType());
			if (propertyName.split("[.]").length - 1 >= queryCount) {
				return;
			}
			for (int j=0; j<innerpropertyDescs.length; j++) {
				transferQuery(innerpropertyDescs[j], searchName, searchOperator, actualValues, searchRelative, request,
						propertyName+"."+innerpropertyDescs[j].getName());
			}
			return;
		}
		
		//属性为基本类型
		String value = request.getParameter(propertyName);
		if (StringUtils.isNotBlank(value)) {
			String op = request.getParameter(propertyName+"_op");
			if (StringUtils.isBlank(op)) {
				op = "=";
			}
			
			String relative = request.getParameter(propertyName+"_relative");
			if (StringUtils.isBlank(relative)) {
				relative = "and";
			}
			
			String joinType = request.getParameter(propertyName+"_joinType");
			if (joinType == null) {
				joinType = "";
			}
			
			String originalValue = URLDecoder.decode(value, "UTF-8");
			//between . and . 查询
			if (QueryParam.OPERATOR_BT.equals(op)) {
				String[] values = originalValue.split(",");
				if (values.length != 2) {
					throw new Exception("between查询,值必须为2个");
				}
				Object obj1 = transferValueImpl(propertyDescs.getPropertyType().getSimpleName(), values[0]);
				Object obj2 = transferValueImpl(propertyDescs.getPropertyType().getSimpleName(), values[1]);
				searchName.add(propertyName + joinType);
				actualValues.add(obj1);
				searchOperator.add(QueryParam.OPERATOR_GE);
				searchRelative.add(relative);

				searchName.add(propertyName + joinType);
				actualValues.add(obj2);
				searchOperator.add(QueryParam.OPERATOR_LE);
				searchRelative.add(relative);
			} else {
				Object paraValue = transferValueImpl(propertyDescs.getPropertyType().getSimpleName(), originalValue);
				searchName.add(propertyName + joinType);
				actualValues.add(paraValue);
				searchOperator.add(op);
				searchRelative.add(relative);
			}
		}
	}

	/*public static Object transferValue(String type, String originalValue) {
		Object result = null;
		if (StringUtils.isBlank(originalValue)) {
			return null;
		}
		boolean listFlag = false;
		if (type.endsWith(TOLIST)) {
			type = type.substring(0, type.length() - TOLIST.length());
			listFlag = true;
		}

		if (listFlag) {
			result = new ArrayList();
			String[] strValues = originalValue.split("\\|");
			for (String theValue : strValues) {
				((ArrayList) result).add(transferValueImpl(type, theValue));
			}
		} else {
			result = transferValueImpl(type, originalValue);
		}

		return result;
	}
*/
	/**
	 * 把传递参数转换为实际对象类型
	 * @param type 参数实际类型
	 * @param originalValue 原生参数值
	 * @return
	 */
	public static Object transferValueImpl(String type, String originalValue) {
		Object result = null;
		originalValue = originalValue.trim();
		try {
			if (type==null || STRING.equals(type)) {
				result = originalValue;
			} else if (INTEGER.equals(type)) {
				result = Integer.valueOf(originalValue);
			} else if (LONG.equals(type)) {
				result = Long.valueOf(originalValue);
			} else if (BIGDECIMAL.equals(type)) {
				result = new BigDecimal(originalValue);
			} else if (DOUBLE.equals(type)) {
				result = Double.valueOf(originalValue);
			} else if (FLOAT.equals(type)) {
				result = Float.valueOf(originalValue);
			} else if (BOOLEAN.equals(type)) {
				result = Boolean.valueOf(originalValue);
			} else if (type.startsWith(DATE) && StringUtils.isNotBlank(originalValue)) {
				if (originalValue.indexOf(":") != -1) {
					result = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", originalValue);
				} else {
					result = DateUtil.convertStringToDate("yyyy-MM-dd", originalValue);
				}
			} else {
				result = originalValue;
			}
		} catch (Exception e) {
			logger.warn("Error occured when parse data: type -- " + type + ", value --" + originalValue);
		}
		return result;
	}

	/*public static Object[] transferValues(String[] searchName,
			String[] searchValue) {
		Object[] values = null;
		if (searchName != null && searchValue != null
				&& searchName.length == searchValue.length) {
			values = new Object[searchValue.length];
			for (int i = 0, max = searchValue.length; i < max; i++) {
				values[i] = transferValue(getNameType(searchName[i]),
						searchValue[i]);
			}
		}
		return values;
	}
*/
	public static String[] transferNames(String[] searchName) {
		String[] trueName = null;
		if (searchName != null) {
			trueName = new String[searchName.length];
			for (int i = 0, max = searchName.length; i < max; i++) {
				trueName[i] = transferName(searchName[i]);
			}
		}
		return trueName;
	}

	public static String transferName(String searchName) {
		int pos = searchName.lastIndexOf('_');
		return pos == -1 ? searchName : searchName.substring(0, pos);
	}

	public static String getNameType(String searchName) {
		int pos = searchName.lastIndexOf('_');
		return searchName.substring(pos + 1);
	}

	/**
	 * public static QueryParam generateQueryParam(String queryString) { if
	 * (queryString != null && queryString.indexOf(",") != -1) { String[] qs =
	 * queryString.split(","); String[] names = new String[qs.length]; String[]
	 * operators = new String[qs.length]; String[] values = new
	 * String[qs.length]; String[] q = null; for (int i = 0; i < qs.length; i++)
	 * { q = qs[i].split(" "); if (q.length != 3) { throw new
	 * RuntimeException("queryString error:" + queryString); } names[i] = q[0];
	 * operators[i] = q[1]; values[i] = q[2]; } return generateQueryParam(names,
	 * operators, values); } else if (queryString != null &&
	 * queryString.indexOf(" ") != -1) { String[] q = queryString.split(" "); if
	 * (q.length == 3) { return generateQueryParam(transferName(q[0]), q[1],
	 * transferValue(getNameType(q[0]), q[2])); } else { throw new
	 * RuntimeException("queryString error:" + queryString); } } else { throw
	 * new RuntimeException("queryString is null:" + queryString); } }
	 */

	/**
	 * 根据查询字段、操作符、值，获取查询参数对象
	 * @param name 查询名字
	 * @param operator 操作符
	 * @param value 值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static QueryParam generateQueryParam(String name, String operator, Object value) {
		QueryParam innerQueryParam = null;
		if (operator.indexOf("and_") != -1 && value instanceof List) {
			innerQueryParam = new QueryParam();
			List valueList = (List) value;
			for (int x = valueList.size() - 1; x >= 0; x--) {
				innerQueryParam.and(new QueryParam(name, operator.replaceFirst("and_", ""), valueList.get(x)));
			}

		} else if (operator.indexOf("or_") != -1 && value instanceof List) {
			innerQueryParam = new QueryParam();
			List valueList = (List) value;
			for (int x = valueList.size() - 1; x >= 0; x--) {
				innerQueryParam.or(new QueryParam(name, operator.replaceFirst("or_", ""), valueList.get(x)));
			}

		} else if (operator.indexOf("not_") != -1 && value instanceof List) {
			innerQueryParam = new QueryParam();
			List valueList = (List) value;
			for (int x = valueList.size() - 1; x >= 0; x--) {
				innerQueryParam.not(new QueryParam(name, operator.replaceFirst("not_", ""), valueList.get(x)));
			}
		} else if (operator.indexOf("sql_") != -1) {
			innerQueryParam = new QueryParam(name+' '+operator.replaceFirst("sql_", "")+' '+value);
		} else if (QueryParam.OPERATOR_IN.equals(operator) || QueryParam.OPERATOR_NIN.equals(operator)) {// 扩展支持in查询
			innerQueryParam = new QueryParam(name, operator, Arrays.asList(value.toString().split(",")));
		} else {
			innerQueryParam = new QueryParam(name, operator, value);
		}
		return innerQueryParam;
	}

	/**
	 * 根据查询信息获取查询参数对象
	 * @param names 查询名称
	 * @param operators 操作符
	 * @param values 值
	 * @param relatives
	 * @return
	 */
	public static QueryParam generateQueryParam(String[] names, String[] operators, Object[] values, String[] relatives) {
		QueryParam queryParam = new QueryParam();

		Map<String, List<QueryParam>> andParameters = null;
		Map<String, List<QueryParam>> orParameters = null;
		Map<String, List<QueryParam>> notParameters = null;

		for (int i=0, max=relatives.length; i<max; i++) {
			QueryParam innerQueryParam = null;
			String relative = relatives[i];
			boolean groupFlag = relative.startsWith("[");
			int pos = relative.indexOf(":");
			if (groupFlag && relative.indexOf("]") > pos) {
				int posEnd = relative.indexOf("]");
				innerQueryParam = generateQueryParam(names[i], operators[i], values[i]);
				if (pos == -1) {
					// key not exists, like [and]xxx instead of [and:xx]xxx
					String operator = relative.substring(1, posEnd);

					if (operator.toLowerCase().equals("and")) {
						queryParam.and(innerQueryParam);
					} else if (operator.toLowerCase().equals("or")) {
						queryParam.or(innerQueryParam);
					} else if (operator.toLowerCase().equals("not")) {
						queryParam.not(innerQueryParam);
					}
				} else {
					String operator = relative.substring(1, pos);
					String key = relative.substring(pos + 1, posEnd);
					if (operator.toLowerCase().equals("and")) {
						andParameters = putParameters(andParameters, key,
								innerQueryParam);
					} else if (operator.toLowerCase().equals("or")) {
						orParameters = putParameters(orParameters, key,
								innerQueryParam);
					} else if (operator.toLowerCase().equals("not")) {
						notParameters = putParameters(notParameters, key,
								innerQueryParam);
					}
				}
			} else {
				innerQueryParam = generateQueryParam(names[i], operators[i],
						values[i]);
				if (innerQueryParam == null) {
					continue;
				}
				queryParam.and(innerQueryParam);
			}
		}
		
		
		if (andParameters != null) {
			QueryParam innerQueryParam = new QueryParam();
			for (String key : andParameters.keySet()) {
				List<QueryParam> parameters = andParameters.get(key);
				if (parameters != null && parameters.size() > 0) {
					QueryParam innerInnerQueryParam = new QueryParam();
					for (QueryParam p : parameters) {
						if (key.startsWith("or")) {
							innerInnerQueryParam.or(p);
						} else {
							innerInnerQueryParam.and(p);
						}
					}
					innerQueryParam.and(innerInnerQueryParam);
				}
			}
			queryParam.and(innerQueryParam);
		}
		if (orParameters != null) {
			QueryParam innerQueryParam = new QueryParam();
			for (String key : orParameters.keySet()) {
				List<QueryParam> parameters = orParameters.get(key);
				if (parameters != null && parameters.size() > 0) {
					QueryParam innerInnerQueryParam = new QueryParam();
					for (QueryParam p : parameters) {
						if (key.startsWith("or")) {
							innerInnerQueryParam.or(p);
						} else {
							innerInnerQueryParam.and(p);
						}
					}
					innerQueryParam.and(innerInnerQueryParam);
				}
			}
			queryParam.or(innerQueryParam);
		}
		if (notParameters != null) {
			QueryParam innerQueryParam = new QueryParam();
			for (String key : notParameters.keySet()) {
				List<QueryParam> parameters = notParameters.get(key);
				if (parameters != null && parameters.size() > 0) {
					QueryParam innerInnerQueryParam = new QueryParam();
					for (QueryParam p : parameters) {
						if (key.startsWith("or")) {
							innerInnerQueryParam.or(p);
						} else {
							innerInnerQueryParam.and(p);
						}
					}
					innerQueryParam.and(innerInnerQueryParam);
				}
			}
			queryParam.not(innerQueryParam);
		}
		return queryParam;
	}

	private static Map<String, List<QueryParam>> putParameters(
			Map<String, List<QueryParam>> parameterMap, String key,
			QueryParam queryParam) {
		if (parameterMap == null) {
			parameterMap = new HashMap<String, List<QueryParam>>();
		}
		List<QueryParam> parameters = parameterMap.get(key);
		if (parameters == null) {
			parameters = new ArrayList<QueryParam>();
		}
		parameters.add(queryParam);
		parameterMap.put(key, parameters);
		return parameterMap;
	}

	/**
	 * for the parameters from view layer(jsp, velocity or freemarker and so on)
	 * -- "search_name = [operator:innerOperator_somekey]name" the below example
	 * split search_name,search_operator,search_value with a char "," example:
	 * age,>,20 [and:a]id,>,100 [and:a]id,!=,200 [or:or_b]salary,>,200
	 * [or:or_b]salary,<,400 [or:and_a]age,=,60 [or:and_a]title,=,manager
	 * [not:or_a]age,=,30 [not:or_a]title,=,cto
	 * 
	 * then the generated sql like this: (age>20 and (age=60 or title=cto)) or
	 * (age=60 or title=cto) and not (age=60 or title=cto)
	 * 
	 * public static void main(String[] args) { List codes = new ArrayList();
	 * codes.add("238949234-e"); // Criteria criteria = null; //queryParam from
	 * view layer String[] names = { "usName", "org.name", "org.code",
	 * "usrIsExpired", "usrPwdExpired" }; String[] operators = { "rlike",
	 * "like", "in", "=", "=" }; //this objects are generated at action layer
	 * Object[] values = { "admin", "政采", codes, true, false }; String []
	 * relatives = {"[and]","[and:or_c]","","[or:or]","[or:or]"
	 * 
	 * }; QueryParam queryParam = QueryWebUtils.generateQueryParam(names,
	 * operators, values,relatives); Session session =
	 * HibernateUtil.getSessionFactory().openSession(); Criteria criteria =
	 * session.createCriteria(User.class);
	 * 
	 * HibernateDAOHelper h = new HibernateDAOHelper(); Criterion criterion =
	 * h.processParameter(criteria, queryParam, "fromview"); List users =
	 * criteria.add(criterion).list();
	 * 
	 * System.out.println("Plan SQL:" + queryParam);
	 * System.out.println("True SQL: "
	 * +AuthenticationHelper.convertToString(users, "usName") );
	 * 
	 * /**queryParam from service queryParam = new QueryParam();
	 * queryParam.and(new QueryParam("age", ">", new Integer(40))); QueryParam
	 * innerQueryParam = new QueryParam(); innerQueryParam.or(new
	 * QueryParam("title", "cto")); QueryParam innerInnerQueryParam = new
	 * QueryParam(); innerInnerQueryParam.and(new QueryParam("customers:c.name",
	 * "!like", "%Li")); innerInnerQueryParam.and(new
	 * QueryParam("customers:c.addresses:addr.description", "like", "%China%"));
	 * innerQueryParam.or(innerInnerQueryParam);
	 * queryParam.and(innerQueryParam); // criteria =
	 * HibernateUtils.currentSession().createCriteria(Employee.class);
	 * //criterion = dao.getHelper().processParameter(criteria, queryParam,
	 * "fromservice"); //System.out.println("True SQL: " + criterion);
	 * System.out.println("Plan SQL:" + queryParam);
	 * 
	 * session.close(); HibernateUtil.shutdown();
	 * //HibernateUtils.closeSession(); }
	 **/
}
