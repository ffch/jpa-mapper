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

import com.cff.jpamapper.core.method.MethodType;
import com.cff.jpamapper.core.mybatis.MapperAnnotationBuilder;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class JpaMapperAnnotationBuilder extends MapperAnnotationBuilder implements MethodType{

	public JpaMapperAnnotationBuilder(Configuration configuration, Class<?> type) {
		super(configuration, type);
		assistant.setCurrentNamespace(type.getName());
	}

	@Override
	public void parseStatement(Method method) {
		final String mappedStatementId = type.getName() + "." + method.getName();
		LanguageDriver languageDriver = assistant.getLanguageDriver(null);
		SqlCommandType sqlCommandType = getSqlCommandType(method);
		Class<?> parameterTypeClass = getParameterType(method);

		Type[] types = type.getGenericInterfaces();
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
	
	@Override
	public SqlCommandType getSqlCommandType(Method method) {
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
	
}
