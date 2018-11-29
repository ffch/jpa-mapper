package com.cff.jpamapper.core.mapper.register;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
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
	
	public void registerMappedStatement() throws SQLException{
		Environment environment = configuration.getEnvironment();
    	DataSource dataSource = environment.getDataSource();
    	DatabaseMetaData md = dataSource.getConnection().getMetaData();
    	String databaseName = md.getDatabaseProductName();
    	
		for(MapperRegister mapperRegister : mapperRegisters){
			mapperRegister.genMappedStatement(databaseName);
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
