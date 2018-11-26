package com.cff.jpamapper.core.sqltype;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.entity.JpaModelEntity;

public interface SqlType {
	public static final String CONDITION_AND = "AND|and|And";
	
	public SqlCommandType getSqlCommandType();

	String makeSql(JpaModelEntity jpaModelEntity, Method method);
	
	String makeShardingSql(JpaModelEntity jpaModelEntity, Method method);
}
