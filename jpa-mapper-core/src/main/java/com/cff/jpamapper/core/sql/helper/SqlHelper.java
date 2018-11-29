package com.cff.jpamapper.core.sql.helper;

import java.util.Map;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;

public abstract class SqlHelper {
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
}
