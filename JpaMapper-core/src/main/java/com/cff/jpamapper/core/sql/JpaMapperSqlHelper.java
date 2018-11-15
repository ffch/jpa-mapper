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
	
	private static String conditionSelectSql(Class<?> entity, Method method) {
		StringBuilder sql = new StringBuilder();
		sql.append("where ");
		String name = method.getName();
		
		String para = name.replaceFirst(MethodTypeHelper.SELECT, "");
		String params[] = para.split(CONDITION_AND);
		
		if (params == null || params.length < 1)
			return null;
		Field fields[] = entity.getDeclaredFields();
		
		Map<String,String> fieldMap = new HashMap<>();
		for (Field field : fields) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				continue;
			}
			
			fieldMap.put(fieldName.toLowerCase(), fieldDeclaredName);
		}
		
		int index = 0;
		for (String param : params) {
			String fieldDeclaredName = fieldMap.get(param.toLowerCase());
			if(fieldDeclaredName != null){
				sql.append(" AND ");
				sql.append(fieldDeclaredName);
				sql.append(" = #{arg");
				sql.append(index);
				sql.append("}");
			}
			index ++;
		}
		
		
		return sql.toString();
	}
	
	/**
	 * 单id条件
	 * @param entity
	 * @param method
	 * @return
	 */
	private static String conditionIdSql(Class<?> entity, Method method) {
		int count = method.getParameterCount();
		if(count != 1)return null;
				
		StringBuilder sql = new StringBuilder();
		sql.append("where ");
		
		Field fields[] = entity.getDeclaredFields();
		count = 0;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if(id == null)continue;
			count ++;
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
		if(count > 1)throw new IllegalStateException("id只能为一个");
		return sql.toString();
	}
	
	/**
	 * 多id用
	 * @param entity
	 * @param method
	 * @return
	 */
	private static String conditionIdsSql(Class<?> entity, Method method) {
		int count = method.getParameterCount();
		if(count != 1)return null;
				
		StringBuilder sql = new StringBuilder();
		sql.append("where ");
		
		Field fields[] = entity.getDeclaredFields();
		count = 0;
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if(id == null)continue;
			count ++;
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
		if(count > 1)throw new IllegalStateException("id只能为一个");
		return sql.toString();
	}

	/**
	 * 获取实体的select语句，只获取Column注解的字段
	 * 
	 * @param mapper
	 * @return
	 */
	private static String selectEntitySql(Class<?> entity) {
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
	private static String selectCountSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) ");

		return sql.toString();
	}

	/**
	 * 判断是否存在的sql语句
	 * 
	 * @return
	 */
	private static String selectExistSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) ");

		return sql.toString();
	}

	private static String fromSql(Class<?> mapper) {
		Table tableAnnotation = mapper.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return null;
		String table = tableAnnotation.name();
		return " from " + table + " ";
	}

	public static String makeUpdateSql(Class<?> entity, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String makeDeleteSql(Class<?> entity, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String makeInsertSql(Class<?> entity, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String makeSelectOneSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(selectEntitySql(entity));
		sql.append(fromSql(entity));
		sql.append(conditionIdSql(entity, method));
		return sql.toString().trim();
	}

	public static String makeSelectAllSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(selectEntitySql(entity));
		sql.append(fromSql(entity));
		if(method.getParameterCount() > 0){
			sql.append(conditionIdsSql(entity, method));
		}
		return sql.toString().trim();
	}
	
	public static String makeSelectBySql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(selectEntitySql(entity));
		sql.append(fromSql(entity));
		sql.append(conditionSelectSql(entity, method));

		return sql.toString().trim();
	}

	public static String makeCountSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(selectCountSql());
		sql.append(fromSql(entity));

		return sql.toString().trim();
	}

	public static String makeExistsSql(Class<?> entity, Method method) {
		final StringBuilder sql = new StringBuilder();
		sql.append(selectExistSql());
		sql.append(fromSql(entity));
		sql.append(conditionIdSql(entity, method));
		return sql.toString().trim();
	}
}
