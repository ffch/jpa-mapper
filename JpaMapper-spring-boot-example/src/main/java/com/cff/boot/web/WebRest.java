package com.cff.boot.web;

import java.util.ArrayList;
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
		return (List<UserInfo>) userInfoDao.findAll();
	}
	
	@RequestMapping("/batch")
	public List<UserInfo> batch(){
		List<String> userNames = new ArrayList<>();
		userNames.add("admin");
		userNames.add("cff");
		return (List<UserInfo>) userInfoDao.findBatch(userNames);
	}
	
	@RequestMapping("/save")
	public void save(){
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei");
		userInfo.setPasswd("342");
		userInfo.setMobile("342");
		System.out.println(userInfoDao.save(userInfo));
	}
	
	@RequestMapping("/update")
	public void update(){
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei");
		userInfo.setPasswd("342");
		userInfo.setMobile("342");
		System.out.println(userInfoDao.update(userInfo));
	}
}
