package cn.pomit.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;

public abstract class AbstractPrecisSqlType implements SqlType {

	@Override
	public String makeShardingSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return makeSql(jpaModelEntity, method);
	}
}
