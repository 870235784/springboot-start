package com.tca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tca.beans.BeanTest;

@Configuration
public class MainConfig {
	
	public MainConfig() {
		System.err.println("ConfigurationTest initialize ...");
	}
	
	@Bean
	public BeanTest initBeanTest() {
		return new BeanTest("test", "test");
	}
	
}
