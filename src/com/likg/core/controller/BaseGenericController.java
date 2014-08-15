package com.likg.core.controller;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.likg.common.Constants;
import com.likg.common.page.Page;
import com.likg.core.context.FrameMessageSource;
import com.likg.core.dao.hibernate.query.QueryObject;
import com.likg.core.dao.hibernate.query.QueryWebUtils;
import com.likg.core.service.BaseGenericService;
import com.likg.core.util.HqlResultConvertUtils;

public abstract class BaseGenericController<T> {
	
	@Resource(name="baseGenericServiceImpl")
	BaseGenericService<T> baseGenericService;
	
	@Resource(name="frameMessageSource")
	protected FrameMessageSource messageSource;
	
	/**
	 * 根据泛型获取持久化对象的类型
	 * @return 持久化对象的类型
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getPersistClass() {
		if(super.getClass().getGenericSuperclass() instanceof ParameterizedType) {
			return (Class) ((ParameterizedType)super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return null;
	}

	/**
	 * 根据id删除对象
	 * @param objId 对象id
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=remove")
	public ModelAndView remove(String[] objId, HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		baseGenericService.remove(objId, this.getPersistClass());
		model.put(Constants.SUCCESS, true);
		
		return new ModelAndView(Constants.JSON_VIEW, model);
	}
	
	/**
	 * 获取列表数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=list")
	public ModelAndView list(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		//接收前台的指定查询字段
		String queryColumns = request.getParameter(Page.QUERY_COLUMNS); //显示的列
		String hiddenColumns = request.getParameter(Page.HIDDEN_COLUMNS); //隐藏的列
		Assert.notNull(queryColumns, "dataTables未设置要查询的字段!");
		if(StringUtils.isNotBlank(hiddenColumns)) {
			queryColumns = (queryColumns+","+hiddenColumns).trim();
		}
		
		//根据查询信息创建QueryObject对象
		QueryObject<T> query = QueryWebUtils.getQuery(request, this.getPersistClass());
		query.getQueryProjections().setProperty(queryColumns.split(","));
    	
		//获取分页信息
		String pageIndex = request.getParameter(Page.PAGE_INDEX); //当前页码
		String pageSize = request.getParameter(Page.PAGE_SIZE); //每页显示记录数
		Assert.notNull(pageIndex, "分页需要传递参数pageIndex:表示当前页!");
		Assert.notNull(pageSize, "分页需要传递参数pageSize:表示每页有多少行!");
		Page<T> page = new Page<T>(Integer.parseInt(pageSize), Integer.parseInt(pageIndex));
		
		//获取列表数据
		Page<T> pageData = baseGenericService.findByQuery(query, true, page.getFirstResult(), page.getPageSize());
		
		
		String alias=request.getParameter("alias");
		pageData.setData(HqlResultConvertUtils.hqlResultConvert(pageData.getData(), queryColumns.split(","), alias==null?null:alias.split(","), this.getPersistClass(), new HashMap<String, String>()));


		//设置分页列表数据
		model.put(Page.TOTAL_RECORD, pageData.getTotalRecord()); //总记录数
		model.put(Page.PAGE_INDEX, pageData.getPageIndex()); //当前页
		model.put(Page.PAGE_COUNT, pageData.getPageCount()); //总页数
		model.put(Page.PAGE_DATA, pageData.getData()); //数据
		
		return new ModelAndView(Constants.JSON_VIEW, model);
    }
	
}
