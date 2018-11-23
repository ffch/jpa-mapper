package com.cff.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import com.cff.boot.domain.UserInfoHis;
import com.cff.jpamapper.core.mapper.SimpleShardingMapper;

@Mapper
public interface UserInfoHisDao extends SimpleShardingMapper<UserInfoHis, String> {
	
}