package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class CountSqlType implements SqlType {

	public static final CountSqlType INSTANCE = new CountSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectCountSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));

		return sql.toString().trim();
	}
}
