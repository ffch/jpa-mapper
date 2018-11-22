package com.cff.jpamapper.core.sql;

import java.util.Map;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.entity.ShardingEntity;

public class ShardingSqlHelper extends SqlHelper {

	/**
	 * 表名选择绑定方法
	 * @param jpaModelEntity
	 * @param isSole
	 * @return
	 */
	public static String bindSql(JpaModelEntity jpaModelEntity, boolean isSole) {
		ShardingEntity shardingEntity = jpaModelEntity.getShardingEntity();
		StringBuilder sql = new StringBuilder();
		sql.append("<bind name=\"pattern\" value=\"object.");
		if(isSole){
			sql.append(shardingEntity.getMethodPrecis());
		}else{
			sql.append(shardingEntity.getMethodRange());
		}
		sql.append("()\" />");
		return sql.toString();
	}

	/**
	 * 不去重多表查询
	 * @param jpaModelEntity
	 * @return
	 */
	public static String shardingSelectSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<foreach collection =\"pattern\" item=\"item\" index=\"index\" separator=\" union all \" >");
		sql.append(selectEntitySql(jpaModelEntity));
		sql.append(fromRangeSql(jpaModelEntity));
		sql.append(conditionRangeSql(jpaModelEntity));
		sql.append("</foreach>");
		return sql.toString();
	}
	
	/**
	 * 去重多表查询
	 * @param jpaModelEntity
	 * @return
	 */
	public static String shardingSelectDistinctSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<foreach collection =\"pattern\" item=\"item\" index=\"index\" separator=\" union \" >");
		sql.append(selectEntitySql(jpaModelEntity));
		sql.append(fromRangeSql(jpaModelEntity));
		sql.append(conditionRangeSql(jpaModelEntity));
		sql.append("</foreach>");
		return sql.toString();
	}
	
	/**
	 * 精确表查询的where条件
	 * @param jpaModelEntity
	 * @return
	 */
	public static String conditionSoleSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
		sql.append(conditionShardingKeySql(jpaModelEntity));
		sql.append(conditionEntitySql(jpaModelEntity));
		sql.append("</trim>");
		return sql.toString();
	}
	
	/**
	 * 多表查询的where条件
	 * @param jpaModelEntity
	 * @return
	 */
	public static String conditionRangeSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
		sql.append(conditionRangeShardingKeySql(jpaModelEntity));
		sql.append(conditionEntitySql(jpaModelEntity));
		sql.append("</trim>");
		return sql.toString();
	}
	
	/**
	 * 获取实体的where语句，只获取Column注解的字段
	 * 
	 * @param entity
	 * @return
	 */
	private static String conditionEntitySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		
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
	 * 分表字段的查询条件
	 * @param jpaModelEntity
	 * @return
	 */
	private static String conditionShardingKeySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
		sql.append(" = #{object.");
		sql.append(jpaModelEntity.getShardingEntity().getFieldName());
		sql.append("}");
		return sql.toString();
	}
	
	/**
	 * 分表字段的范围查询条件
	 * @param jpaModelEntity
	 * @return
	 */
	private static String conditionRangeShardingKeySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
		sql.append("between #{start} and #{end}");
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
		
		sql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
		sql.append(" ");
		sql.append(jpaModelEntity.getShardingEntity().getFieldName());
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
	 * 精确表查询
	 * @return
	 */
	public static String fromSoleSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(jpaModelEntity.getShardingEntity().getPrefix());
		sql.append("${pattern}");
		sql.append(jpaModelEntity.getShardingEntity().getSuffix());
		return sql.toString();
	}
	
	/**
	 * 多表查询
	 * @return
	 */
	public static String fromRangeSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(" from ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(jpaModelEntity.getShardingEntity().getPrefix());
		sql.append("${item}");
		sql.append(jpaModelEntity.getShardingEntity().getSuffix());
		return sql.toString();
	}
	
	/**
	 * 插入语句	
	 * @param jpaModelEntity
	 * @return
	 */
	public static String insertSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(jpaModelEntity.getShardingEntity().getPrefix());
		sql.append("${pattern}");
		sql.append(jpaModelEntity.getShardingEntity().getSuffix());
		sql.append(" ");
		return sql.toString();
	}
	
	public static String updateSql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(jpaModelEntity.getTableName());
		sql.append(jpaModelEntity.getShardingEntity().getPrefix());
		sql.append("${pattern}");
		sql.append(jpaModelEntity.getShardingEntity().getSuffix());
		sql.append(" ");

		return sql.toString();
	}
	
	/**
	 * 赋值语句
	 * @param jpaModelEntity
	 * @return
	 */
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
		
		sql.append(" <if test='");
		sql.append(jpaModelEntity.getShardingEntity().getFieldName());
		sql.append(" != null'> ");
		sql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
		sql.append(" , </if> ");

		valuesSql.append(" <if test='");
		valuesSql.append(jpaModelEntity.getShardingEntity().getFieldName());
		valuesSql.append(" != null'> ");
		valuesSql.append(" #{");
		valuesSql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
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

}
