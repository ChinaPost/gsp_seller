package com.framework.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * @author tang
 * @version 2016-04-18 分页参数、数据控制逻辑
 */
public class PaginationBase {
	/***************************************************************************
	 * 页数、页大小、总行数
	 */
	public static final Integer MAX_PAGE_SIZE = 500;// 每页最大记录数限制
	private int pageNo = 0;//当前页码
	private int	pageSize = 10;//页码大小
	private long totalRows = 0;//总记录数
	private int totalPages = 0;//总页码
	private boolean hasNextPage = false;//是否有下一页
	private boolean hasPreviousPage = false;//是否有上一页
	private long pageStartRow = 0;//当前页开始记录数
	private long pageEndRow = 0;
	
	private List rslist;//查询结果

	//构造函数
	public PaginationBase(){
	}
	
	public PaginationBase(int pageNo , int pageSize){
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
	}
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo < 1) {
			pageNo = 1;
		}

		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		
		this.pageSize = pageSize;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getPageStartRow() {
		pageStartRow = ((pageNo - 1L) * this.pageSize);
		if (0 > pageStartRow) {
			pageStartRow = 0;
		}
		
		return pageStartRow;
	}

	public void setPageStartRow(long pageStartRow) {
		this.pageStartRow = pageStartRow;
	}

	public long getPageEndRow() {
		return pageEndRow;
	}

	public void setPageEndRow(long pageEndRow) {
		this.pageEndRow = pageEndRow;
	}

	public boolean isHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public void calculate(long totalRows) {
		setTotalRows(totalRows);
		
		if (1 > pageNo) {
			pageNo = 1;
		}

		totalPages = (int) (totalRows / pageSize);
		if (totalRows % pageSize == 0L) {
		} else {
			totalPages++;
		}

		if (pageNo > totalPages) {
			pageNo = totalPages;
		}

		if (this.pageNo >= this.totalPages) {
			this.hasNextPage = false;
		} else {
			this.hasNextPage = true;
		}

		if ((this.pageNo <= 1L) || (this.totalPages == 0L)) {
			this.hasPreviousPage = false;
		} else {
			this.hasPreviousPage = true;
		}

		pageStartRow = ((pageNo - 1L) * this.pageSize);
		if (0 > pageStartRow) {
			pageStartRow = 0;
		}

		pageEndRow = (pageNo * pageSize);
		if (pageEndRow > totalRows)
			pageEndRow = totalRows;
		
		
		if(pageNo == totalPages)pageSize = (int)(totalRows - pageSize * (pageNo - 1));
		if(totalRows <= 0) pageSize = 0;
	}

	public List getRslist() {
		return rslist;
	}

	public void setRslist(List rslist) {
		this.rslist = rslist;
	}
	
	
	/**
	 * 若分页信息合法，则返回带有totalRows,totalPages,pageSize的rltmap
	 * 否则返回普通的rltmap
	 */
	public Map<String, Object> genRltmap() {
        Map<String, Object> rltmap = new HashMap<>();
        if(this.getPageNo()>0){//分页信息合法
        	rltmap.put("totalRows", this.getTotalRows());
        	rltmap.put("totalPages", this.getTotalPages());
        	rltmap.put("pageSize", this.getPageSize());
        }
        return rltmap;
	}
}
