### 简介
+ Super-Jacoco是基于Jacoco、git二次开发打造的一站式JAVA代码全量/diff覆盖率收集平台，能够低成本、无侵入的收集代码覆盖率数据。Super-Jacoco除了支持JVM运行时间段的覆盖率收集外；还能够和环境无缝对接，收集服务端自定义时间段代码全量/增量覆盖率。并提供可视化的html覆盖率报表，协助覆盖情况精准分析，支撑精准测试落地。

### 产品特性：
+ 通用：既支持单元测试覆盖率收集，也支持手工测试覆盖率收集；既支持全量覆盖率收集，也支持diff覆盖率收集；
+ 无侵入：采用on-the-fly模式，无需对开发代码做任何改造，即可收集覆盖率数据；
+ 高可用：分布式架构，任务机可无限扩展，避免任务机down机或者任务过多时出现性能瓶颈；
+ 可视化：提供html格式的覆盖率报告，可读性高。

### 使用方法
#### 1、数据库安装和初始化
	安装mysql数据库，创建数据库后执行sql/db.sql文件中的建表SQL
#### 2、编译打包
	 2.1 安装JDK1.8、mavan3
	 2.2 clone代码，更改application.properties文件中的数据库和gitlab配置：
		 spring.datasource.url=jdbc:mysql://IP:端口/数据库名?useUnicode=true&amp;characterEncoding=utf8
		 spring.datasource.username=
		 pring.datasource.password=
		 gitlab.username=
		 gitlab.password=
	 2.3 执行mvn package -Dmaven.test.skip=true生成super-jacoco.jar
#### 3、部署
	 3.1 执行“nohup java -jar super-jacoco.jar &”启动代码覆盖率服务，默认端口为8899
#### 4、覆盖率收集接口
##### 4.1 单测覆盖率接口
###### 1、启动覆盖率收集
	 URL：/cov/triggerUnitCover
	 调用方法：POST
	 参数（body方式传入）：{"uuid":"uuid","type":1,"gitUrl":"git@git","subModule":"","baseVersion":"master","nowVersion":"feature","envType":"-Ptest"}
	 返回：{"code":200,"data":true,"msg":"msg"}
	 备注：
###### 2、获取覆盖率结果
	 URL：/cov/getUnitCoverResult
	 调用方法：GET
	 参数：uuid(String)
	 返回：{"code":200,"data":{"coverStatus":1,"errMsg":"msg","lineCoverage":100.0,"branchCoverage":100.0,"logFile":"file content","reportUrl":"http://"},"msg":"msg"}
	 备注：
##### 4.2 环境覆盖率接口
###### 1、启动覆盖率收集
	 URL：/cov/triggerEnvCov
	 调用方法：POST
	 参数（body方式传入）：{"uuid":"uuid","type":1,"gitUrl":"git@git","subModule":"","baseVersion":"master","nowVersion":"feature"，"address":"127.0.0.1","port":"8088"}
	 返回：{"code":200,"data":true,"msg":"msg"}
	 备注：IP和port为模块部署服务器的IP和端口，在dump jacoco.exec时使用，需要提前把org.jacoco.agent-0.8.5-runtime.jar包拷贝到服务器:/home/xxx/目录，服务启动时需要添加启动参数： -javaagent:/home/xxx/org.jacoco.agent-0.8.5-runtime.jar=includes=*,output=tcpserver,address=*,port=18513
###### 2、获取覆盖率结果
	 URL：/cov/getEnvCoverResult
	 调用方法：GET
	 参数：uuid(String)
	 返回：{"code":200,"data":{"coverStatus":1,"errMsg":"msg","lineCoverage":100.0,"branchCoverage":100.0,"logFile":"file content","reportUrl":"http://"},"msg":"msg"}
	 备注：

### 联系我们
#### 微信群：super-jacoco技术支持群(添加管理员二维码邀请进群)
![image](https://dpubstatic.udache.com/static/dpubimg/31985204-6e7f-47ed-bd73-ec69b8ef63bf.png)
