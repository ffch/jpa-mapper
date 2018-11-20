package com.cff.jpamapper.core.sqltype.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class DeleteSqlType implements SqlType {

	public static final DeleteSqlType INSTANCE = new DeleteSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}
}
