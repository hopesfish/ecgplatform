package com.ainia.ecgApi.core.crud;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * <p>CRUD Page Object</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * Page.java
 * @author pq
 * @createdDate 2013-6-21
 * @version 0.3
 */
public class Page<T> implements Serializable {

	public static final int DEFAULT_PAGE_SIZE = 15;
	
	private Long total;
	private int max = DEFAULT_PAGE_SIZE;
	private int curPage = 1;
	private int pageCount = 0;
	private boolean pageable = true;
	/**
	 * data Object
	 */
	private Iterable<T> datas;
	
	public void refresh() {
		Assert.notNull(total , "total not be null");
		Assert.notNull(max , "page max not be null");
		pageCount = (int) (total/max);
		if (total % max != 0) {
			pageCount++;
		}
		curPage = Math.min(curPage , pageCount);
		curPage = Math.max(curPage , 1);
		
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
		refresh();
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getOffset() {
		return (curPage - 1) * max;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public Iterable<T> getDatas() {
		return datas;
	}

	public void setDatas(Iterable<T> datas) {
		this.datas = datas;
	}

	public boolean isPageable() {
		return pageable;
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}
	
}
