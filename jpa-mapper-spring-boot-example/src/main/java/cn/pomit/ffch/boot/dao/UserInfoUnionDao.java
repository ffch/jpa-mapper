package cn.pomit.ffch.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.pomit.ffch.boot.domain.UserInfoUnion;
import cn.pomit.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserInfoUnionDao extends CrudMapper<UserInfoUnion, String> {
	
}