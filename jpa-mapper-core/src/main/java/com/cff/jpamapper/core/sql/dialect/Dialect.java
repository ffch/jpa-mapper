package com.cff.jpamapper.core.sql.dialect;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public interface Dialect {
	public String getLimitSql(JpaModelEntity jpaModelEntity);
}
