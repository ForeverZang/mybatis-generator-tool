MyBatis代码自动生成工具，可以jar包形式集成到项目中，一键生成！
-

######版本简介
>依赖
>>1.mysql-connector-java-5.1.14-bin.jar<br>
>>2.mybatis-generator-core-1.3.2.jar<br>

>时间 2018/06/03<br>
>版本 1.3.2<br>

#####1.生成数据层dao，service，daoEx配置参数（generator.properties文件配置内容）

#######目标路径配置(例：src/main/java)
	genarate.sourceRoot=src/main/java

#######数据库配置
	jdbc.url=jdbc:mysql://127.0.0.1:3306/db_test?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull<br>
	jdbc.username=root<br>
	jdbc.password=123456

#######com.item.dao
	genarate.daoPackage=com.eliteams.quick4j.web.dao

#######com.item.service(此配置可省略)
	generate.serviceGenrate=false<br>
	genarate.servicePackage=com.eliteams.quick4j.web.service

#######t_(去除前缀)
	generate.removePrefix=t_<br>
	generate.excludeTables=<br>
	generate.includeTables=t_focus

#######com.item.daoEx
	genarate.daoEx.daoPackage=com.eliteams.quick4j.web.dao<br>
	genarate.daoEx.beanName=Menu





#####2.生成数据层dao，service，daoEx使用说明
```java
// 调用代码示例
public static void main(String[] args) {
	String path = TestMain.class.getResource("/").getPath();
	System.out.println("=======path:" + path);
	// 生成dao
	GenerateTool.generateDao("generator.properties", path);
	// 生成daoEx
	GenerateTool.generateDaoEx("generator.properties", path);
}
```
#####3.打包集成使用说明
>1.cd 到build.bat脚本的磁盘目录，并双击执行<br>
>2.成功后，cd 到target目录下，即可得到mybatis-generator-tool-1.3.2.jar<br>
>3.集成到项目中,使用示例代码即可生成所需代码<br>
