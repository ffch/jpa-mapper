package com.github.ffch.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;

public class IgnoreSqlType implements SqlType {

	public static final IgnoreSqlType INSTANCE = new IgnoreSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UNKNOWN;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}
}
