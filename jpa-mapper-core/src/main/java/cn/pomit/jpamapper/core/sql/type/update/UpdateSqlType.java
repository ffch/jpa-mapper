package cn.pomit.jpamapper.core.sql.type.update;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.helper.ShardingSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractShardingSqlType;

public class UpdateSqlType extends AbstractShardingSqlType {

	public static final UpdateSqlType INSTANCE = new UpdateSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.UPDATE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.updateSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.setSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
	
	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(ShardingSqlHelper.bindSql(jpaModelEntity, true));
		sql.append(ShardingSqlHelper.updateSql(jpaModelEntity));
		sql.append(ShardingSqlHelper.setSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
