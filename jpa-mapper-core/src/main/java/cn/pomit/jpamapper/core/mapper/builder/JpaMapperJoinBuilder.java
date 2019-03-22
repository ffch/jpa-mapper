package cn.pomit.jpamapper.core.mapper.builder;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.TypeDiscriminator;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import cn.pomit.jpamapper.core.entity.JoinEntity;
import cn.pomit.jpamapper.core.entity.JpaModelEntity;
import cn.pomit.jpamapper.core.exception.JpaMapperException;
import cn.pomit.jpamapper.core.helper.MethodTypeHelper;
import cn.pomit.jpamapper.core.mybatis.MapperAnnotationBuilder;
import cn.pomit.jpamapper.core.sql.JpaMapperConcealedSqlFactory;
import cn.pomit.jpamapper.core.sql.type.AbstractConcealedSqlType;
import cn.pomit.jpamapper.core.sql.type.IgnoreSqlType;
import cn.pomit.jpamapper.core.sql.type.SqlType;
import cn.pomit.jpamapper.core.util.StringUtil;

public class JpaMapperJoinBuilder extends MapperAnnotationBuilder {
	JpaModelEntity jpaModelEntity;

	public JpaMapperJoinBuilder(Configuration configuration, Class<?> type) {
		super(configuration, type);
		assistant.setCurrentNamespace(type.getName());
	}

	public JpaModelEntity getJpaModelEntity() {
		return jpaModelEntity;
	}

	public void setJpaModelEntity(JpaModelEntity jpaModelEntity) {
		this.jpaModelEntity = jpaModelEntity;
	}

	public void parseJoinStatement(String methodName) {
		final String mappedStatementId = type.getName() + "." + methodName;
		LanguageDriver languageDriver = assistant.getLanguageDriver(null);
		SqlType jpaMapperSqlType = getJpaMapperSqlType(methodName);
		if (jpaMapperSqlType instanceof IgnoreSqlType) {
			throw new JpaMapperException("复合子查询创建失败！未知方法名：" + methodName);
		}
		SqlCommandType sqlCommandType = jpaMapperSqlType.getSqlCommandType();
		Class<?> parameterTypeClass = ParamMap.class;

		JpaModelEntity targertJpaModelEntity = parseSimpleModel(jpaModelEntity.getJoinEntity().getEntityType());
		targertJpaModelEntity.setJoinEntity(jpaModelEntity.getJoinEntity());
		SqlSource sqlSource = JpaMapperConcealedSqlFactory.createSqlSource(targertJpaModelEntity, jpaMapperSqlType,
				parameterTypeClass, languageDriver, configuration);

		StatementType statementType = StatementType.PREPARED;
		ResultSetType resultSetType = ResultSetType.FORWARD_ONLY;
		boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
		String resultMapId = null;
		if (isSelect) {
			resultMapId = parseResultMap(methodName, (AbstractConcealedSqlType) jpaMapperSqlType,
					jpaModelEntity.getJoinEntity());
		}
		boolean flushCache = !isSelect;
		boolean useCache = isSelect;

		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		String keyProperty = "id";
		String keyColumn = null;

		assistant.addMappedStatement(mappedStatementId, sqlSource, statementType, sqlCommandType, null, null,
				// ParameterMapID
				null, parameterTypeClass, resultMapId, jpaModelEntity.getJoinEntity().getEntityType(), resultSetType,
				flushCache, useCache, false, keyGenerator, keyProperty, keyColumn,
				// DatabaseID
				null, languageDriver,
				// ResultSets
				null);
	}

	private String parseResultMap(String methodName, AbstractConcealedSqlType concealedSqlType, JoinEntity joinEntity) {
		Class<?> returnType = joinEntity.getEntityType();
		ConstructorArgs args = null;
		Results results = null;
		TypeDiscriminator typeDiscriminator = null;
		String resultMapId = generateResultMapName(methodName, joinEntity.getJoinColumns());
		applyResultMap(resultMapId, returnType, argsIf(args), resultsIf(results), typeDiscriminator);
		return resultMapId;
	}

	public String generateResultMapName(String methodName, Map<String, String> map) {
		StringBuilder suffix = new StringBuilder();

		for (String key : map.keySet()) {
			suffix.append("-");
			String typeName = jpaModelEntity.getFieldType().get(key);
			if (StringUtil.isEmpty(typeName) && key.equals(jpaModelEntity.getIdName())) {
				typeName = jpaModelEntity.getIdField().getType().getSimpleName();
			}
			suffix.append(typeName);
		}

		if (suffix.length() < 1) {
			suffix.append("-void");
		}
		return type.getName() + "." + methodName + suffix;
	}

	public SqlType getJpaMapperSqlType(String method) {
		return MethodTypeHelper.getSqlCommandType(method);
	}

	private JpaModelEntity parseSimpleModel(Class<?> entity) {
		JpaModelEntity jpaModelEntity = new JpaModelEntity();

		Table tableAnnotation = entity.getAnnotation(Table.class);
		String tableName = entity.getSimpleName();
		jpaModelEntity.setTargertEntity(entity);
		jpaModelEntity.setId(entity.getSimpleName());
		if (tableAnnotation != null) {
			tableName = tableAnnotation.name();
		}
		jpaModelEntity.setTableName(tableName);

		Field fields[] = entity.getDeclaredFields();
		for (Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			boolean isId = false;
			if (id != null)
				isId = true;

			Column columnAnnotation = field.getAnnotation(Column.class);
			String fieldName = field.getName();
			String fieldDeclaredName = fieldName;
			if (columnAnnotation != null) {
				if (StringUtil.isNotEmpty(columnAnnotation.name())) {
					fieldDeclaredName = columnAnnotation.name();
				}
			} else {
				if (!isId)
					continue;
			}

			if (isId) {
				jpaModelEntity.setHasId(true);
				jpaModelEntity.setIdName(fieldName);
				jpaModelEntity.setIdColumn(fieldDeclaredName);
				jpaModelEntity.setIdField(field);
			} else {
				jpaModelEntity.addField(fieldName, fieldDeclaredName);
				jpaModelEntity.addFieldType(fieldName, field.getType().getSimpleName());
			}
		}
		if(!jpaModelEntity.isHasId()){
			throw new JpaMapperException("JpaMapper要求必须有ID字段！");
		}
		return jpaModelEntity;
	}

}
