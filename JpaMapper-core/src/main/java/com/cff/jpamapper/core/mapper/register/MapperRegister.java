package com.cff.jpamapper.core.mapper.register;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.Configuration;

import com.cff.jpamapper.core.mapper.builder.JpaMapperAnnotationBuilder;

public class MapperRegister{
	private Class<?> mapper;
	private List<Method> registerMethod = new ArrayList<>();
	private Configuration configuration;

	public MapperRegister(Class<?> mapper, Configuration configuration) {
		this.mapper = mapper;
		this.configuration = configuration;
		scanMappers();
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
		JpaMapperAnnotationBuilder jpaMapperAnnotationBuilder = new JpaMapperAnnotationBuilder(configuration, mapper);
		for (Method method : registerMethod) {
			jpaMapperAnnotationBuilder.parseStatement(method);
		}
	}
}
