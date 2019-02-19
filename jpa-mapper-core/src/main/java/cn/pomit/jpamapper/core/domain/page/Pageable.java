package cn.pomit.jpamapper.core.domain.page;

public class Pageable {
	private int page;
	private int size;
	private String sort = "";
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


	public int getPage() {
		return page;
	}


	public int getSize() {
		return size;
	}
	
	public void setSort(Sort sort){
		this.sort = sort.toSort();
	}

	public String getSort() {
		return sort;
	}
}
