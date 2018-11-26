package com.cff.jpamapper.core.sqltype.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.ShardingSqlHelper;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class SaveSqlType implements SqlType {

	public static final SaveSqlType INSTANCE = new SaveSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {		
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.insertSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.valuesSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(ShardingSqlHelper.bindSqlNoPrefix(jpaModelEntity, true));
		sql.append(ShardingSqlHelper.insertSql(jpaModelEntity));
		sql.append(ShardingSqlHelper.valuesSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
