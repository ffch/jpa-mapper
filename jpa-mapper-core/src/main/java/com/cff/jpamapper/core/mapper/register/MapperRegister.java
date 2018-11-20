package com.cff.jpamapper.core.mapper.register;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.mapper.builder.JpaMapperAnnotationBuilder;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class MapperRegister {
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private Configuration configuration;

	public MapperRegister(Class<?> mapper, Configuration configuration) {
		this.mapper = mapper;
		this.configuration = configuration;
		scanMappers();
	}

	private void scanMappers() {
		Method[] methods = mapper.getMethods();
		for (Method method : methods) {
			if (method.getAnnotations() == null || method.getAnnotations().length < 1) {
				registerMethod.add(method);
			}
		}
	}

	public void genMappedStatement() {
		JpaMapperAnnotationBuilder jpaMapperAnnotationBuilder = new JpaMapperAnnotationBuilder(configuration, mapper);
		JpaModelEntity jpaModelEntity = parseModel();
		jpaMapperAnnotationBuilder.setJpaModelEntity(jpaModelEntity);
		for (Method method : registerMethod) {
			jpaMapperAnnotationBuilder.parseStatement(method);
		}
	}

	private JpaModelEntity parseModel() {
		JpaModelEntity jpaModelEntity = new JpaModelEntity();
		Class<?> entity = ReflectUtil.findGenericClass(mapper);
		if (entity == null) {
			throw new JpaMapperException("未能获取到Mapper的泛型类型");
		}
		Table tableAnnotation = entity.getAnnotation(Table.class);
		String tableName = entity.getSimpleName();

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
			}
		}
		return jpaModelEntity;
	}
}
