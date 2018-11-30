package com.github.ffch.jpamapper.core.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaModelEntity {
	String tableName;
	Map<String, String> fieldMap = new HashMap<>();
	boolean hasId = false;
	String idName;
	String idColumn;
	Field idField;
	boolean isSharding = false;
	boolean isPageSort = false;
	ShardingEntity shardingEntity = null;
	Class<?> targertEntity;
	String databaseName;
	
	//分页参数
	List<MethodParameters> methodParametersList = null;
	
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

	public boolean isSharding() {
		return isSharding;
	}

	public void setSharding(boolean isSharding) {
		this.isSharding = isSharding;
	}

	public ShardingEntity getShardingEntity() {
		return shardingEntity;
	}

	public void setShardingEntity(ShardingEntity shardingEntity) {
		this.shardingEntity = shardingEntity;
	}

	public boolean isPageSort() {
		return isPageSort;
	}

	public void setPageSort(boolean isPageSort) {
		this.isPageSort = isPageSort;
	}

	public Class<?> getTargertEntity() {
		return targertEntity;
	}

	public void setTargertEntity(Class<?> targertEntity) {
		this.targertEntity = targertEntity;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<MethodParameters> getMethodParametersList() {
		return methodParametersList;
	}

	public void setMethodParametersList(List<MethodParameters> methodParametersList) {
		this.methodParametersList = methodParametersList;
	}
}
