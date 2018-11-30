package com.github.ffch.jpamapper.core.sql.type;

import java.lang.reflect.Method;

import org.apache.ibatis.mapping.SqlCommandType;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;

public interface SqlType {
	public static final String CONDITION_AND = "AND|and|And";
	
	public SqlCommandType getSqlCommandType();

	String makeSql(JpaModelEntity jpaModelEntity, Method method);
	
	String makeShardingSql(JpaModelEntity jpaModelEntity, Method method);
	
	String makePageSortSql(JpaModelEntity jpaModelEntity, Method method);
	
	public default boolean pageSupport(){
		return false;
	}
	
	public default String makeConcealedSql(JpaModelEntity jpaModelEntity){
		return "";
	}
}
