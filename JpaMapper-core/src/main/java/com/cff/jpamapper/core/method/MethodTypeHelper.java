package com.cff.jpamapper.core.method;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sql.JpaMapperSqlType;

public class MethodTypeHelper {
	public static final String SELECT = "findBy";
	public static final String UPDATE = "updateBy";
	public static final String DELETE = "deleteBy";
	public static final String INSERT = "saveBy";

	public static Map<String, JpaMapperSqlType> mapperTypeMap = new HashMap<>();

	public static final String SELECT_REG = "findBy[a-zA-z]*|findOne|exists|findAll|count";
	public static final String UPDATE_REG = "updateBy[a-zA-z]*";
	public static final String DELETE_REG = "deleteBy[a-zA-z]*|delete|deleteAll";
	public static final String INSERT_REG = "saveBy[a-zA-z]*|save";

	static {
		mapperTypeMap.put("findOne", JpaMapperSqlType.SQLTYPE_FINDONE);
		mapperTypeMap.put("exists", JpaMapperSqlType.SQLTYPE_EXISTS);
		mapperTypeMap.put("findAll", JpaMapperSqlType.SQLTYPE_FINDALL);
		mapperTypeMap.put("findBatch", JpaMapperSqlType.SQLTYPE_FINDBATCH);
		mapperTypeMap.put("count", JpaMapperSqlType.SQLTYPE_COUNT);

		mapperTypeMap.put("delete", JpaMapperSqlType.SQLTYPE_DELETE);
		mapperTypeMap.put("deleteBatch", JpaMapperSqlType.SQLTYPE_DELETEBATCH);
		mapperTypeMap.put("deleteEntity", JpaMapperSqlType.SQLTYPE_DELETEENTITY);
		mapperTypeMap.put("deleteAll", JpaMapperSqlType.SQLTYPE_DELETEALL);

		mapperTypeMap.put("save", JpaMapperSqlType.SQLTYPE_SAVE);
		mapperTypeMap.put("saveAll", JpaMapperSqlType.SQLTYPE_SAVEALL);
		
		mapperTypeMap.put("updateAll", JpaMapperSqlType.SQLTYPE_UPDATEALL);
	}

	public static void main(String args[]) {

	}

	public static JpaMapperSqlType getSqlCommandType(Method method) {
		String name = method.getName();
		JpaMapperSqlType jpaMapperSqlType = new JpaMapperSqlType();

		if (name.startsWith(SELECT)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.SELECT);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_FINDBY);
		} else if (name.startsWith(UPDATE)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.UPDATE);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_UPDATEBY);
		} else if (name.startsWith(DELETE)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.DELETE);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_DELETEBY);
		} else if (name.startsWith(INSERT)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.INSERT);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_SAVEBY);
		} else {
			jpaMapperSqlType = mapperTypeMap.getOrDefault(name, JpaMapperSqlType.SQLTYPE_IGNORE);
		}

		return jpaMapperSqlType;
	}

	public static JpaMapperSqlType getSqlCommandType(String name) {
		JpaMapperSqlType jpaMapperSqlType = new JpaMapperSqlType();

		if (name.startsWith(SELECT)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.SELECT);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_FINDBY);
		} else if (name.startsWith(UPDATE)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.UPDATE);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_UPDATEBY);
		} else if (name.startsWith(DELETE)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.DELETE);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_DELETEBY);
		} else if (name.startsWith(INSERT)) {
			jpaMapperSqlType.setSqlCommandType(SqlCommandType.INSERT);
			jpaMapperSqlType.setType(JpaMapperSqlType.TYPE_SAVEBY);
		} else {
			jpaMapperSqlType = mapperTypeMap.getOrDefault(name, JpaMapperSqlType.SQLTYPE_IGNORE);
		}

		return jpaMapperSqlType;
	}

	public static SqlCommandType getSqlCommandTypeReg(Method method) {
		String name = method.getName();
		if (name.matches(SELECT_REG)) {
			return SqlCommandType.SELECT;
		} else if (name.matches(UPDATE_REG)) {
			return SqlCommandType.UPDATE;
		} else if (name.matches(DELETE_REG)) {
			return SqlCommandType.DELETE;
		} else if (name.matches(INSERT_REG)) {
			return SqlCommandType.INSERT;
		} else {
			return SqlCommandType.UNKNOWN;
		}
	}

	public static SqlCommandType getSqlCommandTypeReg(String name) {
		if (name.matches(SELECT_REG)) {
			return SqlCommandType.SELECT;
		} else if (name.matches(UPDATE_REG)) {
			return SqlCommandType.UPDATE;
		} else if (name.matches(DELETE_REG)) {
			return SqlCommandType.DELETE;
		} else if (name.matches(INSERT_REG)) {
			return SqlCommandType.INSERT;
		} else {
			return SqlCommandType.UNKNOWN;
		}
	}
}
