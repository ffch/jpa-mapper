package com.cff.boot.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cff.boot.dao.UserInfoDao;
import com.cff.boot.dao.UserRoleDao;
import com.cff.boot.domain.UserInfo;
import com.cff.boot.domain.UserRole;

@RestController
public class WebRest {
	@Autowired
	UserInfoDao userInfoDao;
	@Autowired
	UserRoleDao userRoleDao;
	
	@RequestMapping("/findByUserName")
	public List<UserInfo> findByUserName(){
		return userInfoDao.findByUserName("admin");
	}
	
	@RequestMapping("/findBy")
	public List<UserInfo> findBy(){
		return userInfoDao.findByNameAndMobile("cff", "12");
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
		userInfo.setName("fcc");
		userInfo.setMobile("342");
		System.out.println(userInfoDao.save(userInfo));
	}
	
	@RequestMapping("/saveRole")
	public void saveRole(){
		UserRole userInfo = new UserRole();
		userInfo.setUserName("heihei");
		userInfo.setRole("USER");
		System.out.println(userRoleDao.save(userInfo));
		System.out.println(userInfo);
	}
	
	@RequestMapping("/saveRoleTest")
	public void saveRoleTest(){
		UserRole userInfo = new UserRole();
		userInfo.setUserName("admin");
		userInfo.setRole("USER");
		System.out.println(userRoleDao.saveTest(userInfo,1));
		System.out.println(userInfo);
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
	
	@RequestMapping("/deleteBy")
	public void deleteBy(){
		userInfoDao.deleteByNameAndMobile("fcc","342");
	}
	
	@RequestMapping("/testall")
	public void testall(){
		System.out.println(userInfoDao.count());
		System.out.println(userInfoDao.exists("admin"));
		System.out.println(userInfoDao.findByUserName("admin"));
		System.out.println(userInfoDao.findOne("admin"));
	}
}
