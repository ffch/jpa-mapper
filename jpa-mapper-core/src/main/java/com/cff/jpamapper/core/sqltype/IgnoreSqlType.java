package com.cff.jpamapper.core.sqltype;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

public class IgnoreSqlType implements SqlType {

	public static final IgnoreSqlType INSTANCE = new IgnoreSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UNKNOWN;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		return null;
	}
}
