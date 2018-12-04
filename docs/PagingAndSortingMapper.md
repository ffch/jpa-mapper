## 简化mybatis的使用方式：通用插件JpaMapper之PagingAndSortingMapper

### 简介
JpaMapper以Jpa hibernate的风格写mybatis的代码，可以减少手动写sql的烦恼。

PagingAndSortingMapper可以实现带分页和排序的增删改查，简化mybatis的使用。

这里介绍如何快速配置JpaMapper。

### maven依赖
首先配置好mybatis的依赖，这是必须的。

配置JpaMapper的依赖，JpaMapper已经上传到maven中央仓库。

springboot启动方式：
```xml
<dependency>
    <groupId>com.github.ffch</groupId>
    <artifactId>jpa-mapper-spring-boot-starter</artifactId>
    <version>1.2</version>
</dependency>
```

如果当前的spring不支持boot-starter方式，可以只依赖core包：
```xml
<dependency>
    <groupId>com.github.ffch</groupId>
    <artifactId>jpa-mapper-core</artifactId>
    <version>1.2</version>
</dependency>
```

### 启动

springboot配置依赖之后就可以直接启动了，**如果当前的spring不支持boot-starter方式，还需要配置**：

使用@Autowired注入List<SqlSessionFactory\> sqlSessionFactoryList;
在需要调用的类中的初始化方法中加入以下代码：
```
MapperScanner mapperScanner = new MapperScanner();
mapperScanner.scanAndRegisterJpaMethod(sqlSessionFactoryList);
```

### 分页排序
以上配置完成以后，mybatis的Mapper直接继承PagingAndSortingMapper，其中定义的方法可以直接使用，因为继承自CrudMapper，因此CrudMapper的方法也可以使用，同时也支持CrudMapper的findBy等功能。

实体类需要加上@Table注解，指明数据库表，同时需要和数据库字段对应的变量加上@Column注解，主键加@Id即可

支持功能如下：
1. CrudMapper中定义的方法
2. findBy和deleteBy功能
3. PagingAndSortingMapper中定义的方法
4. pageBy分页查询。

mapper示例：

```
import org.apache.ibatis.annotations.Mapper;
import com.github.ffch.jpamapper.core.domain.page.Page;
import com.github.ffch.boot.domain.UserInfo;
import com.github.ffch.jpamapper.core.domain.page.Pageable;
import com.github.ffch.jpamapper.core.mapper.PagingAndSortingMapper;
@Mapper
public interface UserInfoSortDao extends PagingAndSortingMapper<UserInfo, String> {
	Page<UserInfo> pageByPasswd(String passwd, Pageable pageable);
}
```

调用示例：

```
@RequestMapping("/selectAll")
public Collection<UserInfo> selectAll(){
	return userInfoSortDao.findAll();
}

@RequestMapping("/findAllSorted")
public Collection<UserInfo> findAllSorted(){
	Order order = new Order(Direction.ASC, "mobile");
	Order order1 = new Order(Direction.ASC, "userName");
	Sort sort = new Sort(order, order1);
	return userInfoSortDao.findAllSorted(sort );
}
@RequestMapping("/findAllPageable")
public com.github.ffch.jpamapper.core.domain.page.Page findAllPageable(){
	Pageable pageable = new Pageable();
	pageable.setPage(1);
	pageable.setSize(5);
	com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.findAllPageable(pageable);
	return page;
}

@RequestMapping("/findAllPageableSort")
public com.github.ffch.jpamapper.core.domain.page.Page findAllPageableSort(){
	Pageable pageable = new Pageable();
	pageable.setPage(1);
	pageable.setSize(5);
	Order order = new Order(Direction.ASC, "mobile");
	Order order1 = new Order(Direction.ASC, "userName");
	Sort sort = new Sort(order, order1);
	pageable.setSort(sort);
	com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.findAllPageable(pageable);
	return page;
}

@RequestMapping("/findPageable")
public com.github.ffch.jpamapper.core.domain.page.Page findPageable(){
	Pageable pageable = new Pageable();
	pageable.setPage(1);
	pageable.setSize(5);
	com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.pageByPasswd("123", pageable);
	return page;
}

@RequestMapping("/findPageableSort")
public com.github.ffch.jpamapper.core.domain.page.Page findPageableSort(){
	Pageable pageable = new Pageable();
	pageable.setPage(1);
	pageable.setSize(5);
	Order order = new Order(Direction.ASC, "mobile");
	Order order1 = new Order(Direction.ASC, "userName");
	Sort sort = new Sort(order, order1);
	pageable.setSort(sort);
	com.github.ffch.jpamapper.core.domain.page.Page<UserInfo> page =  userInfoSortDao.pageByPasswd("123", pageable);
	return page;
}
```

Pageable类支持分页和排序；
Sort类提供排序功能。

**Page<T> 是分页数据实体。
getPage是页码；getSize是分页大小；getTotalPages分页总数；getTotalElements是所有数量；getContent是分页实体列表。**


实体示例：

```
import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the user_info database table.
 * 
 */
@Table(name="user_info")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=64)
	private String userName;

	@Column(length=20)
	private String mobile;

	@Column(length=64)
	private String name;

	@Column(nullable=false, length=32)
	private String passwd;

	@Column(length=2)
	private String valid;

	public UserInfo() {
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getValid() {
		return this.valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", mobile=" + mobile + ", name=" + name + ", passwd=" + passwd
				+ ", valid=" + valid + "]";
	}
}
```

### 项目地址

gitee地址：[https://gitee.com/xiaoyaofeiyang/JpaMapper](https://gitee.com/xiaoyaofeiyang/JpaMapper "https://gitee.com/xiaoyaofeiyang/JpaMapper")

github地址：[https://github.com/feiyangtianyao/jpa-mapper](https://github.com/feiyangtianyao/jpa-mapper "https://github.com/feiyangtianyao/jpa-mapper")