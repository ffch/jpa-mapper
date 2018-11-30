package com.github.ffch.jpamapper.core.sql.dialect;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;

public interface Dialect {
	public String getLimitSql(JpaModelEntity jpaModelEntity);
}
