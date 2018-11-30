package com.github.ffch.boot.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * Mapper 配置
 *
 * @author liuzh
 */
@org.springframework.context.annotation.Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@ConditionalOnBean(SqlSessionFactory.class)
public class MapperConfiguration {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private ApplicationContext applicationContext;
    

    @PostConstruct
    public void addPageInterceptor() {
        System.out.println("哈哈");
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            processConfiguration(sqlSessionFactory.getConfiguration(),null);
        }
    }
    
    /**
     * 配置指定的接口
     *
     * @param configuration
     * @param mapperInterface
     */
    public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
        String prefix;
        if (mapperInterface != null) {
            prefix = mapperInterface.getCanonicalName();
        } else {
            prefix = "";
        }
        for (Object object : new ArrayList<Object>(configuration.getMappedStatements())) {
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                
            }
        }
    }
}

