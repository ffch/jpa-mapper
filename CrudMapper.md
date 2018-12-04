
## 简化mybatis的使用方式：通用插件JpaMapper之CrudMapper

### 简介
JpaMapper以Jpa hibernate的风格写mybatis的代码，可以减少手动写sql的烦恼。

CrudMapper可以实现简单的增删改查，并提供findBy和deletBy操作，简化mybatis的使用。

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

### 增删改查
以上配置完成以后，mybatis的Mapper直接继承CrudMapper即可。

可以使用以下方式进行数据库的操作：

1. CrudMapper中定义的方法
2. findBy+字段名进行查询，字段直接可以用And隔开。
3. deleteBy + 字段名进行查询，字段直接可以用And隔开。
4. 主键取回策略@GeneratedValue与@SelectKey


示例：

```
import org.apache.ibatis.annotations.Mapper;
import com.github.ffch.boot.domain.UserInfo;
import com.github.ffch.jpamapper.core.mapper.CrudMapper;

@Mapper
public interface UserInfoDao extends CrudMapper<UserInfo, String> {
	List<UserInfo> findByUserName(String userName);
}
```

实体类需要加上@Table注解，指明数据库表，同时需要和数据库字段对应的变量加上@Column注解，主键加@Id即可，如下：

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

调用方法：直接调用即可。如：

```
@RequestMapping("/findByUserName")
public List<UserInfo> findByUserName(){
	return userInfoDao.findByUserName("admin");
}

@RequestMapping("/findAll")
public List<UserInfo> findAll(){
	return (List<UserInfo>) userInfoDao.findAll();
}
```
其中findByUserName是使用findBy方法。findAll是CrudMapper中定义的方法。

以上示例的主键并没有自动生成，如果想取回主键需要在实体的主键字段上使用@GeneratedValue。
@GeneratedValue(generator="JDBC")代表使用mybatis的Jdbc3KeyGenerator。对应自增。
@GeneratedValue除generator="JDBC"外，支持@SelectKey注解（非mybatis的注解，但和mybatis的注解一致，这里是为了将SelectKey注解扩展到字段上）添加到字段上，和mybatis的@SelectKey注解功能一致，可以自定义主键策略。

### 项目地址

gitee地址：[https://gitee.com/xiaoyaofeiyang/JpaMapper](https://gitee.com/xiaoyaofeiyang/JpaMapper "https://gitee.com/xiaoyaofeiyang/JpaMapper")

github地址：[https://github.com/feiyangtianyao/jpa-mapper](https://github.com/feiyangtianyao/jpa-mapper "https://github.com/feiyangtianyao/jpa-mapper")