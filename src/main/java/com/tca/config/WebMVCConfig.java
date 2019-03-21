package com.tca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.tca.interceptor.MyFirstInterceptor;

/**
 * 相当于springmvc.xml文件 
 * 	1.继承WebMvcConfigurationSupport
 * 	2.添加@Configuration标签
 * @author zhouan
 *
 */
@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport{
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		MyFirstInterceptor myFirstInterceptor = new MyFirstInterceptor(); 
		registry.addInterceptor(myFirstInterceptor)
				.addPathPatterns("/first/**") //拦截/first开始的请求
				.excludePathPatterns("/second/**"); //不拦截/second开始的请求
	}
}
