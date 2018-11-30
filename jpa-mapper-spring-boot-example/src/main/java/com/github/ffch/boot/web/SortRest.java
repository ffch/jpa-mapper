package com.github.ffch.boot.web;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ffch.boot.dao.UserInfoHisDao;
import com.github.ffch.boot.dao.UserInfoSortDao;
import com.github.ffch.boot.dao.UserRoleDao;
import com.github.ffch.boot.domain.Page;
import com.github.ffch.boot.domain.UserInfo;
import com.github.ffch.boot.domain.UserInfoHis;
import com.github.ffch.boot.domain.UserRole;
import com.github.ffch.jpamapper.core.domain.page.Pageable;
import com.github.ffch.jpamapper.core.domain.page.Sort;
import com.github.ffch.jpamapper.core.domain.page.Sort.Direction;
import com.github.ffch.jpamapper.core.domain.page.Sort.Order;

@RestController
@RequestMapping("/sort")
public class SortRest {
	@Autowired
	UserInfoSortDao userInfoSortDao;
	
	@RequestMapping("/selectAll")
	public Collection<UserInfo> selectAll(){
		return userInfoSortDao.findAll();
	}
	
	@RequestMapping("/findAllSorted")
	public Collection<UserInfo> findAllSorted(){
		Order order = new Order(Direction.ASC, "mobile");
		Order order1 = new Order(Direction.ASC, "userName");
		Sort sort = new Sort(order, order1);
		return userInfoSortDao.findAllSorted(sort );
	}
	
//	@RequestMapping("/findPageTest")
//	public Page findPageTest(){
//		
//		return userInfoSortDao.selectPage(0,5);
//	}
	
	@RequestMapping("/selectCount")
	public Page selectCount(){
		
		Page<UserInfo> page =  userInfoSortDao.selectCount(0,5);
		return page;
	}
	
	@RequestMapping("/selectInfo")
	public List selectInfo(){
		
		List<UserInfo> page =  userInfoSortDao.selectInfo("123",1,5);
		return page;
	}
	
	@RequestMapping("/selectCondition")
	public Page selectCondition(){
		Pageable pageable = new Pageable();
		pageable.setPage(1);
		pageable.setSize(5);
		Page<UserInfo> page =  userInfoSortDao.selectCondition("123",pageable);
		return page;
	}
	
	@RequestMapping("/findAllPageable")
	public com.github.ffch.jpamapper.core.domain.page.Page findAllPageable(){
		Pageable pageable = new Pageable();
		pageable.setPage(1);
		pageable.setSize(5);
		com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.findAllPageable(pageable);
		return page;
	}
	
	@RequestMapping("/findAllPageableSort")
	public com.github.ffch.jpamapper.core.domain.page.Page findAllPageableSort(){
		Pageable pageable = new Pageable();
		pageable.setPage(1);
		pageable.setSize(5);
		Order order = new Order(Direction.ASC, "mobile");
		Order order1 = new Order(Direction.ASC, "userName");
		Sort sort = new Sort(order, order1);
		pageable.setSort(sort);
		com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.findAllPageable(pageable);
		return page;
	}
	
	@RequestMapping("/findPageable")
	public com.github.ffch.jpamapper.core.domain.page.Page findPageable(){
		Pageable pageable = new Pageable();
		pageable.setPage(1);
		pageable.setSize(5);
		com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.pageByPasswd("123", pageable);
		return page;
	}
	
	@RequestMapping("/findPageableSort")
	public com.github.ffch.jpamapper.core.domain.page.Page findPageableSort(){
		Pageable pageable = new Pageable();
		pageable.setPage(1);
		pageable.setSize(5);
		Order order = new Order(Direction.ASC, "mobile");
		Order order1 = new Order(Direction.ASC, "userName");
		Sort sort = new Sort(order, order1);
		pageable.setSort(sort);
		com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.pageByPasswd("123", pageable);
		return page;
	}
}
