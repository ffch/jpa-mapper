package com.cff.jpamapper.core.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.cff.jpamapper.core.domain.Page;
import com.cff.jpamapper.core.domain.Pageable;
import com.cff.jpamapper.core.domain.Sort;

public interface PagingAndSortingMapper<T, ID extends Serializable> extends CrudMapper<T, ID> {
	Collection<T> findAllSorted(Sort sort);
	
	Page<T> findAllPageable(Pageable pageable);
	
	List<T> listAllPageable(Pageable pageable);
}
