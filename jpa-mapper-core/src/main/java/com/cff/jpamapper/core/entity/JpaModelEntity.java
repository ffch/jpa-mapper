package com.cff.jpamapper.core.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JpaModelEntity {
	String tableName;
	Map<String, String> fieldMap = new HashMap<>();
	boolean hasId = false;
	String idName;
	String idColumn;
	Field idField;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	/**
	 * 添加变量和数据库的对应关系
	 * @param fieldName 成员变量
	 * @param columnName 数据库字段名
	 */
	public void addField(String fieldName, String columnName) {
		fieldMap.put(fieldName, columnName);
	}

	public boolean isHasId() {
		return hasId;
	}

	public void setHasId(boolean hasId) {
		this.hasId = hasId;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}

	public Field getIdField() {
		return idField;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

}
