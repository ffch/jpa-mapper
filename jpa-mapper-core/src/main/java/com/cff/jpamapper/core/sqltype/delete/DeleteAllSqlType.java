package com.cff.jpamapper.core.sqltype.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class DeleteAllSqlType implements SqlType {

	public static final DeleteAllSqlType INSTANCE = new DeleteAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		return sql.toString().trim();
	}
}
