package com.cff.boot.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cff.boot.dao.UserInfoDao;
import com.cff.boot.domain.UserInfo;

@RestController
public class WebRest {
	@Autowired
	UserInfoDao userInfoDao;
	
	@RequestMapping("/hello")
	public List<UserInfo> hello(){
		return userInfoDao.findByUserName("admin");
	}
	
	@RequestMapping("/all")
	public List<UserInfo> all(){
		return userInfoDao.findAll();
	}
}
