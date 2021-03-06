1.搭建springboot项目
	1.1 pom文件中添加父项目
		<parent>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-starter-parent</artifactId>
    		<version>1.4.0.RELEASE</version>
    	</parent>
    1.2 添加springboot相关依赖
	    <dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-web</artifactId>
	    </dependency>
	1.3 添加启动类
		注意：启动类所在包及其子包里标上注解@Controller @Service @Component @Repository都会被扫描

2.添加插件，将springboot打包成可执行jar包
	2.1 添加插件
		<plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>

3.配置application.properties主配置文件
	3.1 主配置文件必须以application.properties为文件名, 且必须放在src/main/resources类路径下

4.添加springboot热部署
	4.1 添加依赖
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
		</dependency>
	4.2 需要将project -> build automatically打开
		
5.添加错误页面
	5.1 在src/main/resources类路径下创建目录结构 public》》error》》 ***.html (***与错误码匹配,如404.html)

6.配置多环境
	方案一：打包成多环境
		6.1 在src/main/resources类路径下创建对应环境的文件, 必须以application-***.properties为文件名
		6.2 在主配文件application.properties中添加打包时选择的环境模板
			pring.profiles.active=***
	方案二：启动时多环境
		6.1 在src/main/resources类路径下创建对应环境的文件, 必须以application-***.properties为文件名
		6.2 打包
		6.3 选择运行时配置文件启动
			java -jar ***.jar --pring.profiles.active=***
			
		
	

7.添加jsp支持
	7.1 创建web相关目录结构webapp》》WEB-INF》》jsp
	7.2 添加jsp访问前缀和后缀
		spring.mvc.view.prefix=/WEB-INF/jsp/
		spring.mvc.view.suffix=.jsp
	7.3 添加jar包依赖
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
		</dependency>
		
8.添加mybatis整合
	8.1 添加mybatis相关jar包依赖
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.0</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>1.1.13</version>
		</dependency>
	8.2 添加Dao接口，添加@Mapper注解
	8.3 添加数据源配置，添加mapper.xml映射文件扫描
		#  配置数据源
		spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
		spring.datasource.driver-class-name=com.mysql.jdbc.Driver
		spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test
		spring.datasource.username=root
		spring.datasource.password=admin
		# 映射文件路径
		mybatis.mapper-locations: classpath:mapper-config/*.xml
		
9.事务支持
	9.1 在启动类上添加@EnableTransactionManagement注解
	9.2 在方法上添加@Transactional注解
	
10.添加logback日志
	10.1 在src/main/resources目录下添加日志配置文件logback.xml
	10.2 在application.properties主配置文件中添加引入日志文件配置
		logging.config=classpath:logback.xml
		
11.@Configuration和@Bean
	11.1 在类上加上@Configuration标签, 则该类就等同于spring.xml文件中的<beans>, 即当前类是一个Spring容器
	11.2 在容器类(@Configuration)中, 方法上添加@Bean标签
		11.2.1 当前方法的返回值会注册在当前容器中, 类似于spring.xml文件中的<bean>标签
		11.2.2 返回的类必须被@ComponentScan扫描
		
12.添加单元测试
	12.1 添加相关jar包依赖
	    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    12.2 在src/test/java路径下添加测试基础类BaseTest, 并添加标签
    	@RunWith(SpringRunner.class)
		@SpringBootTest(classes = SpringApplicationBoot.class)
	12.3 单元测试类只需要继承BaseTest即可
	
13.添加redis整合
	13.1 添加相关jar包依赖
		<dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-data-redis</artifactId>  
        </dependency>
    13.2 添加redis.properties配置文件
    13.3 编写Redis实现工具类, 使用@PropertySource读取配置文件, 使用@Component注册
    
14.添加Interceptor拦截器
	14.1 定义拦截器类MyFirstInterceptor继承HandlerInterceptor类, 复写preHandle, postHandle, afterCompletion方法
	14.2 定义springmvc容器, 并注册自定义拦截器
		14.2.1 自定义springmvc容器: 自定义类继承WebMvcConfigurationSupport类, 并加上@Configuration标签
		14.2.2 复写addInterceptors方法, 注册自定义拦截器
		
15.添加Servlet整合
	方式一: (推荐使用)
		15.1 自定义Servlet继承HttpServlet, 复写doGet和doPost方法, 添加@WebServlet注解
		15.2 启动类上添加@ServletComponentScan扫描Servlet所在包
	方式二:
		配置方式, 较繁琐
		
	
	
