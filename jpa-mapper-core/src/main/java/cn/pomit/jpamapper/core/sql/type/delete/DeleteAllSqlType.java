package cn.pomit.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;

public class DeleteAllSqlType extends AbstractPrecisSqlType {

	public static final DeleteAllSqlType INSTANCE = new DeleteAllSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		return sql.toString().trim();
	}
}
