package cn.pomit.jpamapper.core.sql.dialect;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;

public interface Dialect {
	public String getLimitSql(JpaModelEntity jpaModelEntity);
}
