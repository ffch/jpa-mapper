package com.cff.jpamapper.core.domain.page;

public class Pageable {
	private int page;
	private int size;
	
	public int getPageSize() {
		return size;
	}

	
	public int getPageNumber() {
		return page;
	}
	
	public long getOffset() {
		return (long) page * (long) size;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public void setSize(int size) {
		this.size = size;
	}
}
