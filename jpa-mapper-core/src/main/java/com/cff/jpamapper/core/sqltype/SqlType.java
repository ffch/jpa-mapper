package com.cff.jpamapper.core.sqltype;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public interface SqlType {

	public SqlCommandType getSqlCommandType();

	String makeSql(JpaModelEntity jpaModelEntity, Method method);
}
