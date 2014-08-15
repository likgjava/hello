package com.likg.core.dao.hibernate.query;

import java.util.LinkedList;
import java.util.List;

/**
 * 查询参数对象<br/>
 * Advanced Query Parameters, can be nested unlimited. Example:
 * <pre>
 * QueryParam queryParam1 = new QueryParam(&quot;name&quot;, &quot;like&quot;, &quot;admin%&quot;);
 * QueryParam queryParam2 = new QueryParam(&quot;id&quot;, &quot;&gt;&quot;, new Integer(100));
 * QueryParam queryParam3 = new QueryParam(&quot;age&quot;, &quot;&lt;&quot;, new Integer(30));
 * QueryParam queryParam4 = new QueryParam(&quot;creationDate &gt; '2004-10-24'&quot;);
 * QueryParam queryParam = new QueryParam();
 * queryParam.and(queryParam1);
 * queryParam.and(queryParam2);
 * queryParam.not(queryParam3);
 * queryParam.not(queryParam4);
 * </pre>
 * 
 * the above code will generate the sql: 
 * where (name like admin% and id > 100) and not age < 30 and not creationDate > '2004-10-24')
 * <br/>
 * Note: A QueryParam can not be nested when his model is BASIC.
 * 
 * @author xiaojf
 */
public final class QueryParam {

	public final static String AND = "and";

	public final static String OR = "or";

	public final static String NOT = "not";

	public final static String OPERATOR_EQ = "=";

	public final static String OPERATOR_BT = "bt";

	public final static String OPERATOR_NE = "!=";

	public final static String OPERATOR_NE_ANSINULL_OFF = "!=%";

	public final static String OPERATOR_GE = ">=";

	public final static String OPERATOR_GT = ">";

	public final static String OPERATOR_NGE = "!>=";

	public final static String OPERATOR_NGT = "!>";

	public final static String OPERATOR_LE = "<=";

	public final static String OPERATOR_LT = "<";

	public final static String OPERATOR_NLE = "!<=";

	public final static String OPERATOR_NLT = "!<";

	public final static String OPERATOR_LIKE = "like";

	public final static String OPERATOR_LEFTLIKE = "llike";

	public final static String OPERATOR_RIGHTLIKE = "rlike";

	public final static String OPERATOR_NLIKE = "!like";

	public final static String OPERATOR_NLEFTLIKE = "!llike";
	
	public final static String OPERATOR_NRIGHTLIKE = "!rlike";

	public final static String OPERATOR_INCLUDE = "include";

	public final static String OPERATOR_NINCLUDE = "!include";

	public final static String OPERATOR_ILIKE = "ilike";

	public final static String OPERATOR_NILIKE = "!ilike";

	public final static String OPERATOR_IINCLUDE = "iinclude";

	public final static String OPERATOR_NIINCLUDE = "!iinclude";

	public final static String OPERATOR_IS = "is";

	public final static String OPERATOR_NIS = "!is";

	public final static String OPERATOR_IN = "in";

	public final static String OPERATOR_NIN = "!in";

	public final static String FETCH = "fetch";

	/**基本查询*/
	public final static int BASIC = 1;
	/**高级查询*/
	public final static int ADVANCED = 2;

	private String name;

	private Object value;

	private int type;

	private String operator = OPERATOR_EQ;

	private String sql;

	private int queryMode;

	private List<QueryParam> andParams;
	private List<QueryParam> orParams;
	private List<QueryParam> notParams;
	
	public QueryParam() {
	}

	/**
	 * 根据sql语句创建查询参数对象
	 * @param sql 例如："creationDate > '2004-10-24'"
	 */
	public QueryParam(String sql) {
		this.queryMode = BASIC;
		this.sql = sql;
	}

	/**
	 * @param name 查询字段
	 * @param operator 操作符
	 * @param value 值
	 * @param type
	 */
	public QueryParam(String name, String operator, Object value, int type) {
		this.queryMode = BASIC;
		setName(name);
		setOperator(operator);
		setValue(value);
		this.type = type;
		checkOperatorForNullValue();
	}

	/**
	 * @param name 查询字段
	 * @param operator 操作符
	 * @param value 值
	 */
	public QueryParam(String name, String operator, Object value) {
		this.queryMode = BASIC;
		setName(name);
		setOperator(operator);
		setValue(value);
		this.type = -1;
		checkOperatorForNullValue();
	}

