## Go享生活 

Go享生活 购物系统采用的开发模式是前后端分离的开发模式。



### Mall后端



###### 模块说明：

Mall： Go享生活 项目的父工程，进行项目依赖的统一管理，打包方式为pom

common:  Go享生活 项目公共模块的管理模块，父工程为Mall

common-util:    工具类模块，父工程为common

common-service：公共服务模块，父工程为common

model:  Go享生活 实体类模块

manager： Go享生活 项目后台管理系统的后端服务



###### 一个项目中所涉及到的实体类往往有三种：

1、封装请求参数的实体类：这种实体类在定义的时候往往会携带到dto【数据传输对象：Data Transfer Object】字样，会定义在dto包中

2、与数据库对应的实体类：这种实体类往往和数据表名称保证一致，会定义在domain、entity、pojo包中

3、封装响应结果的实体类：这种实体类在定义的时候往往会携带到vo【视图对象：View Object】字样，会定义在vo包中



模块依赖说明：

common-service会依赖：common-util、common

manager会依赖：common-service



本次项目开发的时候所使用的软件环境版本如下所示：

| 软件名称     | 版本说明 |
| ------------ | -------- |
| jdk          | jdk17    |
| spring boot  | 3.0.5    |
| spring cloud | 2022.0.2 |
| redis        | 7.0.10   |
| mysql        | 8.0.30   |
| idea         | 2023.2   |
|              |          |





### mall-admin前端

###### 部署操作步骤

```shell
# Vue3-Element-Admin 要求 Node.js 版本 >= 12 ，推荐Node.js  16.x版本

# 使用git克隆项目 或者 直接下载项目
git clone https://github.com/huzhushan/vue3-element-admin.git

# 进入项目目录
cd vue3-element-admin

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm start
```

修改页面项目bug：

```js
# 将permission.js中的相关代码
# 原代码：import { TOKEN } from '@/store/modules/app' // TOKEN变量名
# 更改为如下代码：
import { TOKEN } from '@/pinia/modules/app' // TOKEN变量名
```

启动

```shell
npm start
```

部署好的前端工程的核心目录结构如下所示：

```java
mock					// 用于测试，模拟后端接口地址
public					// 存储公共的静态资源：图片
src						// 源代码目录，非常重要
    | api				// 提供用于请求后端接口的js文件
    | assets			// 存储静态资源：图片、css
    | components		// 存储公共组件,可重用的一些组件
    | directive			// 存储自定义的一些指令
    | hooks				// 存储自定义的钩子函数
    | i18n				// 存储用于国际化的js文件
    | layout			// 存储首页布局组件
    | pinia				// 用于进行全局状态管理
    | router			// 存储用于进行路由的js文件
    | utils				// 存储工具类的js文件
    | views				// 和路由绑定的组件
    | App.vue			// 根组件
    | default-settings.js // 默认设置的js文件
    | error-log.js		// 错误日志js文件
    | global-components.js // 全局组件的js文件
    | main.js			// 入口js文件(非常重要)
    | permission.js		// 权限相关的js文件(路由前置守卫、路由后置守卫)
vite.config.js			// vite的配置文件，可以在该配置文件中配置前端工程的端口号
```





### 数据库准备

本地安装mysql数据库使用的是docker安装，对应的步骤如下所示：

###### 部署mysql

开发阶段也可以连接本地mysql服务

```shell
# 拉取镜像
docker pull mysql:8.0.30

# 创建容器
docker run -d --name mysql -p 3306:3306 -v mysql_data:/var/lib/mysql -v mysql_conf:/etc/mysql --restart=always --privileged=true -e MYSQL_ROOT_PASSWORD=1234 mysql:8.0.30
```

docker安装完成mysql8，如果使用sqlyog或者navite连接，需要修改密码加密规则，因为低版本客户端工具不支持mysql8最新的加密规则。如果使用客户端连接，需要修改：

* docker exec 进入mysql容器

* mysql -uroot -p 登录你的 MySQL 数据库，然后 执行这条SQL：

```sql
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '1234';
```

然后再重新配置SQLyog的连接，重新填写密码，则可连接成功了。



### Redis准备

###### 部署Redis

使用docker部署Redis，具体的操作如下所示：

开发阶段也可以连接本地redis服务

```shell
#1 拉取镜像
docker pull redis:7.0.10

#2 在宿主机的 /var/lib/docker/volumes/redis-config/_data/目录下创建一个redis的配置文件，
vim redis.conf
# 内容如下所示
#开启持久化
appendonly yes
port 6379
# requirepass 1234
bind 0.0.0.0

#3 如果/var/lib/docker/volumes没有redis-config，创建数据卷 
docker volume create redis-config

#4 创建容器
docker run -d -p 6379:6379 --restart=always \
-v redis-config:/etc/redis/config \
-v redis-data:/data \
--name redis redis \
redis-server /etc/redis/config/redis.conf
```

```shell
#进入到redis
docker exec -it redis redis-cli

#清除
flushdb

#查看存储
keys *
```





### Minio准备

官网地址：https://www.minio.org.cn/docs/cn/minio/container/index.html

###### Windows安装

1、下载Minio安装文件

* 地址：https://dl.min.io/server/minio/release/

2、进入到Monio所在目录，启动Minio

* 启动命令： minio.exe  server  数据文件目录(数据文件地址)
* 访问控制台：localhost:9000
* 创建bucket并设置minio的中该桶的访问权限为public



###### Linux安装

具体命令：

```java
//拉取镜像
docker pull quay.io/minio/minio

// 创建数据存储目录
mkdir -p ~/minio/data

// 创建minio
docker run \
   -p 9001:9000 \
   -p 9090:9090 \
   --name minio \
   -v ~/minio/data:/data \
   -e "MINIO_ROOT_USER=admin" \
   -e "MINIO_ROOT_PASSWORD=admin123456" \
   -d \
   quay.io/minio/minio server /data --console-address ":9090"
```



###### pom.xml添加依赖

```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.2</version>
</dependency>
```





### Nacos启动

```
nacos bin>startup.cmd -m standalone
http://localhost:8848/nacos                                                                                                                                                                              

```

