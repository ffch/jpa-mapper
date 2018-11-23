package com.cff.jpamapper.core.sqltype.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class DeleteAllSqlType implements SqlType {

	public static final DeleteAllSqlType INSTANCE = new DeleteAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		return sql.toString().trim();
	}
}
