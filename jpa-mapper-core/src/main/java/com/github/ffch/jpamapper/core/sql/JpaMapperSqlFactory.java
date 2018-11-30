package com.github.ffch.jpamapper.core.sql;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.type.SqlType;
import com.github.ffch.jpamapper.core.util.StringUtil;

public class JpaMapperSqlFactory {

	public static SqlSource createSqlSource(JpaModelEntity jpaModelEntity, Method method, SqlType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver, Configuration configuration) {
		try {
			String sql = "";
			if(jpaModelEntity.isSharding()){
				sql = createShardingSql(jpaModelEntity, method, sqlCommandType);
			}else if(jpaModelEntity.isPageSort()){
				sql = createPageAndSortSql(jpaModelEntity, method, sqlCommandType);
			}else{
				sql = createCommonSql(jpaModelEntity, method, sqlCommandType);
			}
			if (StringUtil.isEmpty(sql))
				return null;
			return languageDriver.createSqlSource(configuration, sql, parameterTypeClass);
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}
	
	public static String createCommonSql(JpaModelEntity jpaModelEntity, Method method, SqlType sqlCommandType) {
		return sqlCommandType.makeSql(jpaModelEntity, method);
	}
	
	public static String createShardingSql(JpaModelEntity jpaModelEntity, Method method, SqlType sqlCommandType) {
		return sqlCommandType.makeShardingSql(jpaModelEntity, method);
	}
	
	public static String createPageAndSortSql(JpaModelEntity jpaModelEntity, Method method, SqlType sqlCommandType) {
		return sqlCommandType.makePageSortSql(jpaModelEntity, method);
	}
	
}
