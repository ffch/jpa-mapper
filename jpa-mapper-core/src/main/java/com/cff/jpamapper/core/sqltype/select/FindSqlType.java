package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.ShardingSqlHelper;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;

public class FindSqlType implements SqlType {

	public static final FindSqlType INSTANCE = new FindSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		if(jpaModelEntity.isSharding())return makeShardingSql(jpaModelEntity, method);
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionIdSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
	
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method){
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(ShardingSqlHelper.bindSql(jpaModelEntity, true));
		sql.append(ShardingSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(ShardingSqlHelper.fromSoleSql(jpaModelEntity));
		sql.append(ShardingSqlHelper.conditionSoleSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
