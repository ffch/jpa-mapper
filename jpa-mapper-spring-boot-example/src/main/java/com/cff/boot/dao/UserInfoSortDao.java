package com.cff.boot.dao;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.cff.boot.domain.Page;
import com.cff.boot.domain.UserInfo;
import com.cff.boot.domain.UserRole;
import com.cff.jpamapper.core.mapper.CrudMapper;
import com.cff.jpamapper.core.mapper.PagingAndSortingMapper;

@Mapper
public interface UserInfoSortDao extends PagingAndSortingMapper<UserInfo, String> {
	@Select({
		"<script>",
	        "SELECT ",
	        "userName,passwd,name,mobile,valid",
	        "FROM user_info",
	        "limit #{size} OFFSET #{page}",
	   "</script>"})
	List<UserInfo> selectPage(int page, int size);
	
	@Select({
		"<script>",
	        "SELECT count(1) count, ${page} page, ${size} size",
	        "FROM user_info",
	   "</script>"})
	@Results(value = {
            @Result(column = "count", property = "count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "{page = page,size = size}", property = "list", many = @Many(select="selectPage"))
    })
	Page<UserInfo> selectCount(@Param("page")int page, @Param("size")int size);
}