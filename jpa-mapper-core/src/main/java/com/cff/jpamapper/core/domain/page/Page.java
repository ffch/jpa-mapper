package com.cff.jpamapper.core.domain.page;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	List<T> content = new ArrayList<>();;
	int count = 0;
	int page = 1;
	int size = 1;
	
	public Page(){
	}
	
	
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getTotalPages() {
		return getSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) getSize());
	}
	
	public long getTotalElements() {
		return count;
	}
}
