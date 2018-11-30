package com.github.ffch.jpamapper.core.sql.type.select.conceal;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.helper.PageAndSortSqlHelper;
import com.github.ffch.jpamapper.core.sql.type.AbstractConcealedSqlType;

public class PagedFindAllPageableSqlType extends AbstractConcealedSqlType {

	public static final PagedFindAllPageableSqlType INSTANCE = new PagedFindAllPageableSqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeConcealedSql(JpaModelEntity jpaModelEntity) {
		return PageAndSortSqlHelper.limitForAllSql(jpaModelEntity);
	}
}
