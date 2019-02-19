package cn.pomit.jpamapper.core.sql.type;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.MethodParameters;

public abstract class AbstractConcealedSqlType extends AbstractPageSortSqlType {
	
	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makePageSortSql(JpaModelEntity jpaModelEntity, Method method) {
		return null;
	}

	@Override
	public List<MethodParameters> getMethodParameters(JpaModelEntity jpaModelEntity, String methodName) {
		return null;
	}
}
