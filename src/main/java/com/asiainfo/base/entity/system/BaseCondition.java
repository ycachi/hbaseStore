package com.asiainfo.base.entity.system;

public class BaseCondition {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前页数
	 */
	private int page = 1;

	/**
	 * 每页显示记录数
	 */
	private int pageSize = 10;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
