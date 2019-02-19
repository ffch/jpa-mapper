package cn.pomit.ffch.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.pomit.ffch.boot.domain.UserInfoHis;
import cn.pomit.jpamapper.core.mapper.SimpleShardingMapper;

@Mapper
public interface UserInfoHisDao extends SimpleShardingMapper<UserInfoHis, String> {
	
}