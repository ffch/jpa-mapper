package com.cff.jpamapper.core.mapper.register;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.annotation.ShardingKey;
import com.cff.jpamapper.core.entity.JpaModelEntity;
import com.cff.jpamapper.core.entity.ShardingEntity;
import com.cff.jpamapper.core.exception.JpaMapperException;
import com.cff.jpamapper.core.mapper.JMapper;
import com.cff.jpamapper.core.mapper.PagingAndSortingMapper;
import com.cff.jpamapper.core.mapper.SimpleShardingMapper;
import com.cff.jpamapper.core.mapper.builder.JpaMapperAnnotationBuilder;
import com.cff.jpamapper.core.util.ReflectUtil;
import com.cff.jpamapper.core.util.StringUtil;

public class MapperRegister {
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private Configuration configuration;

	public static final int NO_MAPPER = -1;
	public static final int CRUD_MAPPER = 0;
	public static final int SHARDING_MAPPER = 2;
	public static final int PAGESORT_MAPPER = 1;
	
	int type = NO_MAPPER;

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

	public void genMappedStatement(String databaseName) {
		type = checkMapperType();
		if (type == NO_MAPPER)
			return;

		JpaMapperAnnotationBuilder jpaMapperAnnotationBuilder = new JpaMapperAnnotationBuilder(configuration, mapper);
		JpaModelEntity jpaModelEntity = parseModel();
		jpaModelEntity.setDatabaseName(databaseName);
		jpaMapperAnnotationBuilder.setJpaModelEntity(jpaModelEntity);
		for (Method method : registerMethod) {
			jpaMapperAnnotationBuilder.parseStatement(method);
		}
	}

	private int checkMapperType() {
		Class<?> interfases[] = mapper.getInterfaces();
		if (interfases == null || interfases.length < 1) {
			return NO_MAPPER;
		}
		for (Class<?> interfase : interfases) {
			if (ReflectUtil.checkTypeFit(interfase, JMapper.class)) {
				if (interfase.equals(SimpleShardingMapper.class)) {
					return SHARDING_MAPPER;
				} else if (interfase.equals(PagingAndSortingMapper.class)) {
					return PAGESORT_MAPPER;
				} else {
					return CRUD_MAPPER;
				}
			}
		}
		return NO_MAPPER;
	}

	private JpaModelEntity parseModel() {
		JpaModelEntity jpaModelEntity = new JpaModelEntity();
		if (type == SHARDING_MAPPER) {
			jpaModelEntity.setSharding(true);
		}else if(type == PAGESORT_MAPPER){
			jpaModelEntity.setPageSort(true);
		}
		Class<?> entity = ReflectUtil.findGenericClass(mapper);
		if (entity == null) {
			throw new JpaMapperException("未能获取到Mapper的泛型类型");
		}
		Table tableAnnotation = entity.getAnnotation(Table.class);
		String tableName = entity.getSimpleName();
		jpaModelEntity.setTargertEntity(entity);
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
			
			// 判断是否分表
			if (jpaModelEntity.isSharding()) {
				ShardingKey shardingKey = field.getAnnotation(ShardingKey.class);
				if (shardingKey != null) {
					String entityFullName = entity.getCanonicalName();
					ShardingEntity shardingEntity = new ShardingEntity(shardingKey, fieldName, fieldDeclaredName, entityFullName);
					jpaModelEntity.setShardingEntity(shardingEntity);
					continue;
				}
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
