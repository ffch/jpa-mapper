## 简化mybatis的使用方式：通用插件JpaMapper之SimpleShardingMapper

### 简介
JpaMapper以Jpa hibernate的风格写mybatis的代码，可以减少手动写sql的烦恼。

SimpleShardingMapper可以实现简单分表的增删改查，简化mybatis的使用。

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

### 分表
以上配置完成以后，mybatis的Mapper直接继承SimpleShardingMapper，同时使用@ShardingKey配置表映射方法即可使用。
实体类需要加上@Table注解，指明数据库表，同时需要和数据库字段对应的变量加上@Column注解，主键加@Id即可

实体及表映射方案示例：

```
import java.io.Serializable;
import javax.persistence.*;

import com.github.ffch.jpamapper.core.annotation.ShardingKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the user_info database table.
 * 
 */
@Table(name="user_info_his")
public class UserInfoHis implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=64)
	private String userName;

	@Column(length=20)
	@ShardingKey(prefix="_", methodPrecis="getTable", methodRange = "getTables")
	private String mobile;

	@Column(length=64)
	private String name;

	@Column(nullable=false, length=32)
	private String passwd;

	@Column(length=2)
	private String valid;

	public UserInfoHis() {
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


其中，@ShardingKey的**methodPrecis和methodRange**指定是**该实体**中的**static**方法，这一点一定要注意。

上面的getTable方法是按照mobile取余标明了从哪一张表中取数据。getTables方法根据起始字段查出一个范围表。


之后，直接使用SimpleShardingMapper定义的方法进行数据库的操作：

示例：

```
import org.apache.ibatis.annotations.Mapper;

import com.github.ffch.boot.domain.UserInfoHis;
import com.github.ffch.jpamapper.core.mapper.SimpleShardingMapper;

@Mapper
public interface UserInfoHisDao extends SimpleShardingMapper<UserInfoHis, String> {
	
}
```



调用方法：直接调用即可。如：

```
@RequestMapping("/findOne/{mobile}")
public List<UserInfoHis> findOne(@PathVariable String mobile){
	UserInfoHis userInfoHis = new UserInfoHis();
	userInfoHis.setMobile(mobile);
	return (List<UserInfoHis>) userInfoHisDao.find(userInfoHis);
}

@RequestMapping("/findAll")
public List<UserInfoHis> findAll(){
	return (List<UserInfoHis>) userInfoHisDao.findAll("100", "400", false);
}

@RequestMapping("/findRange")
public List<UserInfoHis> findRange(){
	UserInfoHis userInfoHis = new UserInfoHis();
	userInfoHis.setValid("1");
	return (List<UserInfoHis>) userInfoHisDao.findRange(userInfoHis, "100", "400", false);
}
```

**分表同样支持id策略取回id值。**


### 项目地址

gitee地址：[https://gitee.com/xiaoyaofeiyang/JpaMapper](https://gitee.com/xiaoyaofeiyang/JpaMapper "https://gitee.com/xiaoyaofeiyang/JpaMapper")

github地址：[https://github.com/feiyangtianyao/jpa-mapper](https://github.com/feiyangtianyao/jpa-mapper "https://github.com/feiyangtianyao/jpa-mapper")