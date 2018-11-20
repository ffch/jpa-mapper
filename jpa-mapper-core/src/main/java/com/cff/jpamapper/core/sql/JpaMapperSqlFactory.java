package com.cff.jpamapper.core.sql;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperSqlFactory {

	public static SqlSource createSqlSource(JpaModelEntity jpaModelEntity, Method method, SqlType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver, Configuration configuration) {
		try {
			String sql = sqlCommandType.makeSql(jpaModelEntity, method);
			if (StringUtil.isEmpty(sql))
				return null;
			return languageDriver.createSqlSource(configuration, sql, parameterTypeClass);
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}
	
}
