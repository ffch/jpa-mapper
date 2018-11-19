package com.cff.jpamapper.core.mapper.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.key.JpaMapperKeyGenerator;
import com.cff.jpamapper.core.key.SelectKey;
import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.mybatis.MapperAnnotationBuilder;
import com.cff.jpamapper.core.sql.JpaMapperSqlFactory;
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

		Class<?> entityClass = ReflectUtil.findGenericClass(type);

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

		JpaMapperKeyGenerator jpaMapperKeyGenerator = processGeneratedValue(entityClass, mappedStatementId,
				getParameterType(method), languageDriver);
		KeyGenerator keyGenerator = jpaMapperKeyGenerator.getKeyGenerator();
		String keyProperty = jpaMapperKeyGenerator.getKeyProperty();
		String keyColumn = jpaMapperKeyGenerator.getKeyColumn();

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
				sql = JpaMapperSqlFactory.makeSelectOneSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_FINDALL:
				sql = JpaMapperSqlFactory.makeSelectAllSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_FINDBATCH:
				sql = JpaMapperSqlFactory.makeSelectBatchSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_FINDBY:
				sql = JpaMapperSqlFactory.makeSelectBySql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_COUNT:
				sql = JpaMapperSqlFactory.makeCountSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_EXISTS:
				sql = JpaMapperSqlFactory.makeExistsSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_DELETE:
				sql = JpaMapperSqlFactory.makeDeleteSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_DELETEENTITY:
				sql = JpaMapperSqlFactory.makeDeleteEntitySql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_DELETEBATCH:
				sql = JpaMapperSqlFactory.makeDeleteBatchSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_DELETEALL:
				sql = JpaMapperSqlFactory.makeDeleteAllSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_SAVE:
				sql = JpaMapperSqlFactory.makeSaveSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_SAVEALL:
				sql = JpaMapperSqlFactory.makeSaveAllSql(entity, method, false);
				break;
			case JpaMapperSqlType.TYPE_SAVEALLWITHID:
				sql = JpaMapperSqlFactory.makeSaveAllSql(entity, method, true);
				break;
			case JpaMapperSqlType.TYPE_UPDATEALL:
				sql = JpaMapperSqlFactory.makeUpdateAllSql(entity, method);
				break;
			case JpaMapperSqlType.TYPE_UPDATE:
				sql = JpaMapperSqlFactory.makeUpdateSql(entity, method);
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

	/**
	 * 处理 GeneratedValue 注解
	 *
	 * @param entityTable
	 * @param entityColumn
	 * @param generatedValue
	 */
	protected JpaMapperKeyGenerator processGeneratedValue(Class<?> entity, String baseStatementId,
			Class<?> parameterTypeClass, LanguageDriver languageDriver) {
		JpaMapperKeyGenerator jpaMapperKeyGenerator = new JpaMapperKeyGenerator();
		Field fields[] = entity.getDeclaredFields();
		GeneratedValue generatedValue = null;
		Field idField = null;
		for (Field field : fields) {
			if (field.isAnnotationPresent(GeneratedValue.class)) {
				generatedValue = field.getAnnotation(GeneratedValue.class);
				idField = field;
			}
		}
		Column columnAnnotation = idField.getAnnotation(Column.class);
		String fieldName = idField.getName();
		String fieldDeclaredName = fieldName;
		if (columnAnnotation != null) {
			if (StringUtil.isNotEmpty(columnAnnotation.name())) {
				fieldDeclaredName = columnAnnotation.name();
			}
		}
		if (generatedValue != null) {
			if ("JDBC".equals(generatedValue.generator())) {
				jpaMapperKeyGenerator.setKeyGenerator(Jdbc3KeyGenerator.INSTANCE);

				jpaMapperKeyGenerator.setKeyProperty(fieldName);
				jpaMapperKeyGenerator.setKeyColumn(fieldDeclaredName);
				return jpaMapperKeyGenerator;
			} else {
				// 允许通过generator来设置获取id的sql,例如mysql=CALL
				// IDENTITY(),hsqldb=SELECT SCOPE_IDENTITY()
				// 允许通过拦截器参数设置公共的generator
				SelectKey selectKey = idField.getAnnotation(SelectKey.class);
				if (selectKey != null) {
					jpaMapperKeyGenerator.setKeyGenerator(
							handleSelectKeyAnnotation(selectKey, baseStatementId, parameterTypeClass, languageDriver));
					jpaMapperKeyGenerator.setKeyProperty(
							StringUtil.isEmpty(selectKey.keyProperty()) ? fieldName : selectKey.keyProperty());
					jpaMapperKeyGenerator.setKeyColumn(selectKey.keyColumn());
					return jpaMapperKeyGenerator;
				} else {
					throw new IllegalArgumentException(fieldName + " - 该字段@GeneratedValue配置只允许以下几种形式:"
							+ "\n2.useGeneratedKeys的@GeneratedValue(generator=\\\"JDBC\\\")  "
							+ "\n3.另外增加注解@SelectKey（非mybatis的SelectKey，但功能一样，只是扩展到Field上）");
				}
			}
		}
		return jpaMapperKeyGenerator;
	}

	public KeyGenerator handleSelectKeyAnnotation(SelectKey selectKeyAnnotation, String baseStatementId,
			Class<?> parameterTypeClass, LanguageDriver languageDriver) {
		String id = baseStatementId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
		Class<?> resultTypeClass = selectKeyAnnotation.resultType();
		StatementType statementType = selectKeyAnnotation.statementType();
		String keyProperty = selectKeyAnnotation.keyProperty();
		String keyColumn = selectKeyAnnotation.keyColumn();
		boolean executeBefore = selectKeyAnnotation.before();

		// defaults
		boolean useCache = false;
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		Integer fetchSize = null;
		Integer timeout = null;
		boolean flushCache = false;
		String parameterMap = null;
		String resultMap = null;
		ResultSetType resultSetTypeEnum = null;

		SqlSource sqlSource = buildSqlSourceFromStrings(selectKeyAnnotation.statement(), parameterTypeClass,
				languageDriver);
		SqlCommandType sqlCommandType = SqlCommandType.SELECT;

		assistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap,
				parameterTypeClass, resultMap, resultTypeClass, resultSetTypeEnum, flushCache, useCache, false,
				keyGenerator, keyProperty, keyColumn, null, languageDriver, null);

		id = assistant.applyCurrentNamespace(id, false);

		MappedStatement keyStatement = configuration.getMappedStatement(id, false);
		SelectKeyGenerator answer = new SelectKeyGenerator(keyStatement, executeBefore);
		configuration.addKeyGenerator(id, answer);
		return answer;
	}
}
