package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class FindOneSqlType implements SqlType {

	public static final FindOneSqlType INSTANCE = new FindOneSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}
}
