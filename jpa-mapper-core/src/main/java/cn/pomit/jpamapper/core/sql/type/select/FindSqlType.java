package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.helper.ShardingSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractShardingSqlType;

public class FindSqlType extends AbstractShardingSqlType {

	public static final FindSqlType INSTANCE = new FindSqlType();

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
	
	@Override
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
