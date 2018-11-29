package com.cff.jpamapper.core.helper;

import java.util.HashMap;
import java.util.Map;

import com.cff.jpamapper.core.sql.dialect.Dialect;
import com.cff.jpamapper.core.sql.dialect.imp.MysqlDialect;
import com.cff.jpamapper.core.sql.dialect.imp.OracleDialect;

public class DialectTypeHelper {
	public static final String MYSQL = "MYSQL";
	public static final String ORACLE = "ORACLE";
	private static Map<String, Dialect> dialectMap = new HashMap<>();

	static {
		dialectMap.put(MYSQL, new MysqlDialect());
		dialectMap.put(ORACLE, new OracleDialect());
	}

	public static Dialect getDialectType(String databaseName) {
		return dialectMap.getOrDefault(databaseName.toUpperCase(), new MysqlDialect());
	}

	public static void addDialect(String key, Dialect value) {
		dialectMap.put(key.toUpperCase(), value);
	}
}
