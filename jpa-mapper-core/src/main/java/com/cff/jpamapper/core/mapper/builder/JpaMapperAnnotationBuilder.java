package com.cff.jpamapper.core.mapper.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

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

import com.cff.jpamapper.core.annotation.SelectKey;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.key.JpaMapperKeyGenerator;
import com.cff.jpamapper.core.method.MethodTypeHelper;
import com.cff.jpamapper.core.mybatis.MapperAnnotationBuilder;
import com.cff.jpamapper.core.sql.JpaMapperSqlFactory;
import com.cff.jpamapper.core.sqltype.IgnoreSqlType;
import com.cff.jpamapper.core.sqltype.SqlType;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperAnnotationBuilder extends MapperAnnotationBuilder {
	JpaModelEntity jpaModelEntity;

	public JpaMapperAnnotationBuilder(Configuration configuration, Class<?> type) {
		super(configuration, type);
		assistant.setCurrentNamespace(type.getName());
	}

	public JpaModelEntity getJpaModelEntity() {
		return jpaModelEntity;
	}

	public void setJpaModelEntity(JpaModelEntity jpaModelEntity) {
		this.jpaModelEntity = jpaModelEntity;
	}

	@Override
	public void parseStatement(Method method) {
		final String mappedStatementId = type.getName() + "." + method.getName();
		LanguageDriver languageDriver = assistant.getLanguageDriver(null);
		SqlType jpaMapperSqlType = getJpaMapperSqlType(method);
		if(jpaMapperSqlType instanceof IgnoreSqlType){
			throw new JpaMapperException("未知的方法类型！");
		}
		SqlCommandType sqlCommandType = jpaMapperSqlType.getSqlCommandType();
		Class<?> parameterTypeClass = getParameterType(method);

		SqlSource sqlSource = JpaMapperSqlFactory.createSqlSource(jpaModelEntity, method, jpaMapperSqlType,
				parameterTypeClass, languageDriver, configuration);

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
			JpaMapperKeyGenerator jpaMapperKeyGenerator = processGeneratedValue(mappedStatementId,
					getParameterType(method), languageDriver);
			keyGenerator = jpaMapperKeyGenerator.getKeyGenerator();
			keyProperty = jpaMapperKeyGenerator.getKeyProperty();
			keyColumn = jpaMapperKeyGenerator.getKeyColumn();
			;
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

	public SqlType getJpaMapperSqlType(Method method) {
		return MethodTypeHelper.getSqlCommandType(method);
	}

	/**
	 * 处理 GeneratedValue 注解
	 *
	 * @param entityTable
	 * @param entityColumn
	 * @param generatedValue
	 */
	protected JpaMapperKeyGenerator processGeneratedValue(String baseStatementId, Class<?> parameterTypeClass,
			LanguageDriver languageDriver) {
		JpaMapperKeyGenerator jpaMapperKeyGenerator = new JpaMapperKeyGenerator();
		Field idField = jpaModelEntity.getIdField();
		if(idField == null)return jpaMapperKeyGenerator;
		GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
		
		String fieldName = jpaModelEntity.getIdName();
		String fieldDeclaredName = jpaModelEntity.getIdColumn();
		
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
