package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class FindBySqlType implements SqlType {

	public static final FindBySqlType INSTANCE = new FindBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.fromSql(jpaModelEntity));
		sql.append(JpaMapperSqlHelper.conditionRegBySql(jpaModelEntity, method));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
