[![License](http://img.shields.io/:license-apache-blue.svg "2.0")](http://www.apache.org/licenses/LICENSE-2.0.html)
[![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")]()
[![Maven Central](https://img.shields.io/maven-central/v/cn.pomit/jpa-mapper-core.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22cn.pomit%22%20AND%20a:%22jpa-mapper-core%22)


## JpaMapper项目简介

- 如果你喜欢Jpa hibernate的简洁写法；
- 或许你不喜欢写sql；
- 或许你用了Mapper工具之后还是要写sql；

那就用JpaMapper吧！JpaMapper是尽量按照JPA hibernate的书写风格，对mybatis进行封装，是CRUD操作更加简单易用，免于不断写sql。

JpaMapper以动态生成sql替换手动生成sql的过程，并根据注解生成sqlsource的过程去生成sql，并将sql交给mybatis去管理，原理上和自己写sql是一致的，并不会去替换mybatis的底层实现。因为不用担心无法操控，任何可能出现的问题，只需要debug下查看生成的sql和预期的是否一致即可。

## [Gitee](https://gitee.com/ffch/JpaMapper)
## [Github](https://github.com/ffch/jpa-mapper)
## [Get Started](https://www.pomit.cn/jpa-mapper/#/)


## 主要功能
v1.0.0:

 1. 增加(C): 提供单个实体保存、批量保存功能。
 2. 查询(R): 提供单/多查询，并支持findBy自定义字段名查询。
 3. 更新(U): 提供单个实体更新、批量更新功能。
 4. 删除(D): 提供单/多删除功能，并支持deleteBy自定义字段名删除。
 5. 简单易用，继承CrudMapper即可。
 6. 易于切换，从JPA hibernate替换为mybatis，只需要将CrudRepository替换为CrudMapper，当然，mybatis没办法方法重载，所以CrudRepository相同的方法名会做一定的区分。
 
v1.1

 1. 部分逻辑调整优化
 2. 增加简单分表功能
 
v1.2

 1. 部分逻辑调整优化
 2. 增加分页功能、排序功能，因分页写法的不同，暂时只支持mysql和oracle
 3. 支持pageBy分页功能，功能都与jpa hibernate的相似
 
v1.2.1

 1. 修复了jar引入时的bug

v1.2.2

 1. 修复了inseret操作的字段对应bug
 
 v2.0

 1. groupId更改为cn.pomit，2.0版本以后不再用com.github.ffch。
 2. 新增sortBy功能，可以根据And分割字段名进行排序。

## 使用说明
jar包已经上传到maven中央仓库。
https://search.maven.org/search?q=jpa-mapper ，groupId为cn.pomit。
详细使用说明可以在[项目主页](https://www.pomit.cn/jpa-mapper/#/)里查看，也可以在[个人博客JpaMapper目录](https://blog.csdn.net/feiyangtianyao/article/category/8446635)下查看

### Maven依赖

**使用mybatis 3.4.4进行分页存在类型转换错误，因此JpaMapper需要引入spring-mybatis版本1.3.2以上。**

 **springboot启动：** 
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>cn.pomit</groupId>
    <artifactId>jpa-mapper-spring-boot-starter</artifactId>
    <version>2.0</version>
</dependency>
```

 **非AutoConfiguration:** 
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>cn.pomit</groupId>
    <artifactId>jpa-mapper-core</artifactId>
    <version>2.0</version>
</dependency>
```
使用@Autowired注入List<SqlSessionFactory\> sqlSessionFactoryList;
调用：
```
MapperScanner mapperScanner = new MapperScanner();
mapperScanner.scanAndRegisterJpaMethod(sqlSessionFactoryList);
```

### Mapper种类

1. CrudMapper简单的增删改查Mapper，继承CrudMapper可以实现简单的CRUD。
2. SimpleShardingMapper分表Mapper，继承SimpleShardingMapper可以实现分表功能
3. PagingAndSortingMapper分页Mapper，继承PagingAndSortingMapper可以实现分页功能。

Mapper的泛型要指明实体类和主键

### 增删改查CrudMapper

1. 新建实体，实体字段必须加上@Column注解，Id字段加上@Id可以不加@Column。类注解@Table必须要指定表名。
2. 新建Mapper，继承CrudMapper<T, ID>，Mapper上要加上mybatis的Mapper注解。
3. CrudMapper中定义的方法可以直接使用。
4. 查询方法支持findBy + 字段名（And）查询。删除方法支持deleteBy + 字段名（And）删除。

详细使用方法可以在[JpaMapper-CrudMapper](https://www.pomit.cn/jpa-mapper/#/?id=_31-crud)里查看


### 主键策略

1. @GeneratedValue(generator="JDBC"), 使用自增策略，对应mybatis的Jdbc3KeyGenerator
2. @GeneratedValue除generator="JDBC"外，支持@SelectKey注解（非mybatis的注解，但和mybatis的注解一致，这里是为了将SelectKey注解扩展到字段上）添加到**字段**上，和mybatis的@SelectKey注解功能一致，可以自定义主键策略。

### 分表功能SimpleShardingMapper

1. 需要继承SimpleShardingMapper<T, ID>, SimpleShardingMapper中定义的方法可以直接使用，因为mybatis的sql是根据类名+方法名确定唯一，所有SimpleShardingMapper和CrudMapper不能同时使用。
2. 在实体中分表字段增加注解	@ShardingKey(prefix="_", methodPrecis="getTable", methodRange = "getTables")  methodPrecis为了根据字段值精确查找表的后缀。methodRange是为了支持分表字段的between and 操作。其余字段选填。
3. 在实体中增加静态方法（methodPrecis和methodRange指定的方法名），上所示的方法：<br>
```java

	public static String getTable(Object mobile) {
		int index = Integer.parseInt(mobile.toString()) % 5;
		return String.valueOf(index);
	}
	
	public static String[] getTables(Object start, Object end) {
		Map<Integer, String> maps = new HashMap<>();
		int index = 0;
		for(int i = Integer.parseInt(start.toString()); i < Integer.parseInt(end.toString()); i++){
			if(index >= 5)break;
			maps.put(index, String.valueOf(i % 5));
			index++;	
		}
		
		List<String> mapValueList = new ArrayList<String>(maps.values()); 
		String[] arr = new String[mapValueList.size()];
		return mapValueList.toArray(arr);
	}
```

示例使用mobile的取余5寻找分表字段。

详细使用方法可以在[JpaMapper-SimpleShardingMapper](https://www.pomit.cn/jpa-mapper/#/?id=_32-%E5%88%86%E8%A1%A8)里查看


### 分页功能PagingAndSortingMapper

1. 需要继承PagingAndSortingMapper<T, ID>，其中定义的方法可以直接使用，因为继承自CrudMapper，因此CrudMapper的方法也可以使用，同时也支持CrudMapper的findBy等功能。

2. findAllPageable示例：
```java
Pageable pageable = new Pageable();
pageable.setPage(1);
pageable.setSize(5);
Order order = new Order(Direction.ASC, "mobile");
Order order1 = new Order(Direction.ASC, "userName");
Sort sort = new Sort(order, order1);
pageable.setSort(sort);
Page<UserInfo> page =  userInfoSortDao.findAllPageable(pageable);
```
3. pageBy分页查询，因为CrudMapper已经定义了findBy,所以这里换个名字。用法如：
```java
Page<UserInfo> pageByPasswd(String passwd, Pageable pageable);
```
这样就可以以passwd分页查询了。

4. sortBy排序查询。
```java
List<UserInfo> sortByPasswd(String passwd, Sort sort);
```
这样就可以以passwd为条件查询，以sort作为排序条件。


详细使用方法可以在[JpaMapper-PagingAndSortingMapper](https://www.pomit.cn/jpa-mapper/#/?id=_33-%E5%88%86%E9%A1%B5%E6%8E%92%E5%BA%8F)里查看


## 设计原理

https://blog.csdn.net/feiyangtianyao/article/category/8446635

## 版权声明
JpaMapper使用 Apache License 2.0 协议.

## 作者信息
      
   作者博客：https://blog.csdn.net/feiyangtianyao
   
   作者邮箱： fufeixiaoyu@163.com

## License
Apache License

