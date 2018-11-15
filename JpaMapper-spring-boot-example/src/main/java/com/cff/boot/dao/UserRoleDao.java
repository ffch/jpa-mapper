package com.cff.boot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cff.boot.domain.UserRole;
import com.cff.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserRoleDao extends CrudMapper<UserRole, Integer> {
	
}