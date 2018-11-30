package com.github.ffch.boot.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ffch.boot.dao.UserInfoHisDao;
import com.github.ffch.boot.domain.UserInfoHis;

@RestController
@RequestMapping("/shard")
public class ShardingRest {
	@Autowired
	UserInfoHisDao userInfoHisDao;
	
	@RequestMapping("/findOne/{mobile}")
	public List<UserInfoHis> findOne(@PathVariable String mobile){
		UserInfoHis userInfoHis = new UserInfoHis();
		userInfoHis.setMobile(mobile);
		return (List<UserInfoHis>) userInfoHisDao.find(userInfoHis);
	}
	
	@RequestMapping("/findAll")
	public List<UserInfoHis> findAll(){
		return (List<UserInfoHis>) userInfoHisDao.findAll("100", "400", false);
	}
	
	@RequestMapping("/findDisAll")
	public List<UserInfoHis> findDisAll(){
		return (List<UserInfoHis>) userInfoHisDao.findAll("100", "400", true);
	}
	
	@RequestMapping("/findRange")
	public List<UserInfoHis> findRange(){
		UserInfoHis userInfoHis = new UserInfoHis();
		userInfoHis.setValid("1");
		return (List<UserInfoHis>) userInfoHisDao.findRange(userInfoHis, "100", "400", false);
	}
	
	@RequestMapping("/save")
	public int save(@RequestBody UserInfoHis userInfoHis){
		return userInfoHisDao.save(userInfoHis);
	}
	
	@RequestMapping("/update")
	public int update(@RequestBody UserInfoHis userInfoHis){
		return userInfoHisDao.update(userInfoHis);
	}
	
	@RequestMapping("/delete")
	public int delete(@RequestBody UserInfoHis userInfoHis){
		return userInfoHisDao.delete(userInfoHis);
	}
}
