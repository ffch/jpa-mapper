package com.cff.jpamapper.core.sqltype.delete;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.util.StringUtil;

public class DeleteBySqlType implements SqlType {

	public static final DeleteBySqlType INSTANCE = new DeleteBySqlType();

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public String makeSql(JpaModelEntity jpaModelEntity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(jpaModelEntity));
		
		String name = method.getName();
		String para = name.replaceFirst(MethodTypeHelper.DELETE, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("deleteBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);
		if (params == null || params.length < 1) {
			throw new JpaMapperException("deleteBy条件不完整！");
		}
		
		sql.append(JpaMapperSqlHelper.conditionRegBySql(jpaModelEntity, params));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
