package com.cff.jpamapper.core.sql.type.select.conceal;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sql.helper.PageAndSortSqlHelper;
import com.cff.jpamapper.core.sql.type.AbstractConcealedSqlType;

public class PagedFindAllPageableSqlType extends AbstractConcealedSqlType {

	public static final PagedFindAllPageableSqlType INSTANCE = new PagedFindAllPageableSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeConcealedSql(JpaModelEntity jpaModelEntity) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(PageAndSortSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.fromSql(jpaModelEntity));
		sql.append(PageAndSortSqlHelper.limitSql());
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
