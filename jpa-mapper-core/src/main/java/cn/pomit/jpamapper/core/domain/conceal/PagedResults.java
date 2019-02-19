package cn.pomit.jpamapper.core.domain.conceal;

import java.util.ArrayList;
import java.util.List;

public class PagedResults {
	String id = "";
	List<PagedResult> value = new ArrayList<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<PagedResult> getValue() {
		return value;
	}
	public void setValue(List<PagedResult> value) {
		this.value = value;
	}
	
	public void addValue(PagedResult pagedResult){
		this.value.add(pagedResult);
	}
}
