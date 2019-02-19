package cn.pomit.jpamapper.core.sql.type.select;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.helper.MethodTypeHelper;
import cn.pomit.jpamapper.core.sql.helper.DefaultSqlHelper;
import cn.pomit.jpamapper.core.sql.type.AbstractPrecisSqlType;
import cn.pomit.jpamapper.core.sql.type.SqlType;
import cn.pomit.jpamapper.core.util.StringUtil;

public class FindBySqlType extends AbstractPrecisSqlType {

	public static final FindBySqlType INSTANCE = new FindBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(DefaultSqlHelper.selectEntitySql(jpaModelEntity));
		sql.append(DefaultSqlHelper.fromSql(jpaModelEntity));
		
		String name = method.getName();
		String para = name.replaceFirst(MethodTypeHelper.SELECT, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		
		sql.append(DefaultSqlHelper.conditionRegBySql(jpaModelEntity, params));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
