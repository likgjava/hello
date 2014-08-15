package com.likg.common.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	
	
	/**每页显示记录数*/
	public static final String PAGE_SIZE = "pageSize";
	
	/** 指定要查询的列，格式如: usrCnName,email */
	public static final String QUERY_COLUMNS = "queryColumns";
	
	/** 指定隐藏查询的列，格式如: objId,status */
	public static final String HIDDEN_COLUMNS = "hiddenColumns";
	
	/** 总记录数 */
	public static final String TOTAL_RECORD = "totalRecord";
	
	/** 当前页码 */
	public static final String PAGE_INDEX = "pageIndex";
	
	/** 总页数 */
	public static final String PAGE_COUNT = "pageCount";
	
	/** 页面数据 */
	public static final String PAGE_DATA = "pageData";
	
	/** DATA_DT 数据 对应dataTables里的aaData属性 */
	public static final String DATA_DT="aaData";
	
	/** 分页数据 */
	private List<T> data = new ArrayList<T>();
	
	/** 每页记录数 */
	private Integer pageSize;
	
	/** 总记录数 */
	private Long totalRecord;
	
	/** 当前页码，从1开始 */
	private Integer pageIndex;
	
	/** 总页数 */
	private Integer pageCount;
	
	
	public Page(){
	}
	
	public Page(Integer pageSize, Integer pageIndex){
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
	}
	
	public Page(List<T> data, Long totalRecord, Integer pageSize, Integer pageIndex){
		this.data = data;
		this.totalRecord = totalRecord;
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
		this.pageCount = (int)(totalRecord%pageSize==0 ? totalRecord/pageSize : totalRecord/pageSize+1);
	}
	
	/**
	 * 获取当前页中第一条记录的序号
	 * @return
	 */
	public int getFirstResult() {
		
		return (this.pageIndex-1) * this.pageSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public Long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}

}
