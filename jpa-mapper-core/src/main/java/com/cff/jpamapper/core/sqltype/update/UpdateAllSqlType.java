package com.cff.jpamapper.core.sqltype.update;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class UpdateAllSqlType implements SqlType {

	public static final UpdateAllSqlType INSTANCE = new UpdateAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UPDATE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.updateSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.setCollectionSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
