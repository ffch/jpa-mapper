package cn.pomit.jpamapper.core.entity;

import java.util.Map;

import org.apache.ibatis.mapping.FetchType;

/**
 * 联表实体
 * @author fufei
 *
 */
public class JoinEntity {
	public static int ONE = 0;
	public static int MANY = 1;
	
	private int mappingType = -1;	
	private Map<String,String> joinColumns;
	private Class<?> entityType;
	private String entityName;
	
	private FetchType fetchType;
	
	public Map<String, String> getJoinColumns() {
		return joinColumns;
	}
	public void setJoinColumns(Map<String, String> joinColumns) {
		this.joinColumns = joinColumns;
	}
	public int getMappingType() {
		return mappingType;
	}
	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}
	public Class<?> getEntityType() {
		return entityType;
	}
	public void setEntityType(Class<?> entityType) {
		this.entityType = entityType;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public FetchType getFetchType() {
		return fetchType;
	}
	public void setFetchType(FetchType fetchType) {
		this.fetchType = fetchType;
	}
	
}
