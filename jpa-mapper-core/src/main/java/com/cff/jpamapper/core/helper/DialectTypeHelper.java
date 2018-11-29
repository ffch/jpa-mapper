package com.cff.jpamapper.core.helper;

import com.cff.jpamapper.core.sql.dialect.Dialect;
import com.cff.jpamapper.core.sql.dialect.imp.MysqlDialect;
import com.cff.jpamapper.core.sql.dialect.imp.OracleDialect;

public class DialectTypeHelper {
	public static final String MYSQL = "MYSQL";
	public static final String ORACLE = "ORACLE";
	
	public static Dialect getDialectType(String databaseName) {
		if (databaseName.equalsIgnoreCase(ORACLE)) {
			return new OracleDialect();
		} else {
			return new MysqlDialect();
		}
	}
	
}
