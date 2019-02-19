package cn.pomit.jpamapper.core.sql.type.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.helper.MethodTypeHelper;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;
import cn.pomit.jpamapper.core.util.StringUtil;

public class DeleteBySqlType extends AbstractPrecisSqlType {

	public static final DeleteBySqlType INSTANCE = new DeleteBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.deleteSql());
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		
		String name = method.getName();
		String para = name.replaceFirst(MethodTypeHelper.DELETE, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("deleteBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("deleteBy条件不完整！");
		}
		
		sql.append(DefaultSqlHelper.conditionRegBySql(jpaModelEntity, params));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
