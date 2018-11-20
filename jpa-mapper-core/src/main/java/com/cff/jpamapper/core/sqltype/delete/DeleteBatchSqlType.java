package com.cff.jpamapper.core.sqltype.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class DeleteBatchSqlType implements SqlType {

	public static final DeleteBatchSqlType INSTANCE = new DeleteBatchSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.conditionIdsSql(jpaModelEntity, method));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
