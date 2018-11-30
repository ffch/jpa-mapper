package com.github.ffch.jpamapper.core.sql.type.insert;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.helper.DefaultSqlHelper;
import com.github.ffch.jpamapper.core.sql.helper.ShardingSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractShardingSqlType;

public class SaveSqlType extends AbstractShardingSqlType {

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
