package com.github.ffch.jpamapper.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.SqlSessionFactory;

import com.github.ffch.jpamapper.core.mapper.register.MappedStatementRegister;

public class MapperScanner {
	List<MappedStatementRegister> mappedStatementRegisters = new ArrayList<>();
	
	public void addMappedStatementRegister(MappedStatementRegister mappedStatementRegister) {
		mappedStatementRegisters.add(mappedStatementRegister);
	}

	public void scanAndRegisterJpaMethod() throws SQLException {
		for(MappedStatementRegister mappedStatementRegister : mappedStatementRegisters){
			mappedStatementRegister.registerMappedStatement();
		}
	}
	
	public void scanAndRegisterJpaMethod(List<SqlSessionFactory> sqlSessionFactoryList) throws SQLException {
		for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
        	org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
        	MapperRegistry mapperRegistry = configuration.getMapperRegistry();
        	List<Class<?>> mappers = new ArrayList<>(mapperRegistry.getMappers());
        	MappedStatementRegister mappedStatementRegister = new MappedStatementRegister(configuration);
        	mappedStatementRegister.addMappers(mappers);
        	addMappedStatementRegister(mappedStatementRegister);
        	mappedStatementRegister.registerMappedStatement();
        }
	}
}
