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
	
	@RequestMapping("/findByUserName")
	public List<UserInfo> findByUserName(){
		return userInfoDao.findByUserName("admin");
	}
	
	@RequestMapping("/findAll")
	public List<UserInfo> findAll(){
		return (List<UserInfo>) userInfoDao.findAll();
	}
	
	@RequestMapping("/findBatch")
	public List<UserInfo> findBatch(){
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
	
	@RequestMapping("/saveall")
	public void saveall(){
		List<UserInfo> list = new ArrayList<>();
		for(int i=0;i<5;i++){
			UserInfo userInfo = new UserInfo();
			userInfo.setUserName("heihei" + i);
			userInfo.setPasswd("342" + i);
			userInfo.setMobile("342" + i);
			list.add(userInfo);
		}
		
		System.out.println(userInfoDao.saveAllWithId(list));
	}
	
	@RequestMapping("/updateAll")
	public void updateAll(){
		List<String> list = new ArrayList<>();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei" + 9);
		userInfo.setPasswd("344");
		userInfo.setMobile("345");
		for(int i=0;i<5;i++){
			list.add("heihei" + i);
		}
		
		System.out.println(userInfoDao.updateAll(userInfo, list));
	}
	
	@RequestMapping("/deleteBatch")
	public void deleteBatch(){
		List<String> list = new ArrayList<>();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei" + 9);
		userInfo.setPasswd("344");
		userInfo.setMobile("345");
		for(int i=0;i<5;i++){
			list.add("heihei" + i);
		}
		
		System.out.println(userInfoDao.deleteBatch(list));
	}
	
	@RequestMapping("/saveTest")
	public void saveTest(){
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei");
		userInfo.setPasswd("342");
		userInfo.setMobile("342");
		System.out.println(userInfoDao.saveTest(userInfo));
	}
	
	@RequestMapping("/update")
	public void update(){
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("heihei");
		userInfo.setPasswd("345");
		userInfo.setMobile("343");
		System.out.println(userInfoDao.update(userInfo));
	}
	
	@RequestMapping("/deleteObj")
	public void deleteObj(){
		UserInfo userInfo = new UserInfo();
		userInfo.setPasswd("345");
		userInfo.setMobile("343");
		userInfoDao.deleteEntity(userInfo);
	}
	
	@RequestMapping("/delete")
	public void delete(){
		userInfoDao.delete("heihei");
	}
	
	@RequestMapping("/testall")
	public void testall(){
		System.out.println(userInfoDao.count());
		System.out.println(userInfoDao.exists("admin"));
		System.out.println(userInfoDao.findByUserName("admin"));
		System.out.println(userInfoDao.findOne("admin"));
	}
}
