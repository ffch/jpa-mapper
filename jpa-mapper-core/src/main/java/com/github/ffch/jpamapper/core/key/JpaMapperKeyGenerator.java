package com.github.ffch.jpamapper.core.key;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;

public class JpaMapperKeyGenerator {
	KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
	String keyProperty = "id";
	String keyColumn = null;
	
	public KeyGenerator getKeyGenerator() {
		return keyGenerator;
	}
	public void setKeyGenerator(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	public String getKeyProperty() {
		return keyProperty;
	}
	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}
	public String getKeyColumn() {
		return keyColumn;
	}
	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}
}
