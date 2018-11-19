package com.cff.boot.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.cff.boot.domain.UserRole;
import com.cff.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserRoleDao extends CrudMapper<UserRole, Integer> {
	@Insert({"<script> ",
		"INSERT INTO user_role",
		"( id,",
		"userName ,",
		"role",
		 ") ",
		" values ",
		 "( #{param1.id},",
		 "#{param1.userName},",
		 "#{param1.role}",
		" ) ",
		 "</script>"})
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int saveTest(UserRole userRole, int test);
}