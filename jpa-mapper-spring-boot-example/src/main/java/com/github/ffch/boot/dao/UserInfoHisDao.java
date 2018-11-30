package com.github.ffch.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.ffch.boot.domain.UserInfoHis;
import com.github.ffch.jpamapper.core.mapper.SimpleShardingMapper;

@Mapper
public interface UserInfoHisDao extends SimpleShardingMapper<UserInfoHis, String> {
	
}