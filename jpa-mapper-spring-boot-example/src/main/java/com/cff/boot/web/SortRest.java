package com.cff.boot.web;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cff.boot.dao.UserInfoHisDao;
import com.cff.boot.dao.UserInfoSortDao;
import com.cff.boot.dao.UserRoleDao;
import com.cff.boot.domain.Page;
import com.cff.boot.domain.UserInfo;
import com.cff.boot.domain.UserInfoHis;
import com.cff.boot.domain.UserRole;
import com.cff.jpamapper.core.domain.Sort;
import com.cff.jpamapper.core.domain.Sort.Direction;
import com.cff.jpamapper.core.domain.Sort.Order;

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
}
