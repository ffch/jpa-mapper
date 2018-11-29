package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.ShardingSqlHelper;
import com.cff.jpamapper.core.sql.DefaultSqlHelper;
import com.cff.jpamapper.core.sqltype.AbstractShardingSqlType;

public class FindAllSqlType extends AbstractShardingSqlType {

	public static final FindAllSqlType INSTANCE = new FindAllSqlType();

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
		sql.append(" </script>");
		return sql.toString().trim();
	}
	
	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method){
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(ShardingSqlHelper.bindSqlNoPrefix(jpaModelEntity, false));
		sql.append(ShardingSqlHelper.shardingSelectSql(jpaModelEntity, false));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
