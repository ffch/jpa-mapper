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
import com.cff.jpamapper.core.domain.page.Pageable;
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
	
	@Select({
	"<script> ",
	"<bind name=\"startNum\" value=\"(page-1)*size\"></bind>",
	"<trim prefix=\" select \" suffix=\" \" suffixOverrides=\",\">",
	"userName userName,valid valid,passwd passwd,mobile mobile,name name,",
	"</trim> from user_info ",
	"<trim prefix=\" where \" prefixOverrides=\"AND\">",
	"AND passwd = #{passwd} ",
	"</trim>",
	"limit #{size} OFFSET #{startNum}  ",
	"</script>"})
	List<UserInfo> selectInfo(String passwd, int page, int size);
	
	@Select({
		"<script>",
	        "SELECT count(1) count, ${pageable.page} page, ${pageable.size} size, ${passwd} passwd",
	        "FROM user_info where passwd = #{passwd}",
	   "</script>"})
	@Results(value = {
            @Result(column = "count", property = "count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(column = "{passwd = passwd,page = page,size = size}", property = "list", many = @Many(select="selectInfo"))
    })
	Page<UserInfo> selectCondition(@Param("passwd")String passwd, @Param("pageable") Pageable pageable);
	
	com.cff.jpamapper.core.domain.page.Page<UserInfo> pageByPasswd(String passwd, Pageable pageable);
}