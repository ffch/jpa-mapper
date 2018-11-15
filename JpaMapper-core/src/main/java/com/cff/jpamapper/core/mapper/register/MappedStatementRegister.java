package com.cff.jpamapper.core.mapper.register;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.Configuration;

public class MappedStatementRegister {
	private List<MapperRegister> mapperRegisters = new ArrayList<>();
	private Configuration configuration;
	public MappedStatementRegister() {
	}

	public MappedStatementRegister(Configuration configuration) {
		this.configuration = configuration;	
	}

	public void addMappers(List<Class<?>> mappers) {
		for(Class<?> className : mappers){
			MapperRegister mapperRegister = new MapperRegister(className, configuration);
			mapperRegisters.add(mapperRegister);
		}
	}
	
	public void registerMappedStatement(){
		for(MapperRegister mapperRegister : mapperRegisters){
			mapperRegister.genMappedStatement();
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
