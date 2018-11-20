package com.cff.jpamapper.core.method;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;

import com.cff.jpamapper.core.sqltype.IgnoreSqlType;
import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.sqltype.delete.DeleteAllSqlType;
import com.cff.jpamapper.core.sqltype.delete.DeleteBatchSqlType;
import com.cff.jpamapper.core.sqltype.delete.DeleteEntitySqlType;
import com.cff.jpamapper.core.sqltype.delete.DeleteSqlType;
import com.cff.jpamapper.core.sqltype.insert.SaveAllSqlType;
import com.cff.jpamapper.core.sqltype.insert.SaveAllWithIdSqlType;
import com.cff.jpamapper.core.sqltype.insert.SaveSqlType;
import com.cff.jpamapper.core.sqltype.select.CountSqlType;
import com.cff.jpamapper.core.sqltype.select.ExistsSqlType;
import com.cff.jpamapper.core.sqltype.select.FindAllSqlType;
import com.cff.jpamapper.core.sqltype.select.FindBatchSqlType;
import com.cff.jpamapper.core.sqltype.select.FindBySqlType;
import com.cff.jpamapper.core.sqltype.select.FindOneSqlType;
import com.cff.jpamapper.core.sqltype.update.UpdateAllSqlType;
import com.cff.jpamapper.core.sqltype.update.UpdateSqlType;

public class MethodTypeHelper {
	public static final String SELECT = "findBy";
	public static final String UPDATE = "updateBy";
	public static final String DELETE = "deleteBy";
	public static final String INSERT = "saveBy";

	public static Map<String, SqlType> mapperTypeMap = new HashMap<>();

	public static final String SELECT_REG = "findBy[a-zA-z]*|findOne|exists|findAll|count";
	public static final String UPDATE_REG = "updateBy[a-zA-z]*";
	public static final String DELETE_REG = "deleteBy[a-zA-z]*|delete|deleteAll";
	public static final String INSERT_REG = "saveBy[a-zA-z]*|save";

	static {
		mapperTypeMap.put("findOne", FindOneSqlType.INSTANCE);
		mapperTypeMap.put("exists", ExistsSqlType.INSTANCE);
		mapperTypeMap.put("findAll", FindAllSqlType.INSTANCE);
		mapperTypeMap.put("findBatch", FindBatchSqlType.INSTANCE);
		mapperTypeMap.put("count", CountSqlType.INSTANCE);

		mapperTypeMap.put("delete", DeleteSqlType.INSTANCE);
		mapperTypeMap.put("deleteBatch", DeleteBatchSqlType.INSTANCE);
		mapperTypeMap.put("deleteEntity", DeleteEntitySqlType.INSTANCE);
		mapperTypeMap.put("deleteAll", DeleteAllSqlType.INSTANCE);

		mapperTypeMap.put("save", SaveSqlType.INSTANCE);
		mapperTypeMap.put("saveAll", SaveAllSqlType.INSTANCE);
		mapperTypeMap.put("saveAllWithId", SaveAllWithIdSqlType.INSTANCE);

		mapperTypeMap.put("update", UpdateSqlType.INSTANCE);
		mapperTypeMap.put("updateAll", UpdateAllSqlType.INSTANCE);
	}

	public static void main(String args[]) {

	}

	public static SqlType getSqlCommandType(Method method) {
		String name = method.getName();
		SqlType jpaMapperSqlType = IgnoreSqlType.INSTANCE;

		if (name.startsWith(SELECT)) {
			jpaMapperSqlType = FindBySqlType.INSTANCE;
		} else {
			jpaMapperSqlType = mapperTypeMap.getOrDefault(name, IgnoreSqlType.INSTANCE);
		}

		return jpaMapperSqlType;
	}

	public static SqlType getSqlCommandType(String name) {
		SqlType jpaMapperSqlType = IgnoreSqlType.INSTANCE;

		if (name.startsWith(SELECT)) {
			jpaMapperSqlType = FindBySqlType.INSTANCE;
		} else {
			jpaMapperSqlType = mapperTypeMap.getOrDefault(name, IgnoreSqlType.INSTANCE);
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