	public QueryParam(String name, Object value, int type) {
		this.queryMode = BASIC;
		setName(name);
		setValue(value);
		this.type = type;
		checkOperatorForNullValue();
	}

	public QueryParam(String name, Object value) {
		this.queryMode = BASIC;
		setName(name);
		setValue(value);
		this.type = -1;
		checkOperatorForNullValue();
	}

	/**
	 * 添加and查询参数
	 * @param queryParam
	 */
	public void and(QueryParam queryParam) {
		if (this.queryMode == BASIC) {
			throw new RuntimeException("Current QueryParam was set as BASIC mode, can not be added a QueryParam!");
		}
		this.queryMode = ADVANCED;
		if (andParams == null) {
			andParams = new LinkedList<QueryParam>();
		}
		andParams.add(queryParam);
	}

	/**
	 * 添加or查询参数
	 * @param queryParam
	 */
	public void or(QueryParam queryParam) {
		if (this.queryMode == BASIC) {
			throw new RuntimeException("Current QueryParam was set as BASIC mode, can not be added a QueryParam!");
		}
		this.queryMode = ADVANCED;
		if (orParams == null) {
			orParams = new LinkedList<QueryParam>();
		}
		orParams.add(queryParam);
	}

	/**
	 * 添加not查询参数
	 * @param queryParam
	 */
	public void not(QueryParam queryParam) {
		if (this.queryMode == BASIC) {
			throw new RuntimeException("Current QueryParam was set as BASIC mode, can not be added a QueryParam!");
		}
		this.queryMode = ADVANCED;
		if (notParams == null) {
			notParams = new LinkedList<QueryParam>();
		}
		notParams.add(queryParam);
	}

	public String getName() {
		return name;
	}

	public String getOperator() {
		return operator;
	}

	public int getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public String getSql() {
		return sql;
	}

	private void setName(String name) {
		if (name == null) {
			throw new RuntimeException("参数name不能为NULL!");
		}
		this.name = name.trim();
	}

	private void setOperator(String operator) {
		if (operator == null) {
			throw new RuntimeException("参数operator不能为NULL!!");
		}
		this.operator = operator.trim();
		validateOperator();
	}

	private void setValue(Object value) {
		if (OPERATOR_LIKE.equals(this.operator) || OPERATOR_NLIKE.equals(this.operator))
			this.value = "%" + value + "%";
		else if (OPERATOR_LEFTLIKE.equals(this.operator) || OPERATOR_NLEFTLIKE.equals(this.operator))
			this.value = value + "%";
		else if (OPERATOR_RIGHTLIKE.equals(this.operator) || OPERATOR_NRIGHTLIKE.equals(this.operator))
			this.value = "%" + value;
		else if (OPERATOR_IS.equals(this.operator) || OPERATOR_NIS.equals(this.operator))
			this.value = null;
		else
			this.value = value;
		transferOperator();
	}

	/**
	 * 当值为null，把'='和'!='转换为'is'和'!is'，当值不为null，把'is'和'!is'转换为'='和'!='
	 * 把llike和rlike转换为like
	 */
	private void transferOperator() {
		if (value == null) {
			if (OPERATOR_EQ.equals(this.operator)) {
				this.operator = OPERATOR_IS;
			} else if (OPERATOR_NE.equals(this.operator)) {
				this.operator = OPERATOR_NIS;
			}
		} else {
			if (OPERATOR_IS.equals(this.operator)) {
				this.operator = OPERATOR_EQ;
			} else if (OPERATOR_NIS.equals(this.operator)) {
				this.operator = OPERATOR_NE;
			}
		}
		if (OPERATOR_LEFTLIKE.equals(this.operator)) {
			this.operator = OPERATOR_LIKE;
		} else if (OPERATOR_RIGHTLIKE.equals(this.operator)) {
			this.operator = OPERATOR_LIKE;
		}
	}

	/**
	 * 检查操作符与值是否匹配
	 */
	private void checkOperatorForNullValue() {
		if (this.value == null) {
			if (!(OPERATOR_IS.equals(this.operator) || OPERATOR_NIS.equals(this.operator))) {
				throw new RuntimeException("当值为null时，操作符只能是'is'或'='或'is not'或'!='!");
			}
		} else if (OPERATOR_IS.equals(this.operator) || OPERATOR_NIS.equals(this.operator)) {
			throw new RuntimeException("当操作符为'is'或'is not'时，值只能是null!");
		}
	}

