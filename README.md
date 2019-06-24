[![License](http://img.shields.io/:license-apache-blue.svg "2.0")](http://www.apache.org/licenses/LICENSE-2.0.html)
[![JDK 1.8](https://img.shields.io/badge/JDK-1.8-green.svg "JDK 1.8")]()
[![Maven Central](https://img.shields.io/maven-central/v/cn.pomit/mybatis-proxy.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22cn.pomit%22%20AND%20a:%22mybatis-proxy%22)

## Mybatis-proxy项目简介

Mybatis本身并不是为Spring环境而生，但Spring对mybatis得调用做了很好得封装，通过Spring，我们可以方便调用Mapper进行CRUD操作。

然而，我们也可能面临这种情况： 我们的项目无法使用Spring，或者没必要使用。这时，我们将怎样操作数据库呢？

对，我们可以通过JDBC操作数据库。也可以用Mybatis拿到SqlSession去操作数据库。

Mybatis-proxy就是为了简化非Spring环境下Mybatis的使用。通过一次调用并搭配注解实现有/无事务的Mybatis操作。

## [Gitee](https://gitee.com/ffch/mybatis-proxy)
## [Github](https://github.com/ffch/mybatis-proxy)
## [Get Started](https://www.pomit.cn/mybatis-proxy/)

## 主要功能

 1. 快速启动。
 2. 支持注解式事务。
 3. 使用配置文件初始化配置。
 

## 使用说明

jar包已经上传到maven中央仓库。
https://search.maven.org/search?q=mybatis-proxy ，groupId为cn.pomit。

[使用文档地址](https://www.pomit.cn/mybatis-proxy)

### maven依赖

```xml
<dependency>
	<groupId>cn.pomit</groupId>
	<artifactId>mybatis-proxy</artifactId>
	<version>1.0</version>
</dependency>
```

### 初始化

调用MybatisConfiguration.initConfiguration进行初始化，共用以下三个参数，packageName指定扫描mapper的包路径，dataSource指定数据源，这两个属性也可以不传，通过properties配置文件传递。

如果没有properties配置文件，packageName和dataSource必须传。

```java
MybatisConfiguration.initConfiguration(packageName, dataSource, properties);
```

properties配置文件配置要求：

```
#mybatis.mapper.scan=cn.pomit.alarm.mapper
mybatis.transation.scan=cn.pomit.alarm.service
mybatis.datasource.type=POOLED
mybatis.datasource.driver=com.mysql.jdbc.Driver
mybatis.datasource.url=jdbc:mysql://127.0.0.1:3306/boot?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
mybatis.datasource.username=cff
mybatis.datasource.password=123456
```

mybatis.mapper.scan指定了扫描mapper的包路径，如果包路径传递了，该配置无效。

mybatis.datasource.type指定了数据源类型。

mybatis.datasource.xxx顾名思义，就是配置mybatis数据源的，和mybatis要求得参数是对应的，如果传递了数据源，该配置无效。


### 使用方法

#### Mapper写法

实体、Mapper和xml等写法就是Mybatis原生写法。

**Mapper可以使用注解的方式写sql。**

**如果要用xml方式写sql，要将xml文件和mapper接口放到同一个包路径下。xml文件放到classpath下无效。**

mapper接口一定要放到包扫描路径下。

#### 调用方法

业务逻辑调用Mapper需要使用以下方式：

```
UserInfoMapper userInfoMapper = ProxyHandlerFactory.getMapper(UserInfoMapper.class);
```

示例：
```java
import cn.pomit.alarm.domain.UserInfo;
import cn.pomit.alarm.mapper.UserInfoMapper;
import cn.pomit.mybatis.ProxyHandlerFactory;


public class UserInfoServiceImp implements UserInfoService{

	UserInfoMapper userInfoMapper = ProxyHandlerFactory.getMapper(UserInfoMapper.class);

	public UserInfo findUser(String userName) {		
		return userInfoMapper.selectByUserName(userName);
	}

	public int save(UserInfo user) throws Exception{		
		UserInfo test = userInfoMapper.selectByUserName(user.getUserName());
		System.out.println(test);
		if(test == null){
			int ret = userInfoMapper.save(user);
			System.out.println(ret);
			return ret;
		}

		return -1;
	}

}
```

#### 事务处理

在主要事务支持的类上，添加@Transactional注解，该注解是cn.pomit注解，非javax注解，只能用于类上，不能在方法上使用。

添加@Transactional注解的类，不能使用new来创建对象，否则无法开启事务。

要使用如下方式调用：

```java
UserInfoService userInfoService = ProxyHandlerFactory.getTransaction(UserInfoServiceImp.class);
```

因为并未使用cglib，使用jdk的动态代理，所以@Transactional注解的类要实现接口。

示例：
UserInfoServiceImp：

```java
import cn.pomit.alarm.domain.UserInfo;
import cn.pomit.alarm.mapper.UserInfoMapper;
import cn.pomit.mybatis.ProxyHandlerFactory;

@Transactional
public class UserInfoServiceImp implements UserInfoService{

	UserInfoMapper userInfoMapper = ProxyHandlerFactory.getMapper(UserInfoMapper.class);

	public UserInfo findUser(String userName) {		
		return userInfoMapper.selectByUserName(userName);
	}

	public int save(UserInfo user) throws Exception{		
		UserInfo test = userInfoMapper.selectByUserName(user.getUserName());
		System.out.println(test);
		if(test == null){
			int ret = userInfoMapper.save(user);
			System.out.println(ret);
			int i = 1/0; //这里测试事务回滚
			return ret;
		}

		return -1;
	}

}
```

调用示例：

```java
UserInfoService userInfoService = ProxyHandlerFactory.getTransaction(UserInfoServiceImp.class);
UserInfo user = new UserInfo();
user.setUserName("123");
user.setPasswd("345");
userInfoService.save(user);
```

## 与consul-proxy整合

mybatis-proxy本身在consul-proxy项目的开发过程中延申出来的项目，为了consul-proxy项目能够简洁地访问数据库。

因此consul-proxy项目提供了快捷使用mybatis的方式。

在引入consul-proxy和mybatis-proxy的项目中，为了避免手动初始化（MybatisConfiguration.initConfiguration），可以使用注解@EnableMybatis搭配mybatis.configuration配置开启mybatis-proxy的使用。

如：

```java
import cn.pomit.alarm.handler.FalconAlarmHandler;
import cn.pomit.alarm.handler.GatewayAlarmHandler;
import cn.pomit.alarm.handler.UserInfoTestHandler;
import cn.pomit.consul.ConsulProxyApplication;
import cn.pomit.consul.annotation.EnableMybatis;
import cn.pomit.consul.annotation.EnableServer;

@EnableServer(handler={FalconAlarmHandler.class,GatewayAlarmHandler.class, UserInfoTestHandler.class})
@EnableMybatis(mapperScan = "cn.pomit.alarm.mapper")
public class AlarmApp {
	public static void main(String[] args) {
		ConsulProxyApplication.run(AlarmApp.class, args);
	}

}
```

同时，需要在配置文件中指定mybatis.configuration：

```
mybatis.configuration=cn.pomit.mybatis.configuration.MybatisConfiguration
```

## 结合consul-proxy的Demo项目

## [Gitee-Consul-proxy-test](https://gitee.com/ffch/consul-proxy-test)
## [Github-Consul-proxy-test](https://github.com/ffch/consul-proxy-test)

## Mybatis-proxy的Demo项目

## [Gitee-Mybatis-proxy-test](https://gitee.com/ffch/mybatis-proxy-test)
## [Github-Mybatis-proxy-test](https://github.com/ffch/mybatis-proxy-test)

## 版权声明
mybatis-proxy使用 Apache License 2.0 协议.

## 作者信息
      
   作者博客：https://blog.csdn.net/feiyangtianyao
  
  个人网站：https://www.pomit.cn
 
   作者邮箱： fufeixiaoyu@163.com

## License
Apache License V2

