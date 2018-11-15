package com.cff.jpamapper.core.mapper.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.mybatis.MapperAnnotationBuilder;
import com.cff.jpamapper.core.sql.JpaMapperSqlHelper;
import com.cff.jpamapper.core.sql.JpaMapperSqlType;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperAnnotationBuilder extends MapperAnnotationBuilder {

	public JpaMapperAnnotationBuilder(Configuration configuration, Class<?> type) {
		super(configuration, type);
		assistant.setCurrentNamespace(type.getName());
	}

	@Override
	public void parseStatement(Method method) {
		final String mappedStatementId = type.getName() + "." + method.getName();
		LanguageDriver languageDriver = assistant.getLanguageDriver(null);
		JpaMapperSqlType jpaMapperSqlType = getJpaMapperSqlType(method);

		SqlCommandType sqlCommandType = jpaMapperSqlType.getSqlCommandType();
		Class<?> parameterTypeClass = getParameterType(method);

		Type[] types = type.getGenericInterfaces();
		Class<?> entityClass = null;
		for (Type item : types) {
			if (item instanceof ParameterizedType) {
				ParameterizedType t = (ParameterizedType) item;
				Type[] ts = t.getActualTypeArguments();
				Class<?> tmpType = (Class<?>) ts[0];
				if (ReflectUtil.isGeneralClass(tmpType)) {
					entityClass = tmpType;
				}
			}
		}

		SqlSource sqlSource = buildSqlSource(entityClass, method, jpaMapperSqlType, parameterTypeClass, languageDriver);

		StatementType statementType = StatementType.PREPARED;
		ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
		boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
		String resultMapId = null;
		if (isSelect) {
			resultMapId = parseResultMap(method);
		}
		boolean flushCache = !isSelect;
		boolean useCache = isSelect;

		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		String keyProperty = "id";
		String keyColumn = null;

		if (SqlCommandType.INSERT.equals(sqlCommandType) || SqlCommandType.UPDATE.equals(sqlCommandType)) {
			keyGenerator = configuration.isUseGeneratedKeys() ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
		}

		assistant.addMappedStatement(mappedStatementId, sqlSource, statementType, sqlCommandType, null, null,
				// ParameterMapID
				null, parameterTypeClass, resultMapId, getReturnType(method), resultSetType, flushCache, useCache,
				false, keyGenerator, keyProperty, keyColumn,
				// DatabaseID
				null, languageDriver,
				// ResultSets
				null);
	}

	public JpaMapperSqlType getJpaMapperSqlType(Method method) {
		return MethodTypeHelper.getSqlCommandType(method);
	}

	private SqlSource buildSqlSource(Class<?> entity, Method method, JpaMapperSqlType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver) {
		try {
			String sql = null;
			switch (sqlCommandType.getType()) {
			case JpaMapperSqlType.TYPE_FINDONE:
				sql = JpaMapperSqlHelper.makeSelectOneSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_FINDALL:
				sql = JpaMapperSqlHelper.makeSelectAllSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_FINDBY:
				sql = JpaMapperSqlHelper.makeSelectBySql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_COUNT:
				sql = JpaMapperSqlHelper.makeCountSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_EXISTS:
				sql = JpaMapperSqlHelper.makeExistsSql(entity, method);
				break;
			default:
				break;
			}

			if (StringUtil.isEmpty(sql))
				return null;
			return languageDriver.createSqlSource(configuration, sql, parameterTypeClass);
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}

}
