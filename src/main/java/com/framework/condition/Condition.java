package com.framework.condition;

import java.util.Map;

import com.framework.utils.PaginationBase;

/**
 * 
 * @author tang
 * @version 2016-04-18
 * 说明：封装所有的 查询条件
 *
 */
@SuppressWarnings("unchecked")
public class Condition {
	private Map<String,Object> condMap;
	private PaginationBase page;

	public Map<String, Object> getCondMap() {
		return condMap;
	}

	public void setCondMap(Map<String, Object> condMap) {
		this.condMap = condMap;
	}

	public PaginationBase getPage() {
		return page;
	}

	public void setPage(PaginationBase page) {
		this.page = page;
	}
	
	
}
