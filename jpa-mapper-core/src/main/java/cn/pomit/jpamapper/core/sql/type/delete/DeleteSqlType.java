package cn.pomit.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.helper.ShardingSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractShardingSqlType;

public class DeleteSqlType extends AbstractShardingSqlType {

	public static final DeleteSqlType INSTANCE = new DeleteSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {		
		final StringBuilder sql = new StringBuilder();
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionIdSql(jpaModelEntity));
		return sql.toString().trim();
	}

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(ShardingSqlHelper.bindSql(jpaModelEntity, true));
		sql.append(ShardingSqlHelper.deleteSql());
		sql.append(ShardingSqlHelper.fromSoleSql(jpaModelEntity));
		sql.append(ShardingSqlHelper.conditionSoleSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
