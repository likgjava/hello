package com.likg.core.dao.hibernate.query;

/**
 * 查询投影
 * 
 * @author likg
 */
public class QueryProjections implements Cloneable {

	public static QueryProjections countProjection;
	/**标记是否查询总记录数，为true时表示查询总记录数*/
	private boolean rowCount;
	private String[] max;
	private String[] min;
	private String[] avg;
	private String[] sum;
	private String[] count;
	private boolean distinctFlag;
	private String[] countDistinct;
	private String[] groupProperty; // TODO having
	private String[] distinct;
	private String[] property;
	
	/**排序字段*/
	private String[] orderProperty;
	/**排序方式*/
	private boolean[] descFlag;

	static {
		if (countProjection == null) {
			countProjection = new QueryProjections();
			countProjection.setRowCount(true);
		}
	}

	public String[] getAvg() {
		return avg;
	}

	public String[] getCount() {
		return count;
	}

	public String[] getCountDistinct() {
		return countDistinct;
	}

	public boolean[] getDescFlag() {
		return descFlag;
	}

	public String[] getGroupProperty() {
		return groupProperty;
	}

	public String[] getMax() {
		return max;
	}

	public String[] getMin() {
		return min;
	}

	public String[] getOrderProperty() {
		return orderProperty;
	}

	public boolean isRowCount() {
		return rowCount;
	}

	public String[] getSum() {
		return sum;
	}

	public void setAvg(String[] avg) {
		this.avg = avg;
	}

	public void setAvg(String avg) {
		this.avg = toArray(avg);
	}

	public void setCount(String[] count) {
		this.count = count;
	}

	public void setCount(String count) {
		this.count = toArray(count);
	}

	public void setCountDistinct(String[] countDistinct) {
		this.countDistinct = countDistinct;
	}

	public void setCountDistinct(String countDistinct) {
		this.countDistinct = toArray(countDistinct);
	}

	public void setDescFlag(boolean[] descFlag) {
		this.descFlag = descFlag;
	}

	public void setDescFlag(boolean descFlag) {
		this.descFlag = toArray(descFlag);
	}

	public void setGroupProperty(String[] groupProperty) {
		this.groupProperty = groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = toArray(groupProperty);
	}

	public void setMax(String[] max) {
		this.max = max;
	}

	public void setMax(String max) {
		this.max = toArray(max);
	}

	public void setMin(String[] min) {
		this.min = min;
	}

	public void setMin(String min) {
		this.min = toArray(min);
	}

	public void setOrderProperty(String[] orderProperty) {
		this.orderProperty = orderProperty;
	}

	public void setOrderProperty(String orderProperty) {
		this.orderProperty = toArray(orderProperty);
	}

	public void setRowCount(boolean rowCount) {
		this.rowCount = rowCount;
	}

	public void setSum(String[] sum) {
		this.sum = sum;
	}

	public void setSum(String sum) {
		this.sum = toArray(sum);
	}

	public boolean isDistinctFlag() {
		return distinctFlag;
	}

	public void setDistinctFlag(boolean distinctFlag) {
		this.distinctFlag = distinctFlag;
	}

	public String[] getDistinct() {
		return distinct;
	}

	public void setDistinct(String[] distinct) {
		this.distinct = distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = toArray(distinct);
	}

	public String[] getProperty() {
		return property;
	}

	public void setProperty(String[] property) {
		this.property = property;
	}

	public void setProperty(String property) {
		this.property = toArray(property);
	}

	private String[] toArray(String str) {
		String[] array = new String[1];
		array[0] = str;
		return array;
	}

	private boolean[] toArray(boolean bool) {
		boolean[] array = new boolean[1];
		array[0] = bool;
		return array;
	}

	public QueryProjections clone() {
		QueryProjections cloneObject = null;
		try {
			cloneObject = (QueryProjections) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cloneObject;
	}

}