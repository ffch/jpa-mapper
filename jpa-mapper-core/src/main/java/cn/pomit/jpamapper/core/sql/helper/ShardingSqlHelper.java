package cn.pomit.jpamapper.core.sql.helper;

import java.util.Map;

import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.entity.ShardingEntity;
import cn.pomit.jpamapper.core.util.StringUtil;

public class ShardingSqlHelper extends SqlHelper {

	/**
	 * 表名选择绑定方法
	 * @param jpaModelEntity jpaModelEntity
	 * @param isSole true: for one  false:for muti
	 * @param paramPrefix 参数前缀
	 * @return sql语句
	 */
	public static String bindSql(JpaModelEntity jpaModelEntity, boolean isSole, String paramPrefix) {
		ShardingEntity shardingEntity = jpaModelEntity.getShardingEntity();
		StringBuilder sql = new StringBuilder();
		sql.append("<bind name=\"pattern\" value=\"@");
		sql.append(shardingEntity.getEntityFullName());
		sql.append("@");
		if(isSole){
			sql.append(shardingEntity.getMethodPrecis());
			sql.append("(");
			if(StringUtil.isNotEmpty(paramPrefix)){
				sql.append(paramPrefix);
				sql.append(".");
			}
			sql.append(shardingEntity.getFieldName());
		}else{
			sql.append(shardingEntity.getMethodRange());
			sql.append("(");
			sql.append("start, end");
		}
		sql.append(")\" />");
		return sql.toString();
	}
	
	public static String bindSql(JpaModelEntity jpaModelEntity, boolean isSole ) {
		return bindSql(jpaModelEntity, isSole, "object");
	}
	
	public static String bindSqlNoPrefix(JpaModelEntity jpaModelEntity, boolean isSole ) {
		return bindSql(jpaModelEntity, isSole, null);
	}
	
	/**
	 * 多表查询语句
	 * @param jpaModelEntity jpaModelEntity
	 * @param hasCondition hasCondition
	 * @return sql语句
	 */
	public static String shardingSelectSql(JpaModelEntity jpaModelEntity, boolean hasCondition) {
		StringBuilder sql = new StringBuilder();		
		sql.append("<choose>");
        sql.append("<when test='distinct'>");
        sql.append("<foreach collection =\"pattern\" item=\"item\" index=\"index\" separator=\" union \">");	
		sql.append(selectEntitySql(jpaModelEntity));
		sql.append(fromRangeSql(jpaModelEntity));
		sql.append(conditionRangeSql(jpaModelEntity, hasCondition));
		sql.append("</foreach>");
        sql.append("</when>");
        sql.append("<otherwise>");
        sql.append("<foreach collection =\"pattern\" item=\"item\" index=\"index\" separator=\" union all \">");	
		sql.append(selectEntitySql(jpaModelEntity));
		sql.append(fromRangeSql(jpaModelEntity));
		sql.append(conditionRangeSql(jpaModelEntity, hasCondition));
		sql.append("</foreach>");
        sql.append("</otherwise>");
        sql.append("</choose>");
		return sql.toString();
	}
	
	/**
	 * 精确表查询的where条件
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @param hasCondition hasCondition
	 * @return sql语句
	 */
	public static String conditionRangeSql(JpaModelEntity jpaModelEntity, boolean hasCondition) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\" where \" prefixOverrides=\"AND\">");
		sql.append(conditionRangeShardingKeySql(jpaModelEntity));
		if(hasCondition){
			sql.append(conditionEntitySql(jpaModelEntity));
		}
		sql.append("</trim>");
		return sql.toString();
	}
	
	/**
	 * 获取实体的where语句，只获取Column注解的字段
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
	 */
	private static String conditionRangeShardingKeySql(JpaModelEntity jpaModelEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append(jpaModelEntity.getShardingEntity().getFieldDeclaredName());
		sql.append(" between #{start} and #{end} ");
		return sql.toString();
	}
	
	/**
	 * 获取实体的select语句，只获取Column注解的字段
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
	 * @param jpaModelEntity jpaModelEntity
	 * @return sql语句
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
