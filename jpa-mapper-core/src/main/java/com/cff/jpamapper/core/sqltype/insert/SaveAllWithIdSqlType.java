package com.cff.jpamapper.core.sqltype.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class SaveAllWithIdSqlType implements SqlType {

	public static final SaveAllWithIdSqlType INSTANCE = new SaveAllWithIdSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesCollectionSql(entity, true));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
