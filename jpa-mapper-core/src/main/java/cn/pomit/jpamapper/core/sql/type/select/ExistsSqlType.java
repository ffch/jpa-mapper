package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;

public class ExistsSqlType extends AbstractPrecisSqlType {

	public static final ExistsSqlType INSTANCE = new ExistsSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(DefaultSqlHelper.selectExistSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		sql.append(DefaultSqlHelper.conditionIdSql(jpaModelEntity));
		return sql.toString().trim();
	}
}
