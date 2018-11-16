package com.cff.jpamapper.core.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperSqlHelper {
	public static final String CONDITION_AND = "AND|and|And";

	public static String conditionSelectSql(Class<?> entity, Method method) {
		StringBuilder sql = new StringBuilder();
		sql.append("where 1=1 ");
		String name = method.getName();

		String para = name.replaceFirst(MethodTypeHelper.SELECT, "");
		String params[] = para.split(CONDITION_AND);

		if (params == null || params.length < 1)
			return null;
		Field fields[] = entity.getDeclaredFields();

		Map<String, String> fieldMap = new HashMap<>();
		for (Field field : fields) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				Id id = field.getAnnotation(Id.class);
				if (id == null)
					continue;
			}

			fieldMap.put(fieldName.toLowerCase(), fieldDeclaredName);
		}

		int index = 0;
		for (String param : params) {
			String fieldDeclaredName = fieldMap.get(param.toLowerCase());
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
	public static String conditionIdSql(Class<?> entity, Method method) {
		int count = method.getParameterCount();
		if (count != 1)
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append("where ");

		Field fields[] = entity.getDeclaredFields();
		count = 0;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id == null)
				continue;
			count++;
			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			}

			sql.append(fieldDeclaredName);
			sql.append(" = #{arg0}");
		}
		if (count > 1)
			throw new IllegalStateException("id只能为一个");
		return sql.toString();
	}

	/**
	 * 多id用
	 * 
	 * @param entity
	 * @param method
	 * @return
	 */
	public static String conditionIdsSql(Class<?> entity, Method method) {
		int count = method.getParameterCount();
		if (count != 1)
			return null;

		StringBuilder sql = new StringBuilder();
		sql.append("where ");

		Field fields[] = entity.getDeclaredFields();
		count = 0;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id == null)
				continue;
			count++;
			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			}

			sql.append(fieldDeclaredName);
		}

		if (count > 1)
			throw new IllegalStateException("id只能为一个");
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
	public static String conditionEntitySql(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("where 1=1 ");
		for (Field field : fields) {
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				Id id = field.getAnnotation(Id.class);
				if (id == null)
					continue;
			}
			sql.append(" <if test='arg0.");
			sql.append(fieldName);
			sql.append("!= null'> and ");
			sql.append(fieldDeclaredName);
			sql.append(" = #{arg0.");
			sql.append(fieldName);
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
	public static String selectEntitySql(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for (Field field : fields) {
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				Id id = field.getAnnotation(Id.class);
				if (id == null)
					continue;
			}
			sql.append(fieldDeclaredName);
			sql.append(" ");
			sql.append(fieldName);
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
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

	public static String fromSql(Class<?> mapper) {
		Table tableAnnotation = mapper.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return null;
		String table = tableAnnotation.name();
		return " from " + table + " ";
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

	public static String updateSql(Class<?> mapper) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		Table tableAnnotation = mapper.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return null;
		String table = tableAnnotation.name();
		sql.append(table);

		return sql.toString();
	}

	public static String insertSql(Class<?> mapper) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		Table tableAnnotation = mapper.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return null;
		String table = tableAnnotation.name();
		sql.append(table);
		return sql.toString();
	}

	public static String setSql(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append(" set ");

		String idName = null;
		String idFieldName = null;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				idName = fieldName;
				idFieldName = fieldDeclaredName;
			}
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
					idFieldName = fieldDeclaredName;
				}
			} else {
				continue;
			}

			sql.append(" <if test='arg0.");
			sql.append(fieldName);
			sql.append(" != null'> ");
			sql.append(fieldDeclaredName);
			sql.append(" = #{");
			sql.append("arg0");
			sql.append(".");
			sql.append(fieldName);
			if (i == fields.length - 1) {
				sql.append("} </if> ");
			} else {
				sql.append("}, </if> ");
			}
		}
		sql.append(" where ");
		if (StringUtil.isEmpty(idName)) {
			throw new IllegalStateException("找不到更新的id?");
		}
		sql.append(idFieldName);
		sql.append(" = #{");
		sql.append("arg0");
		sql.append(".");
		sql.append(idName);
		sql.append("}");
		return sql.toString();
	}
	
	public static String setCollectionSql(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append(" set ");

		String idName = null;
		String idFieldName = null;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Id id = field.getAnnotation(Id.class);
			if (id != null) {
				idName = fieldName;
				idFieldName = fieldDeclaredName;
			}
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
					idFieldName = fieldDeclaredName;
				}
			} else {
				continue;
			}

			sql.append(" <if test='arg0.");
			sql.append(fieldName);
			sql.append(" != null'> ");
			sql.append(fieldDeclaredName);
			sql.append(" = #{");
			sql.append("arg0");
			sql.append(".");
			sql.append(fieldName);
			if (i == fields.length - 1) {
				sql.append("} </if> ");
			} else {
				sql.append("}, </if> ");
			}
		}
		sql.append(" where ");
		if (StringUtil.isEmpty(idName)) {
			throw new IllegalStateException("找不到更新的id?");
		}
		sql.append(idFieldName);
		sql.append(" in ");
		sql.append(
				"<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" open=\"(\" close=\")\"> ");
		sql.append(" #{item} ");
		sql.append(" </foreach> ");
		return sql.toString();
	}

	public static String valuesSql(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		StringBuilder valuesSql = new StringBuilder();
		valuesSql.append("(");
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				Id id = field.getAnnotation(Id.class);
				if (id == null)
					continue;
			}
			sql.append(" <if test='arg0.");
			sql.append(fieldName);
			sql.append(" != null'> ");
			sql.append(fieldDeclaredName);

			valuesSql.append(" <if test='arg0.");
			valuesSql.append(fieldName);
			valuesSql.append(" != null'> ");
			valuesSql.append(" #{");
			valuesSql.append("arg0");
			valuesSql.append(".");
			valuesSql.append(fieldName);

			if (i == fields.length - 1) {
				sql.append(" </if> ");
				valuesSql.append("} </if> ");
			} else {
				sql.append(" , </if> ");
				valuesSql.append("}, </if> ");
			}
		}
		if (sql.toString().endsWith(",")) {
			sql.deleteCharAt(sql.length() - 1);
		}
		if (valuesSql.toString().endsWith(",")) {
			valuesSql.deleteCharAt(sql.length() - 1);
		}
		valuesSql.append(")");
		sql.append(") values ");

		return sql.append("<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" >")
				.append(valuesSql).append("</foreach>").toString();

	}

	public static String valuesCollectionSql(Class<?> entity, boolean hasId) {
		Field fields[] = entity.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		StringBuilder valuesSql = new StringBuilder();
		String entityParam = "item";

		valuesSql.append("(");
		for (Field field : fields) {
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Column columnAnnotation = field.getAnnotation(Column.class);
			Id id = field.getAnnotation(Id.class);
			if(id != null && !hasId){
				continue;
			}
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				continue;
			}
			sql.append(fieldDeclaredName);
			sql.append(",");
			valuesSql.append("#{");
			valuesSql.append(entityParam);
			valuesSql.append(".");
			valuesSql.append(fieldName);
			valuesSql.append("},");

		}
		sql.deleteCharAt(sql.length() - 1);
		valuesSql.deleteCharAt(sql.length() - 1);
		valuesSql.append(")");
		sql.append(") values ");

		return sql.append("<foreach collection =\"list\" item=\"item\" index=\"index\" separator=\",\" >")
				.append(valuesSql).append("</foreach>").toString();

	}

	/**
	 * id字段名
	 * 
	 * @param entity
	 * @param method
	 * @return
	 */
	public static String getSqlId(Class<?> entity) {
		Field fields[] = entity.getDeclaredFields();
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if (id == null)
				continue;
			String fieldName = field.getName();
			return fieldName;
		}
		return null;
	}

}
