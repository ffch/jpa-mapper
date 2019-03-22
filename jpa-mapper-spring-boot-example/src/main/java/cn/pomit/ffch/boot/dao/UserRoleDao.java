package cn.pomit.ffch.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.session.RowBounds;

import cn.pomit.ffch.boot.domain.UserInfoUnion;
import cn.pomit.ffch.boot.domain.UserRole;
import cn.pomit.jpamapper.core.mapper.CrudMapper;

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
	
	@Select({
		"<script>",
	        "SELECT ",
	        "a.userName as userName,",
	        "a.mobile as mobile,",
	        "a.passwd as passwd,",
	        "a.valid as valid,",
	        "a.name as name",
	        "FROM user_info a ",
	        "where a.userName = #{userName}",
	   "</script>"})
	@Results({
		@Result(column="{userName=userName,phone=mobile}",property="userRole",
		one=@One(select="cn.pomit.ffch.boot.dao.UserRoleDao.getRole",
		fetchType=FetchType.EAGER))
	})
	UserInfoUnion selectJion(@Param("userName") String userName);
	
	@Select({
		"<script>",
	        "SELECT ",
	        "a.userName as userName,",
	        "a.role as role,",
	        "a.phone as phone",
	        "FROM user_role a ",
	        "where a.userName = #{userName} and a.phone = #{phone}",
	   "</script>"})
	UserRole getRole(String phone,String userName);
}