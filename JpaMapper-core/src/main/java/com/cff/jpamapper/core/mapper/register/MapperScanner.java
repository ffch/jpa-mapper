package com.cff.jpamapper.core.mapper.register;

import java.util.ArrayList;
import java.util.List;

public class MapperScanner {
	List<MappedStatementRegister> mappedStatementRegisters = new ArrayList<>();

	public void addMappedStatementRegister(MappedStatementRegister mappedStatementRegister) {
		mappedStatementRegisters.add(mappedStatementRegister);
	}

	public void scanAndRegisterJpaMethod() {
		for(MappedStatementRegister mappedStatementRegister : mappedStatementRegisters){
			mappedStatementRegister.registerMappedStatement();
		}
	}
}
