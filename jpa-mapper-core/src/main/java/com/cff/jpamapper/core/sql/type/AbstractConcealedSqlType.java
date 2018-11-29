package com.cff.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public abstract class AbstractConcealedSqlType implements SqlType {
	
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
