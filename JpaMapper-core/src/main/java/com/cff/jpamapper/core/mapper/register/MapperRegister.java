package com.cff.jpamapper.core.mapper.register;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.TypeDiscriminator;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.cff.jpamapper.core.method.MethodType;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class MapperRegister implements MethodType {
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private MapperBuilderAssistant assistant;
	private Configuration configuration;
	private Class<?> type;

	public MapperRegister(Class<?> mapper, Configuration configuration) {
		this.mapper = mapper;
		this.configuration = configuration;
		String resource = mapper.getName().replace('.', '/') + ".java (best guess)";
		type = mapper;
		this.assistant = new MapperBuilderAssistant(configuration, resource);
		scanMappers();
		assistant.setCurrentNamespace(type.getName());
	}

	private void scanMappers() {
		Method[] methods = mapper.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getAnnotations() == null || method.getAnnotations().length < 1) {
				registerMethod.add(method);
			}
		}
	}

	public void genMappedStatement() {
		for (Method method : registerMethod) {
			genMappedStatement(method);
		}
	}

	public void genMappedStatement(Method method) {
		final String mappedStatementId = mapper.getName() + "." + method.getName();
		LanguageDriver languageDriver = assistant.getLanguageDriver(null);
		SqlCommandType sqlCommandType = getSqlCommandType(method);
		Class<?> parameterTypeClass = getParameterType(method);

		Type[] types = mapper.getGenericInterfaces();
		Class <?> entityClass = null;
        for (Type item : types) {
            if (item instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) item;
                Type[] ts = t.getActualTypeArguments();
                Class<?> tmpType = (Class<?>)ts[0];
                if(ReflectUtil.isGeneralClass(tmpType)){
                	entityClass = tmpType;
                }
            }
        }

		SqlSource sqlSource = getSqlSource(entityClass, method, sqlCommandType, parameterTypeClass, languageDriver);

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

	private SqlSource getSqlSource(Class<?> mapper, Method method, SqlCommandType sqlCommandType,
			Class<?> parameterTypeClass, LanguageDriver languageDriver) {
		try {
			final StringBuilder sql = new StringBuilder();
			if (sqlCommandType.equals(SqlCommandType.SELECT)) {
				String selectSql = selectSql(mapper);
				if (selectSql == null)
					return null;
				sql.append(selectSql);
				String fromSql = fromSql(mapper);
				if (fromSql == null)
					return null;
				sql.append(fromSql);
				String conditionSql = conditionSql(mapper, method);
				if (conditionSql == null)
					return null;
				sql.append(conditionSql);
				return languageDriver.createSqlSource(configuration, sql.toString().trim(), parameterTypeClass);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new BuilderException("Could not find value method on SQL annotation.  Cause: " + e, e);
		}
	}

	private String conditionSql(Class<?> mapper, Method method) {
		StringBuilder sql = new StringBuilder();
		sql.append("where 1=1 ");
		String name = method.getName();
		String[] params = paramsOnMethod(name);
		if(params == null|| params.length < 1)return null;
		Field fields[] = mapper.getDeclaredFields();
		for (Field field : fields) {
			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null){
				if(StringUtil.isNotEmpty(columnAnnotation.name())){
					fieldDeclaredName = columnAnnotation.name();
				}
			}else{
				continue;
			}
			
			for(String param : params){
				if(fieldName.equalsIgnoreCase(param)){
					sql.append(" AND ");
					sql.append(fieldDeclaredName);
					sql.append(" = #{");
					sql.append(fieldName);
					sql.append("}");
				}
			}
		}
		return sql.toString();
	}

	private String[] paramsOnMethod(String name) {
		String para = null;
		if (name.startsWith(SELECT)) {
			para = name.replaceFirst(SELECT, "");
		} else if (name.startsWith(UPDATE)) {
			para = name.replaceFirst(UPDATE, "");
		} else if (name.startsWith(DELETE)) {
			para = name.replaceFirst(DELETE, "");
		} else if (name.startsWith(INSERT)) {
			para = name.replaceFirst(INSERT, "");
		} else {
			return null;
		}

		String params[] = para.split("AND|and|And");
		return params;
	}

	private String selectSql(Class<?> mapper) {
		Field fields[] = mapper.getDeclaredFields();
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		for (Field field : fields) {
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			Column columnAnnotation = field.getAnnotation(Column.class);
			if (columnAnnotation != null){
				if(StringUtil.isNotEmpty(columnAnnotation.name())){
					fieldDeclaredName = columnAnnotation.name();
				}
			}else{
				continue;
			}
			sql.append(fieldDeclaredName);
			sql.append(" ");
			sql.append(fieldName);
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		return sql.toString();
	}

	private String fromSql(Class<?> mapper) {
		Table tableAnnotation = mapper.getAnnotation(Table.class);
		if (tableAnnotation == null)
			return null;
		String table = tableAnnotation.name();
		return " from " + table + " ";
	}

	private Class<?> getReturnType(Method method) {
		Class<?> returnType = method.getReturnType();
		Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, type);
		if (resolvedReturnType instanceof Class) {
			returnType = (Class<?>) resolvedReturnType;
			if (returnType.isArray()) {
				returnType = returnType.getComponentType();
			}
			// gcode issue #508
			if (void.class.equals(returnType)) {
				ResultType rt = method.getAnnotation(ResultType.class);
				if (rt != null) {
					returnType = rt.value();
				}
			}
		} else if (resolvedReturnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) resolvedReturnType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			if (Collection.class.isAssignableFrom(rawType) || Cursor.class.isAssignableFrom(rawType)) {
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length == 1) {
					Type returnTypeParameter = actualTypeArguments[0];
					if (returnTypeParameter instanceof Class<?>) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						// (gcode issue #443) actual type can be a also a
						// parameterized type
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
					} else if (returnTypeParameter instanceof GenericArrayType) {
						Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter)
								.getGenericComponentType();
						// (gcode issue #525) support List<byte[]>
						returnType = Array.newInstance(componentType, 0).getClass();
					}
				}
			} else if (method.isAnnotationPresent(MapKey.class) && Map.class.isAssignableFrom(rawType)) {
				// (gcode issue 504) Do not look into Maps if there is not
				// MapKey annotation
				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				if (actualTypeArguments != null && actualTypeArguments.length == 2) {
					Type returnTypeParameter = actualTypeArguments[1];
					if (returnTypeParameter instanceof Class<?>) {
						returnType = (Class<?>) returnTypeParameter;
					} else if (returnTypeParameter instanceof ParameterizedType) {
						// (gcode issue 443) actual type can be a also a
						// parameterized type
						returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
					}
				}
			}
		}

		return returnType;
	}

	private SqlCommandType getSqlCommandType(Method method) {
		String name = method.getName();
		if (name.startsWith(SELECT)) {
			return SqlCommandType.SELECT;
		} else if (name.startsWith(UPDATE)) {
			return SqlCommandType.UPDATE;
		} else if (name.startsWith(DELETE)) {
			return SqlCommandType.DELETE;
		} else if (name.startsWith(INSERT)) {
			return SqlCommandType.INSERT;
		} else {
			return SqlCommandType.UNKNOWN;
		}
	}

	private Class<?> getParameterType(Method method) {
		Class<?> parameterType = null;
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (Class<?> currentParameterType : parameterTypes) {
			if (!RowBounds.class.isAssignableFrom(currentParameterType)
					&& !ResultHandler.class.isAssignableFrom(currentParameterType)) {
				if (parameterType == null) {
					parameterType = currentParameterType;
				} else {
					// issue #135
					parameterType = ParamMap.class;
				}
			}
		}
		return parameterType;
	}

	private String parseResultMap(Method method) {
		Class<?> returnType = getReturnType(method);
		ConstructorArgs args = method.getAnnotation(ConstructorArgs.class);
		Results results = method.getAnnotation(Results.class);
		TypeDiscriminator typeDiscriminator = method.getAnnotation(TypeDiscriminator.class);
		String resultMapId = generateResultMapName(method);
		applyResultMap(resultMapId, returnType, argsIf(args), resultsIf(results), typeDiscriminator);
		return resultMapId;
	}

	private Arg[] argsIf(ConstructorArgs args) {
		return args == null ? new Arg[0] : args.value();
	}

	private Result[] resultsIf(Results results) {
		return results == null ? new Result[0] : results.value();
	}

	private String generateResultMapName(Method method) {
		Results results = method.getAnnotation(Results.class);
		if (results != null && !results.id().isEmpty()) {
			return type.getName() + "." + results.id();
		}
		StringBuilder suffix = new StringBuilder();
		for (Class<?> c : method.getParameterTypes()) {
			suffix.append("-");
			suffix.append(c.getSimpleName());
		}
		if (suffix.length() < 1) {
			suffix.append("-void");
		}
		return type.getName() + "." + method.getName() + suffix;
	}

	private void applyResultMap(String resultMapId, Class<?> returnType, Arg[] args, Result[] results,
			TypeDiscriminator discriminator) {
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		applyConstructorArgs(args, returnType, resultMappings);
		applyResults(results, returnType, resultMappings);
		Discriminator disc = applyDiscriminator(resultMapId, returnType, discriminator);
		// TODO add AutoMappingBehaviour
		assistant.addResultMap(resultMapId, returnType, null, disc, resultMappings, null);
		createDiscriminatorResultMaps(resultMapId, returnType, discriminator);
	}

	private void createDiscriminatorResultMaps(String resultMapId, Class<?> resultType,
			TypeDiscriminator discriminator) {
		if (discriminator != null) {
			for (Case c : discriminator.cases()) {
				String caseResultMapId = resultMapId + "-" + c.value();
				List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
				// issue #136
				applyConstructorArgs(c.constructArgs(), resultType, resultMappings);
				applyResults(c.results(), resultType, resultMappings);
				// TODO add AutoMappingBehaviour
				assistant.addResultMap(caseResultMapId, c.type(), resultMapId, null, resultMappings, null);
			}
		}
	}

	private Discriminator applyDiscriminator(String resultMapId, Class<?> resultType, TypeDiscriminator discriminator) {
		if (discriminator != null) {
			String column = discriminator.column();
			Class<?> javaType = discriminator.javaType() == void.class ? String.class : discriminator.javaType();
			JdbcType jdbcType = discriminator.jdbcType() == JdbcType.UNDEFINED ? null : discriminator.jdbcType();
			@SuppressWarnings("unchecked")
			Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (discriminator
					.typeHandler() == UnknownTypeHandler.class ? null : discriminator.typeHandler());
			Case[] cases = discriminator.cases();
			Map<String, String> discriminatorMap = new HashMap<String, String>();
			for (Case c : cases) {
				String value = c.value();
				String caseResultMapId = resultMapId + "-" + value;
				discriminatorMap.put(value, caseResultMapId);
			}
			return assistant.buildDiscriminator(resultType, column, javaType, jdbcType, typeHandler, discriminatorMap);
		}
		return null;
	}

	private void applyResults(Result[] results, Class<?> resultType, List<ResultMapping> resultMappings) {
		for (Result result : results) {
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			if (result.id()) {
				flags.add(ResultFlag.ID);
			}
			@SuppressWarnings("unchecked")
			Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) ((result
					.typeHandler() == UnknownTypeHandler.class) ? null : result.typeHandler());
			ResultMapping resultMapping = assistant.buildResultMapping(resultType, nullOrEmpty(result.property()),
					nullOrEmpty(result.column()), result.javaType() == void.class ? null : result.javaType(),
					result.jdbcType() == JdbcType.UNDEFINED ? null : result.jdbcType(),
					hasNestedSelect(result) ? nestedSelectId(result) : null, null, null, null, typeHandler, flags, null,
					null, isLazy(result));
			resultMappings.add(resultMapping);
		}
	}

	private void applyConstructorArgs(Arg[] args, Class<?> resultType, List<ResultMapping> resultMappings) {
		for (Arg arg : args) {
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			flags.add(ResultFlag.CONSTRUCTOR);
			if (arg.id()) {
				flags.add(ResultFlag.ID);
			}
			@SuppressWarnings("unchecked")
			Class<? extends TypeHandler<?>> typeHandler = (Class<? extends TypeHandler<?>>) (arg
					.typeHandler() == UnknownTypeHandler.class ? null : arg.typeHandler());
			ResultMapping resultMapping = assistant.buildResultMapping(resultType, nullOrEmpty(arg.name()),
					nullOrEmpty(arg.column()), arg.javaType() == void.class ? null : arg.javaType(),
					arg.jdbcType() == JdbcType.UNDEFINED ? null : arg.jdbcType(), nullOrEmpty(arg.select()),
					nullOrEmpty(arg.resultMap()), null, null, typeHandler, flags, null, null, false);
			resultMappings.add(resultMapping);
		}
	}

	private String nullOrEmpty(String value) {
		return value == null || value.trim().length() == 0 ? null : value;
	}

	private String nestedSelectId(Result result) {
		String nestedSelect = result.one().select();
		if (nestedSelect.length() < 1) {
			nestedSelect = result.many().select();
		}
		if (!nestedSelect.contains(".")) {
			nestedSelect = type.getName() + "." + nestedSelect;
		}
		return nestedSelect;
	}

	private boolean hasNestedSelect(Result result) {
		if (result.one().select().length() > 0 && result.many().select().length() > 0) {
			throw new BuilderException("Cannot use both @One and @Many annotations in the same @Result");
		}
		return result.one().select().length() > 0 || result.many().select().length() > 0;
	}

	private boolean isLazy(Result result) {
		boolean isLazy = configuration.isLazyLoadingEnabled();
		if (result.one().select().length() > 0 && FetchType.DEFAULT != result.one().fetchType()) {
			isLazy = (result.one().fetchType() == FetchType.LAZY);
		} else if (result.many().select().length() > 0 && FetchType.DEFAULT != result.many().fetchType()) {
			isLazy = (result.many().fetchType() == FetchType.LAZY);
		}
		return isLazy;
	}
}
