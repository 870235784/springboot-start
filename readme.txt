1.搭建springboot项目

2.添加插件，将springboot打包成可执行jar包

3.配置application.properties主配置文件

4.添加springboot热部署

5.添加错误页面

6.配置多环境

7.添加jsp支持
	创建web相关目录结构webapp》》WEB-INF》》jsp
	添加jsp访问前缀和后缀
		spring.mvc.view.prefix=/WEB-INF/jsp/
		spring.mvc.view.suffix=.jsp
	添加jar包依赖
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
		</dependency>