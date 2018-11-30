package com.github.ffch.jpamapper.core.mapper.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;

import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.TypeDiscriminator;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.github.ffch.jpamapper.core.annotation.SelectKey;
import com.github.ffch.jpamapper.core.domain.conceal.PagedResult;
import com.github.ffch.jpamapper.core.domain.page.PageConstant;
import com.github.ffch.jpamapper.core.entity.JpaModelEntity;
import com.github.ffch.jpamapper.core.entity.MethodParameters;
import com.github.ffch.jpamapper.core.helper.MethodTypeHelper;
import com.github.ffch.jpamapper.core.key.JpaMapperKeyGenerator;
import com.github.ffch.jpamapper.core.mybatis.MapperAnnotationBuilder;
import com.github.ffch.jpamapper.core.sql.JpaMapperSqlFactory;
import com.github.ffch.jpamapper.core.sql.type.AbstractPageSortSqlType;
import com.github.ffch.jpamapper.core.sql.type.IgnoreSqlType;
import com.github.ffch.jpamapper.core.sql.type.SqlType;
import com.github.ffch.jpamapper.core.util.StringUtil;

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
			return;
			//throw new JpaMapperException("未知的方法类型！" + method.getName());
		}
		SqlCommandType sqlCommandType = jpaMapperSqlType.getSqlCommandType();
		Class<?> parameterTypeClass = getParameterType(method);

		StatementType statementType = StatementType.PREPARED;
		ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
		boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
		String resultMapId = null;
		if (isSelect) {
			if(jpaMapperSqlType.pageSupport()){
				String methodName = PageConstant.PAGE_METHOD_PREFIX + method.getName();
				AbstractPageSortSqlType abstractPageSortSqlType = (AbstractPageSortSqlType)jpaMapperSqlType;
				List<MethodParameters> methodParametersList = abstractPageSortSqlType.getMethodParameters(jpaModelEntity, method.getName());
				jpaModelEntity.setMethodParametersList(methodParametersList);
				resultMapId = parsePagedResultMap(method, methodName, methodParametersList);
				JpaMapperConcealedBuilder jpaMapperConcealedBuilder = new JpaMapperConcealedBuilder(configuration, type);
				jpaMapperConcealedBuilder.setJpaModelEntity(jpaModelEntity);
				jpaMapperConcealedBuilder.parseConcealStatement(methodName);
			}else{
				resultMapId = parseResultMap(method);
			}
		}
		
		SqlSource sqlSource = JpaMapperSqlFactory.createSqlSource(jpaModelEntity, method, jpaMapperSqlType,
				parameterTypeClass, languageDriver, configuration);
		
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

	private String parsePagedResultMap(Method method, String methodName, List<MethodParameters> methodParametersList) {
		Class<?> returnType = getReturnType(method);
		ConstructorArgs args = method.getAnnotation(ConstructorArgs.class);
		List<PagedResult> results = new ArrayList<>();
		
		PagedResult countPagedResult = new PagedResult();
		countPagedResult.setColumn(PageConstant.COUNT);
		countPagedResult.setProperty(PageConstant.COUNT);
		countPagedResult.setJavaType(Integer.class);
		countPagedResult.setJdbcType(JdbcType.INTEGER);
		results.add(countPagedResult);
		
		StringBuilder reg = new StringBuilder();
		reg.append("{");
		if(methodParametersList != null){
			int index = 0;
			for(MethodParameters item : methodParametersList){
				if(index < 2){
					PagedResult pagedResult = new PagedResult();
					pagedResult.setColumn(item.getProperty());
					pagedResult.setProperty(item.getProperty());
					pagedResult.setJavaType(item.getType());
					results.add(pagedResult);
				}
				
				reg.append(item.getProperty());
				reg.append(" = ");
				reg.append(item.getProperty());
				reg.append(",");
				index++;
			}
			reg.deleteCharAt(reg.length()-1);
		}
		reg.append("}");
		
		PagedResult contentPagedResult = new PagedResult();
		contentPagedResult.setColumn(reg.toString());
		contentPagedResult.setProperty(PageConstant.CONTENT);
		contentPagedResult.setSelect(methodName);
		results.add(contentPagedResult);
		
		
		TypeDiscriminator typeDiscriminator = method.getAnnotation(TypeDiscriminator.class);
		String resultMapId = generateResultMapName(method);
		
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		applyConstructorArgs(argsIf(args), returnType, resultMappings);
		applyPagedResults(results, returnType, resultMappings);
		Discriminator disc = applyDiscriminator(resultMapId, returnType, typeDiscriminator);
		assistant.addResultMap(resultMapId, returnType, null, disc, resultMappings, null);
		createDiscriminatorResultMaps(resultMapId, returnType, typeDiscriminator);
		
		return resultMapId;
	}
	
	private void applyPagedResults(List<PagedResult> results, Class<?> resultType, List<ResultMapping> resultMappings) {
		for (PagedResult result : results) {
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			if (result.isId()) {
				flags.add(ResultFlag.ID);
			}
			@SuppressWarnings("unchecked")
			Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) ((result
					.getTypeHandler() == UnknownTypeHandler.class) ? null : result.getTypeHandler());
			ResultMapping resultMapping = assistant.buildResultMapping(resultType, nullOrEmpty(result.getProperty()),
					nullOrEmpty(result.getColumn()), result.getJavaType() == void.class ? null : result.getJavaType(),
					result.getJdbcType() == JdbcType.UNDEFINED ? null : result.getJdbcType(),
							nestedPagedSelectId(result), null, null, null, typeHandler, flags, null,
					null, configuration.isLazyLoadingEnabled());
			resultMappings.add(resultMapping);
		}
	}

	private String nestedPagedSelectId(PagedResult result) {
		String nestedSelect = result.getSelect();
		if(nestedSelect == null || "".equals(nestedSelect))return null;
		if (!nestedSelect.contains(".")) {
			nestedSelect = type.getName() + "." + nestedSelect;
		}
		return nestedSelect;
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
