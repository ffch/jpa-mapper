package com.github.ffch.jpamapper.core.sql.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.exception.JpaMapperException;


public class DefaultSqlHelper extends SqlHelper{

	public static String conditionRegBySql(JpaModelEntity jpaModelEntity, String params[]) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
		
		Map<String, String> ignoreCaseMap = new HashMap<>();
		ignoreCaseMap.put(jpaModelEntity.getIdName().toLowerCase(), jpaModelEntity.getIdColumn());
		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			ignoreCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}

		int index = 1;
		for (String param : params) {
			String fieldDeclaredName = ignoreCaseMap.get(param.toLowerCase());
			if (fieldDeclaredName != null) {
				sql.append("AND ");
				sql.append(fieldDeclaredName);
				sql.append(" = #{param");
				sql.append(index);
				sql.append("} ");
			}
			index++;
		}
		sql.append("</trim>");
		return sql.toString();
	}

	/**
	 * 单id条件
	 * 
	 * @param entity
	 * @param method
	 * @return
	 */
	public static String conditionIdSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" where ");
		if (!jpaModelEntity.isHasId())
			throw new JpaMapperException("未标明id字段！");

		sql.append(jpaModelEntity.getIdColumn());
		sql.append(" = #{id}");

		return sql.toString();
	}

	/**
	 * 多id用
	 * 
	 * @param entity
	 * @param method
	 * @return
	 */
	public static String conditionIdsSql(JpaModelEntity jpaModelEntity, Method method) {
		int count = method.getParameterCount();
		if (count != 1)
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append(" where ");
		if (!jpaModelEntity.isHasId())
			throw new JpaMapperException("未标明id字段！");

		sql.append(jpaModelEntity.getIdColumn());
		sql.append(" in ");
		sql.append(
				"<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" open=\"(\" close=\")\"> ");
		sql.append(" #{item} ");
		sql.append(" </foreach> ");

		return sql.toString();
	}

	/**
	 * 获取实体的where语句，只获取Column注解的字段
	 * 
	 * @param entity
	 * @return
	 */
	public static String conditionEntitySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");

		sql.append(" <if test='object.");
		sql.append(jpaModelEntity.getIdName());
		sql.append(" != null'> and ");
		sql.append(jpaModelEntity.getIdColumn());
		sql.append(" = #{object.");
		sql.append(jpaModelEntity.getIdName());
		sql.append("} </if> ");
		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			sql.append(" <if test='object.");
			sql.append(entry.getKey());
			sql.append("!= null'> and ");
			sql.append(entry.getValue());
			sql.append(" = #{object.");
			sql.append(entry.getKey());
			sql.append("} </if> ");
		}
		sql.append("</trim>");
		return sql.toString();
	}

	/**
	 * 获取实体的select语句，只获取Column注解的字段
	 * 
	 * @param entity
	 * @return
	 */
	public static String selectEntitySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" select \" suffix=\" \" suffixOverrides=\",\">");
		sql.append(jpaModelEntity.getIdColumn());
		sql.append(" ");
		sql.append(jpaModelEntity.getIdName());
		sql.append(",");

		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			sql.append(entry.getValue());
			sql.append(" ");
			sql.append(entry.getKey());
			sql.append(",");
		}

		sql.append("</trim>");

		return sql.toString();
	}

	public static String fromSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(" ");
		return sql.toString();
	}

	public static String updateSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(" ");

		return sql.toString();
	}

	public static String insertSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(" ");
		return sql.toString();
	}

	public static String setCollectionSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" set \" suffix=\" where \" suffixOverrides=\",\">");

		if (!jpaModelEntity.isHasId())
			throw new JpaMapperException("未标明id字段！");

		String idFieldName = jpaModelEntity.getIdColumn();

		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			sql.append(" <if test='object.");
			sql.append(entry.getKey());
			sql.append(" != null'> ");
			sql.append(entry.getValue());
			sql.append(" = #{");
			sql.append("object");
			sql.append(".");
			sql.append(entry.getKey());
			sql.append("}, </if> ");
		}

		sql.append("</trim>");
		sql.append(idFieldName);
		sql.append(" in ");
		sql.append(
				"<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" open=\"(\" close=\")\"> ");
		sql.append(" #{item} ");
		sql.append(" </foreach> ");
		return sql.toString();
	}

	public static String valuesSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");

		StringBuilder valuesSql = new StringBuilder();
		valuesSql.append("<trim prefix=\" VALUES(\" suffix=\")\" suffixOverrides=\",\">");

		sql.append(" <if test='");
		sql.append(jpaModelEntity.getIdName());
		sql.append(" != null'> ");
		sql.append(jpaModelEntity.getIdColumn());
		sql.append(" , </if> ");

		valuesSql.append(" <if test='");
		valuesSql.append(jpaModelEntity.getIdName());
		valuesSql.append(" != null'> ");
		valuesSql.append(" #{");
		valuesSql.append(jpaModelEntity.getIdColumn());
		valuesSql.append("}, </if> ");

		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			sql.append(" <if test='");
			sql.append(entry.getKey());
			sql.append(" != null'> ");
			sql.append(entry.getValue());
			sql.append(" , </if> ");

			valuesSql.append(" <if test='");
			valuesSql.append(entry.getKey());
			valuesSql.append(" != null'> ");
			valuesSql.append(" #{");
			valuesSql.append(entry.getValue());
			valuesSql.append("}, </if> ");
		}

		valuesSql.append("</trim>");
		sql.append("</trim>");

		return sql.append(valuesSql).toString();

	}

	public static String valuesCollectionSql(JpaModelEntity jpaModelEntity, boolean hasId) {
		StringBuilder sql = new StringBuilder();
		StringBuilder valuesSql = new StringBuilder();
		
		sql.append("<trim prefix=\"(\" suffix=\") values \" suffixOverrides=\",\">");
		valuesSql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		String entityParam = "item";

		if(hasId){
			sql.append(jpaModelEntity.getIdColumn());
			sql.append(",");
			valuesSql.append("#{");
			valuesSql.append(entityParam);
			valuesSql.append(".");
			valuesSql.append(jpaModelEntity.getIdName());
			valuesSql.append("},");
		}
		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			sql.append(entry.getValue());
			sql.append(",");
			valuesSql.append("#{");
			valuesSql.append(entityParam);
			valuesSql.append(".");
			valuesSql.append(entry.getKey());
			valuesSql.append("},");
		}
		sql.append("</trim>");
		valuesSql.append("</trim>");
		return sql.append("<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" >")
				.append(valuesSql).append("</foreach>").toString();

	}
}
