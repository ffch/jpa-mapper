package com.github.ffch.jpamapper.spring.boot.autoconfigure;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.github.ffch.jpamapper.core.MapperScanner;
import com.github.ffch.jpamapper.core.mapper.register.MappedStatementRegister;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper 配置
 *
 * @author liuzh
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class JpaMapperAutoConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    
    @PostConstruct
    public void addPageInterceptor() {
    	try{
	        MapperScanner mapperScanner = new MapperScanner();
	        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
	        	org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
	        	MapperRegistry mapperRegistry = configuration.getMapperRegistry();
	        	List<Class<?>> mappers = new ArrayList<>(mapperRegistry.getMappers());
	        	MappedStatementRegister mappedStatementRegister = new MappedStatementRegister(configuration);
	        	mappedStatementRegister.addMappers(mappers);
	        	mapperScanner.addMappedStatementRegister(mappedStatementRegister);
	        }
	        
	        mapperScanner.scanAndRegisterJpaMethod();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
}
