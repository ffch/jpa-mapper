package com.cff.jpamapper.core.sql.type.update;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.cff.jpamapper.core.sql.type.AbstractPrecisSqlType;
import com.cff.jpamapper.core.sql.type.SqlType;

public class UpdateAllSqlType extends AbstractPrecisSqlType {

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
