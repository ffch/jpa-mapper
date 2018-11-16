package com.cff.boot.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.cff.boot.domain.UserInfo;
import com.cff.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserInfoDao extends CrudMapper<UserInfo, String> {
	
//	@Select({
//		"select * from user_info where userName = #{answer.isRecommend, jdbcType=VARCHAR} and valid = #{answer.valid, jdbcType=INTEGER}"
//	})
	List<UserInfo> findByUserName(String userName);
}