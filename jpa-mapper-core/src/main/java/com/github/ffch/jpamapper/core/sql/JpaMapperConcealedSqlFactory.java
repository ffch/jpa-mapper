package com.github.ffch.jpamapper.core.sql;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.sql.type.SqlType;
import com.github.ffch.jpamapper.core.util.StringUtil;

public class JpaMapperConcealedSqlFactory {

	public static SqlSource createSqlSource(JpaModelEntity jpaModelEntity, SqlType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver, Configuration configuration) {
		try {
			String sql = sqlCommandType.makeConcealedSql(jpaModelEntity);
			
			if (StringUtil.isEmpty(sql))
				return null;
			return languageDriver.createSqlSource(configuration, sql, parameterTypeClass);
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}
	
	
}
