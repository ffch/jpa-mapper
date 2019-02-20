package cn.pomit.jpamapper.core.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.mapping.SqlCommandType;

import cn.pomit.jpamapper.core.domain.page.PageConstant;
import cn.pomit.jpamapper.core.sql.type.IgnoreSqlType;
import cn.pomit.jpamapper.core.sql.type.SqlType;
import cn.pomit.jpamapper.core.sql.type.delete.DeleteAllSqlType;
import cn.pomit.jpamapper.core.sql.type.delete.DeleteBatchSqlType;
import cn.pomit.jpamapper.core.sql.type.delete.DeleteBySqlType;
import cn.pomit.jpamapper.core.sql.type.delete.DeleteEntitySqlType;
import cn.pomit.jpamapper.core.sql.type.delete.DeleteSqlType;
import cn.pomit.jpamapper.core.sql.type.insert.SaveAllSqlType;
import cn.pomit.jpamapper.core.sql.type.insert.SaveAllWithIdSqlType;
import cn.pomit.jpamapper.core.sql.type.insert.SaveSqlType;
import cn.pomit.jpamapper.core.sql.type.select.CountSqlType;
import cn.pomit.jpamapper.core.sql.type.select.ExistsSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindAllPageableSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindAllSortedSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindAllSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindBatchSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindBySqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindOneSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindRangeSqlType;
import cn.pomit.jpamapper.core.sql.type.select.FindSqlType;
import cn.pomit.jpamapper.core.sql.type.select.PageBySqlType;
import cn.pomit.jpamapper.core.sql.type.select.SortBySqlType;
import cn.pomit.jpamapper.core.sql.type.select.conceal.PagedFindAllPageableSqlType;
import cn.pomit.jpamapper.core.sql.type.select.conceal.PagedPageBySqlType;
import cn.pomit.jpamapper.core.sql.type.update.UpdateAllSqlType;
import cn.pomit.jpamapper.core.sql.type.update.UpdateSqlType;

public class MethodTypeHelper {
	public static final String SELECT = "findBy";
	public static final String PAGE = "pageBy";
	public static final String SORT = "sortBy";

	public static final String UPDATE = "updateBy";
	public static final String DELETE = "deleteBy";
	public static final String INSERT = "saveBy";

	private static Map<String, SqlType> mapperTypeMap = new HashMap<>();

	public static final String SELECT_REG = "findBy[a-zA-z]*|findOne|exists|findAll|count";
	public static final String UPDATE_REG = "updateBy[a-zA-z]*";
	public static final String DELETE_REG = "deleteBy[a-zA-z]*|delete|deleteAll";
	public static final String INSERT_REG = "saveBy[a-zA-z]*|save";

	static {
		mapperTypeMap.put("findOne", FindOneSqlType.INSTANCE);
		mapperTypeMap.put("find", FindSqlType.INSTANCE);
		mapperTypeMap.put("findRange", FindRangeSqlType.INSTANCE);
		mapperTypeMap.put("exists", ExistsSqlType.INSTANCE);
		mapperTypeMap.put("findAll", FindAllSqlType.INSTANCE);
		mapperTypeMap.put("findBatch", FindBatchSqlType.INSTANCE);
		mapperTypeMap.put("count", CountSqlType.INSTANCE);

		mapperTypeMap.put("findAllSorted", FindAllSortedSqlType.INSTANCE);
		mapperTypeMap.put("findAllPageable", FindAllPageableSqlType.INSTANCE);
		mapperTypeMap.put("pagedfindAllPageable", PagedFindAllPageableSqlType.INSTANCE);
		mapperTypeMap.put("pagedpageBy", PagedPageBySqlType.INSTANCE);

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

	public static void addSqlType(String key, SqlType value) {
		mapperTypeMap.put(key, value);
	}

	public static void main(String args[]) {
		Field fields[] = MethodTypeHelper.class.getDeclaredFields();
		for (Field item : fields) {
			Class<?> cls1 = item.getClass();
			Class<?> cls2 = item.getType();
			Class<?> cls3 = item.getDeclaringClass();
			System.out.println("登等");
		}

	}

	public static SqlType getSqlCommandType(Method method) {
		String name = method.getName();
		return getSqlCommandType(name);
	}

	public static SqlType getSqlCommandType(String name) {
		SqlType jpaMapperSqlType = IgnoreSqlType.INSTANCE;

		if (name.startsWith(SELECT)) {
			jpaMapperSqlType = FindBySqlType.INSTANCE;
		} else if (name.startsWith(DELETE)) {
			jpaMapperSqlType = DeleteBySqlType.INSTANCE;
		} else if (name.startsWith(PAGE)) {
			jpaMapperSqlType = PageBySqlType.INSTANCE;
		} else if (name.startsWith(SORT)) {
			jpaMapperSqlType = SortBySqlType.INSTANCE;
		} else if (name.startsWith(PageConstant.PAGE_METHOD_PREFIX + PAGE)) {
			jpaMapperSqlType = PagedPageBySqlType.INSTANCE;
		} else {
			jpaMapperSqlType = mapperTypeMap.getOrDefault(name, IgnoreSqlType.INSTANCE);
		}
		return jpaMapperSqlType;
	}

	public static SqlCommandType getSqlCommandTypeReg(Method method) {
		String name = method.getName();
		return getSqlCommandTypeReg(name);
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
