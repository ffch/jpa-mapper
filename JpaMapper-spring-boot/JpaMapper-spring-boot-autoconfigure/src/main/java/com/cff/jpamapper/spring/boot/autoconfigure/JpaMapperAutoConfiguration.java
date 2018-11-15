package com.cff.jpamapper.spring.boot.autoconfigure;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.cff.jpamapper.core.mapper.register.MappedStatementRegister;
import com.cff.jpamapper.core.mapper.register.MapperRegister;
import com.cff.jpamapper.core.mapper.register.MapperScanner;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private ApplicationContext applicationContext;
    
    @PostConstruct
    public void addPageInterceptor() {
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
    }
}
