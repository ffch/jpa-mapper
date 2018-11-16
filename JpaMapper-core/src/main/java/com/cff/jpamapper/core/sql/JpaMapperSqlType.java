package com.cff.jpamapper.core.sql;

import org.apache.ibatis.mapping.SqlCommandType;

public class JpaMapperSqlType {
	public static final int TYPE_FINDONE = 10;
	public static final int TYPE_FINDALL = 11;
	public static final int TYPE_FINDBY = 12;
	public static final int TYPE_COUNT = 13;
	public static final int TYPE_EXISTS = 14;
	public static final int TYPE_FINDBATCH = 15;

	public static final int TYPE_UPDATEBY = 20;
	public static final int TYPE_UPDATEALL = 21;

	public static final int TYPE_DELETE = 30;
	public static final int TYPE_DELETEALL = 31;
	public static final int TYPE_DELETEENTITY = 32;
	public static final int TYPE_DELETEBATCH = 33;
	public static final int TYPE_DELETEBY = 34;

	public static final int TYPE_SAVE = 40;
	public static final int TYPE_SAVEALL = 41;
	public static final int TYPE_SAVEBY = 42;

	public static final int TYPE_IGNORE = 99;

	public static final JpaMapperSqlType SQLTYPE_FINDONE = new JpaMapperSqlType(SqlCommandType.SELECT, TYPE_FINDONE);
	public static final JpaMapperSqlType SQLTYPE_FINDALL = new JpaMapperSqlType(SqlCommandType.SELECT, TYPE_FINDALL);
	public static final JpaMapperSqlType SQLTYPE_FINDBATCH = new JpaMapperSqlType(SqlCommandType.SELECT, TYPE_FINDBATCH);
	public static final JpaMapperSqlType SQLTYPE_COUNT = new JpaMapperSqlType(SqlCommandType.SELECT, TYPE_COUNT);
	public static final JpaMapperSqlType SQLTYPE_EXISTS = new JpaMapperSqlType(SqlCommandType.SELECT, TYPE_EXISTS);

	public static final JpaMapperSqlType SQLTYPE_IGNORE = new JpaMapperSqlType(SqlCommandType.UNKNOWN, TYPE_IGNORE);

	public static final JpaMapperSqlType SQLTYPE_UPDATEALL = new JpaMapperSqlType(SqlCommandType.INSERT, TYPE_UPDATEALL);
	
	public static final JpaMapperSqlType SQLTYPE_SAVE = new JpaMapperSqlType(SqlCommandType.INSERT, TYPE_SAVE);
	public static final JpaMapperSqlType SQLTYPE_SAVEALL = new JpaMapperSqlType(SqlCommandType.INSERT, TYPE_SAVEALL);

	public static final JpaMapperSqlType SQLTYPE_DELETEALL = new JpaMapperSqlType(SqlCommandType.DELETE, TYPE_DELETEALL);
	public static final JpaMapperSqlType SQLTYPE_DELETE = new JpaMapperSqlType(SqlCommandType.DELETE, TYPE_DELETE);
	public static final JpaMapperSqlType SQLTYPE_DELETEENTITY = new JpaMapperSqlType(SqlCommandType.DELETE, TYPE_DELETEENTITY);
	public static final JpaMapperSqlType SQLTYPE_DELETEBATCH = new JpaMapperSqlType(SqlCommandType.DELETE, TYPE_DELETEBATCH);

	SqlCommandType sqlCommandType;
	int type;

	public JpaMapperSqlType() {
	}

	public JpaMapperSqlType(SqlCommandType sqlCommandType, int type) {
		super();
		this.sqlCommandType = sqlCommandType;
		this.type = type;
	}

	public SqlCommandType getSqlCommandType() {
		return sqlCommandType;
	}

	public void setSqlCommandType(SqlCommandType sqlCommandType) {
		this.sqlCommandType = sqlCommandType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