	/**
	 * 检查操作符是否有效
	 */
	private void validateOperator() {
		if (this.operator.startsWith("!")) {
			if (this.operator.endsWith("%"))
				return;
			if (OPERATOR_NE.equals(this.operator))
				return;
			if (OPERATOR_NGE.equals(this.operator))
				return;
			if (OPERATOR_NGT.equals(this.operator))
				return;
			if (OPERATOR_NLE.equals(this.operator))
				return;
			if (OPERATOR_NLT.equals(this.operator))
				return;
			if (OPERATOR_NLIKE.equals(this.operator))
				return;
			if (OPERATOR_NLEFTLIKE.equals(this.operator))
				return;
			if (OPERATOR_NRIGHTLIKE.equals(this.operator))
				return;
			if (OPERATOR_NINCLUDE.equals(this.operator))
				return;
			if (OPERATOR_NILIKE.equals(this.operator))
				return;
			if (OPERATOR_NIINCLUDE.equals(this.operator))
				return;
			if (OPERATOR_NIS.equals(this.operator))
				return;
			if (OPERATOR_NIN.equals(this.operator))
				return;
		} else {
			if (OPERATOR_EQ.equals(this.operator))
				return;
			if (OPERATOR_GE.equals(this.operator))
				return;
			if (OPERATOR_GT.equals(this.operator))
				return;
			if (OPERATOR_LE.equals(this.operator))
				return;
			if (OPERATOR_LT.equals(this.operator))
				return;
			if (OPERATOR_LIKE.equals(this.operator))
				return;
			if (OPERATOR_LEFTLIKE.equals(this.operator))
				return;
			if (OPERATOR_RIGHTLIKE.equals(this.operator))
				return;
			if (OPERATOR_INCLUDE.equals(this.operator))
				return;
			if (OPERATOR_ILIKE.equals(this.operator))
				return;
			if (OPERATOR_IINCLUDE.equals(this.operator))
				return;
			if (OPERATOR_IS.equals(this.operator))
				return;
			if (OPERATOR_IN.equals(this.operator))
				return;
			if (FETCH.equals(this.operator))
				return;
			if (OPERATOR_BT.equals(this.operator))
				return;
		}
		throw new RuntimeException("The operator " + this.operator + " could be incorrect!");
	}

	public int getMode() {
		return queryMode;
	}

	public List<QueryParam> getAndParams() {
		return andParams;
	}

	public List<QueryParam> getNotParams() {
		return notParams;
	}

	public List<QueryParam> getOrParams() {
		return orParams;
	}

	public void removeAndParams(QueryParam params) {
		if (andParams != null) {
			for (int i = 0; i < andParams.size(); i++) {
				if (((QueryParam) andParams.get(i)).toString().equals(params.toString())) {
					andParams.remove(i);
					return;
				}
			}

		}
	}

	public void removeOrParams(QueryParam params) {
		if (orParams != null) {
			for (int i = 0; i < orParams.size(); i++) {
				if (((QueryParam) orParams.get(i)).toString().equals(params.toString())) {
					orParams.remove(i);
					return;
				}
			}

		}
	}

	public void removenotParams(QueryParam params) {
		if (notParams != null) {
			for (int i = 0; i < notParams.size(); i++) {
				if (((QueryParam) notParams.get(i)).toString().equals(params.toString())) {
					notParams.remove(i);
					return;
				}
			}

		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('(');
		if (queryMode == BASIC) {
			if (sql != null) {
				sb.append(sql);
			} else {
				sb.append(name).append(' ').append(operator).append(' ').append(value);
			}
		} else {
			if (andParams != null && andParams.size() > 0) {
				boolean firstFlag = true;
				for (QueryParam q : andParams) {
					if (firstFlag) {
						firstFlag = false;
					} else {
						sb.append(" and ");
					}
					sb.append(q.toString());
				}
			}
			if (orParams != null && orParams.size() > 0) {
				if (sb.length() > 1) {
					sb.append(" or ");
				}
				boolean firstFlag = true;
				for (QueryParam q : orParams) {
					if (firstFlag) {
						firstFlag = false;
					} else {
						sb.append(" or ");
					}
					sb.append(q.toString());
				}
			}
			if (notParams != null && notParams.size() > 0) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				boolean firstFlag = true;
				for (QueryParam q : notParams) {
					if (firstFlag) {
						firstFlag = false;
					} else {
						sb.append(" and ");
					}
					sb.append("not ");
					sb.append(q.toString());
				}
			}
		}
		sb.append(')');
		return sb.toString();
	}

}
