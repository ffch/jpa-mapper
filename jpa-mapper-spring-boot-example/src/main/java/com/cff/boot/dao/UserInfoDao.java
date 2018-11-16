package com.cff.boot.dao;


import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cff.boot.domain.UserInfo;
import com.cff.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserInfoDao extends CrudMapper<UserInfo, String> {
	
//	@Select({
//		"select * from user_info where userName = #{answer.isRecommend, jdbcType=VARCHAR} and valid = #{answer.valid, jdbcType=INTEGER}"
//	})
	List<UserInfo> findByUserName(String userName);
	
	int saveAllWithId(@Param("list") Collection<UserInfo> entities);
	
	@Insert({"<script> ",
	"INSERT INTO user_info",
	"( ",
	"<if test='_parameter.userName != null'> userName , </if>  ",
	"<if test='_parameter.mobile != null'> mobile , </if>  ",
	"<if test='_parameter.name != null'> name , </if>  ",
	"<if test='_parameter.passwd != null'> passwd , </if> ",
	 "<if test='_parameter.valid != null'> valid </if> ",
	 ") ",
	" values ",
	 "( ",
	 "<if test='_parameter.userName != null'>  #{_parameter.userName}, </if>  ",
	 "<if test='_parameter.mobile != null'>  #{_parameter.mobile}, </if>  ",
	 "<if test='_parameter.name != null'>  #{_parameter.name}, </if>  ",
	 "<if test='_parameter.passwd != null'>  #{_parameter.passwd}, </if>  ",
	" <if test='_parameter.valid != null'>  #{_parameter.valid} </if> ",
	" ) ",
	 "</script>"})
	int saveTest(UserInfo entity);
}