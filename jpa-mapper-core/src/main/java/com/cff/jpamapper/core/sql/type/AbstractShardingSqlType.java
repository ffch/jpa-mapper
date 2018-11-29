package com.cff.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public abstract class AbstractShardingSqlType implements SqlType {
	
	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UNKNOWN;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return makeSql(jpaModelEntity, method);
	}
}
