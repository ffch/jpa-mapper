package com.cff.jpamapper.core.sql;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperSqlFactory {

	public static SqlSource createSqlSource(Class<?> entity, Method method, SqlType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver, Configuration configuration) {
		try {
			String sql = sqlCommandType.makeSql(entity, method);
			if (StringUtil.isEmpty(sql))
				return null;
			return languageDriver.createSqlSource(configuration, sql, parameterTypeClass);
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}

	@Deprecated
	public static String makeSelectOneSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeSelectAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeSelectBatchSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		if (method.getParameterCount() > 0) {
			sql.append(JpaMapperSqlHelper.conditionIdsSql(entity, method));
		}
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeSelectBySql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionSelectSql(entity, method));

		return sql.toString().trim();
	}

	@Deprecated
	public static String makeCountSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectCountSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));

		return sql.toString().trim();
	}

	@Deprecated
	public static String makeExistsSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectExistSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeDeleteSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeDeleteEntitySql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionEntitySql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeDeleteBatchSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdsSql(entity, method));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeDeleteAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeSaveSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeUpdateSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.updateSql(entity));
		sql.append(JpaMapperSqlHelper.setSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	@Deprecated
	public static String makeSaveAllSql(Class<?> entity, Method method, boolean hasId) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesCollectionSql(entity, hasId));
		sql.append(" </script>");
		return sql.toString().trim();

	}

	@Deprecated
	public static String makeUpdateAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.updateSql(entity));
		sql.append(JpaMapperSqlHelper.setCollectionSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
