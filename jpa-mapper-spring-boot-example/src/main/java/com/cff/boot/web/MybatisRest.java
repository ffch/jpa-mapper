package com.cff.boot.web;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cff.boot.dao.UserInfoHisDao;
import com.cff.boot.dao.UserRoleDao;
import com.cff.boot.domain.UserInfoHis;
import com.cff.boot.domain.UserRole;

@RestController
@RequestMapping("/my")
public class MybatisRest {
	@Autowired
	UserRoleDao userRoleDao;
	
	@RequestMapping("/selectAll")
	public List<UserRole> findOne(@PathVariable String mobile){
		return userRoleDao.selectAll();
	}
	
	@RequestMapping("/selectAllTest")
	public List<UserRole> findAll(){
		RowBounds rowBounds = new RowBounds(0,2);
		return userRoleDao.selectPage(rowBounds);
	}
	
	
}
