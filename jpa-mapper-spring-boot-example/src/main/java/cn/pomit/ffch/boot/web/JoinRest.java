package cn.pomit.ffch.boot.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pomit.ffch.boot.dao.UserInfoUnionDao;
import cn.pomit.ffch.boot.domain.UserInfoUnion;

@RestController
@RequestMapping("/join")
public class JoinRest {
	@Autowired
	UserInfoUnionDao userInfoDao;
	
	@RequestMapping("/findAll")
	public List<UserInfoUnion> findAll(){
		return (List<UserInfoUnion>) userInfoDao.findAll();
	}
	
	@RequestMapping("/findBatch")
	public List<UserInfoUnion> findBatch(){
		List<String> userNames = new ArrayList<>();
		userNames.add("admin");
		userNames.add("cff");
		return (List<UserInfoUnion>) userInfoDao.findBatch(userNames);
	}
}
