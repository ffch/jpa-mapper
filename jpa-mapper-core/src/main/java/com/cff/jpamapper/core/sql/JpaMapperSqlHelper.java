package com.cff.jpamapper.core.sql;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperSqlHelper {
	public static final String CONDITION_AND = "AND|and|And";

	public static String conditionRegBySql(JpaModelEntity jpaModelEntity, Method method) {
		StringBuilder sql = new StringBuilder();
		sql.append("where 1=1 ");
		String name = method.getName();

		String para = name.replaceFirst(MethodTypeHelper.SELECT, "");
		if (StringUtil.isEmpty(para)) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		String params[] = para.split(CONDITION_AND);

		if (params == null || params.length < 1) {
			throw new JpaMapperException("findBy条件不完整！");
		}
		Map<String, String> ignoreCaseMap = new HashMap<>();
		ignoreCaseMap.put(jpaModelEntity.getIdName().toLowerCase(), jpaModelEntity.getIdColumn());
		for (Map.Entry<String, String> entry : jpaModelEntity.getFieldMap().entrySet()) {
			ignoreCaseMap.put(entry.getKey().toLowerCase(), entry.getValue());
		}

		int index = 0;
		for (String param : params) {
			String fieldDeclaredName = ignoreCaseMap.get(param.toLowerCase());
			if (fieldDeclaredName != null) {
				sql.append(" AND ");
				sql.append(fieldDeclaredName);
				sql.append(" = #{arg");
				sql.append(index);
				sql.append("}");
			}
			index++;
		}

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
		sql.append("where 1=1 ");
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

	/**
	 * count的sql语句
	 * 
	 * @return
	 */
	public static String selectCountSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) ");

		return sql.toString();
	}

	/**
	 * 判断是否存在的sql语句
	 * 
	 * @return
	 */
	public static String selectExistSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) ");
		return sql.toString();
	}

	public static String fromSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(" ");
		return sql.toString();
	}

	/**
	 * 删除的sql语句
	 * 
	 * @return
	 */
	public static String deleteSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("delete ");

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

	public static String setSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" set \" suffix=\" where \" suffixOverrides=\",\">");

		if (!jpaModelEntity.isHasId())
			throw new JpaMapperException("未标明id字段！");

		String idName = jpaModelEntity.getIdName();
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
		sql.append(" = #{");
		sql.append("object");
		sql.append(".");
		sql.append(idName);
		sql.append("}");
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
