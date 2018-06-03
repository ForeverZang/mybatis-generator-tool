@author Forever.Zang
@dependency 1.mysql-connector-java-5.1.14-bin.jar
			2.mybatis-generator-core-1.3.2.jar
@date 2018/06/03
@version 1.3.2

##################################### 生成数据层dao，service，daoEx配置参数-start  #####################################
## 目标路径配置src/main/java
genarate.sourceRoot=src/main/java

## 数据库配置
jdbc.url=jdbc:mysql://127.0.0.1:3306/db_education?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
jdbc.username=root
jdbc.password=123456

## com.item.dao
genarate.daoPackage=com.eliteams.quick4j.web.dao

## com.item.service
## 此配置可省略
#generate.serviceGenrate=false
#genarate.servicePackage=com.eliteams.quick4j.web.service

## t_
generate.removePrefix=t_
generate.excludeTables=
generate.includeTables=t_focus

## com.item.daoEx
genarate.daoEx.daoPackage=com.eliteams.quick4j.web.dao
genarate.daoEx.beanName=Menu

##################################### 生成数据层dao，service，daoEx配置参数-end  ######################################




##################################### 生成数据层dao，service，daoEx使用说明-start  ####################################
public static void main(String[] args) {
	String path = TestMain.class.getResource("/").getPath();
	System.out.println("=======path:" + path);
	// 生成dao
	GenerateTool.generateDao("generator.properties", path);
	// 生成daoEx
	GenerateTool.generateDaoEx("generator.properties", path);
}
##################################### 生成数据层dao，service，daoEx使用说明-start  ####################################