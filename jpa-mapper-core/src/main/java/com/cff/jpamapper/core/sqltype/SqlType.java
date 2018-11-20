package com.cff.jpamapper.core.sqltype;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

public interface SqlType {

	public SqlCommandType getSqlCommandType();

	String makeSql(Class<?> entity, Method method);
}
