package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class ExistsSqlType implements SqlType {

	public static final ExistsSqlType INSTANCE = new ExistsSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectExistSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}
}
