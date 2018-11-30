package com.github.ffch.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.entity.MethodParameters;
import com.github.ffch.jpamapper.core.sql.helper.PageAndSortSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractPageSortSqlType;

public class FindAllSortedSqlType extends AbstractPageSortSqlType {

	public static final FindAllSortedSqlType INSTANCE = new FindAllSortedSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(PageAndSortSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.sortSql(jpaModelEntity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Override
	public List<MethodParameters> getMethodParameters(JpaModelEntity jpaModelEntity, String methodName) {
		return null;
	}
}
