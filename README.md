[![License](http://img.shields.io/:license-apache-blue.svg "2.0")](http://www.apache.org/licenses/LICENSE-2.0.html)
[![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")]()

## JpaMapper项目简介

- 如果你喜欢Jpa hibernate的简洁写法；
- 或许你不喜欢写sql；
- 或许你用了Mapper工具之后还是要写sql；

那就用JpaMapper吧！JpaMapper是尽量按照JPA hibernate的书写风格，对mybatis进行封装，是CRUD操作更加简单易用，免于不断写sql。

## 主要功能
 1. 增加(C): 提供单个实体保存、批量保存功能。
 2. 查询(R): 提供单/多查询，并支持findBy自定义字段名查询。
 3. 更新(U): 提供单个实体更新、批量更新功能。
 4. 删除(D): 提供单/多删除功能。
 5. 简单易用，继承CrudMapper即可。
 6. 易于切换，从JPA hibernate替换为mybatis，只需要将CrudRepository替换为CrudMapper，当然，mybatis没办法方法重载，所以CrudRepository相同的方法名会做一定的区分。

## 启动说明
springboot启动：
```xml
<dependency>
    <groupId>com.cff</groupId>
    <artifactId>jpa-mapper-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

非AutoConfiguration:
```xml
<dependency>
    <groupId>com.cff</groupId>
    <artifactId>jpa-mapper-core</artifactId>
    <version>0.0.1</version>
</dependency>
```
使用@Autowired注入List<SqlSessionFactory\> sqlSessionFactoryList;

调用：

```
MapperScanner mapperScanner = new MapperScanner();
mapperScanner.scanAndRegisterJpaMethod(sqlSessionFactoryList);
```

## 使用说明
基于mybatis注解方案，需要继承CrudMapper<T, ID>, CrudMapper中定义的方法可以直接使用，查询方法支持findBy + 字段名（And）查询。

## 版权声明
JpaMapper使用 [Apache License 2.0][] 协议.

## 作者信息
      
   作者博客：https://blog.csdn.net/feiyangtianyao
   
   作者邮箱： fufeixiaoyu@163.com

## License
Apache License

