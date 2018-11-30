package com.github.ffch.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractPrecisSqlType;

public class CountSqlType extends AbstractPrecisSqlType {

	public static final CountSqlType INSTANCE = new CountSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		StringBuilder sql = new StringBuilder();
		sql.append(DefaultSqlHelper.selectCountSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		return sql.toString().trim();
	}
}
