package com.cff.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.cff.jpamapper.core.sql.helper.ShardingSqlHelper;
import com.cff.jpamapper.core.sql.type.AbstractPrecisSqlType;
import com.cff.jpamapper.core.sql.type.SqlType;

public class FindOneSqlType extends AbstractPrecisSqlType {

	public static final FindOneSqlType INSTANCE = new FindOneSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionIdSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
