package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.AbstractPrecisSqlType;
import com.cff.jpamapper.core.sqltype.SqlType;

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
