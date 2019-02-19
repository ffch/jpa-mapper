package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;

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
