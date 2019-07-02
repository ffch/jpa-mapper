package cn.pomit.ffch.boot.web;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pomit.ffch.boot.dao.UserInfoHisDao;
import cn.pomit.ffch.boot.dao.UserRoleDao;
import cn.pomit.ffch.boot.domain.UserInfoHis;
import cn.pomit.ffch.boot.domain.UserInfoUnion;
import cn.pomit.ffch.boot.domain.UserRole;

@RestController
@RequestMapping("/my")
public class MybatisRest {
	@Autowired
	UserRoleDao userRoleDao;
	
	@RequestMapping("/selectAll")
	public List<UserRole> findOne(){
		return userRoleDao.selectAll();
	}
	
	@RequestMapping("/selectAllTest")
	public List<UserRole> findAll(){
		RowBounds rowBounds = new RowBounds(0,2);
		return userRoleDao.selectPage(rowBounds);
	}
	
	@RequestMapping("/selectJoin")
	public UserInfoUnion selectJion(){
		return userRoleDao.selectJion("cff");
	}
}
