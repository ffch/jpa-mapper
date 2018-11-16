package com.cff.jpamapper.core.sql;

import java.lang.reflect.Method;

import com.cff.jpamapper.core.util.ReflectUtil;

public class JpaMapperSqlFactory {

	public static String makeSelectOneSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	public static String makeSelectAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		return sql.toString().trim();
	}

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

	public static String makeSelectBySql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectEntitySql(entity));
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionSelectSql(entity, method));

		return sql.toString().trim();
	}

	public static String makeCountSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectCountSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));

		return sql.toString().trim();
	}

	public static String makeExistsSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.selectExistSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	public static String makeDeleteSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	public static String makeDeleteEntitySql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionEntitySql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	public static String makeDeleteBatchSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		sql.append(JpaMapperSqlHelper.conditionIdsSql(entity, method));
		sql.append(" </script>");
		return sql.toString().trim();
	}

	public static String makeDeleteAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(JpaMapperSqlHelper.deleteSql());
		sql.append(JpaMapperSqlHelper.fromSql(entity));
		return sql.toString().trim();
	}

	public static String makeSaveSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append("<choose> ");
		sql.append("<when test='arg0.");
		sql.append(JpaMapperSqlHelper.getSqlId(entity));
		sql.append("!=null'> ");
		sql.append(JpaMapperSqlHelper.updateSql(entity));
		sql.append(JpaMapperSqlHelper.setSql(entity));
		sql.append("</when> ");
		
		sql.append("<otherwise> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesSql(entity));
		sql.append("</otherwise> ");
		sql.append("</choose> ");	
		sql.append(" </script>");
		return sql.toString().trim();
	}

	public static String makeSaveAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.insertSql(entity));
		sql.append(JpaMapperSqlHelper.valuesCollectionSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();

	}

	public static String makeUpdateAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append("<script> ");
		sql.append(JpaMapperSqlHelper.updateSql(entity));
		sql.append(JpaMapperSqlHelper.setCollectionSql(entity));
		sql.append(" </script>");
		return sql.toString().trim();
	}
}
