package com.cff.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.cff.jpamapper.core.sql.type.AbstractPrecisSqlType;
import com.cff.jpamapper.core.sql.type.SqlType;

public class DeleteEntitySqlType extends AbstractPrecisSqlType {

	public static final DeleteEntitySqlType INSTANCE = new DeleteEntitySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionEntitySql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
