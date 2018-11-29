package com.cff.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

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
	
	@Select({
		"<script>",
	        "SELECT ",
	        "id as id,",
	        "userName as userName,",
	        "role as role",
	        "FROM user_role",
	   "</script>"})
	List<UserRole> selectAll();
	
	@Select({
		"<script>",
	        "SELECT ",
	        "id as id,",
	        "userName as userName,",
	        "role as role",
	        "FROM user_role",
	   "</script>"})
	List<UserRole> selectPage(RowBounds rowBounds);
}