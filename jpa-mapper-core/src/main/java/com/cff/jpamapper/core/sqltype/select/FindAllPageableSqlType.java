package com.cff.jpamapper.core.sqltype.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sqltype.AbstractPageSortSqlType;

public class FindAllPageableSqlType extends AbstractPageSortSqlType {

	public static final FindAllPageableSqlType INSTANCE = new FindAllPageableSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public boolean hasConceal() {
		return true;
	}

}
