package com.cff.jpamapper.core.sqltype.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class SaveSqlType implements SqlType {

	public static final SaveSqlType INSTANCE = new SaveSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
