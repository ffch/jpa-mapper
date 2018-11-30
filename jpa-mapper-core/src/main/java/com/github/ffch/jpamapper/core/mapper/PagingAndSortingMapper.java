package com.github.ffch.jpamapper.core.mapper;

import java.io.Serializable;
import java.util.Collection;

import com.github.ffch.jpamapper.core.domain.page.Page;
import com.github.ffch.jpamapper.core.domain.page.Pageable;
import com.github.ffch.jpamapper.core.domain.page.Sort;

public interface PagingAndSortingMapper<T, ID extends Serializable> extends CrudMapper<T, ID> {
	Collection<T> findAllSorted(Sort sort);
	
	Page<T> findAllPageable(Pageable pageable);
	
}
