package com.tca.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tca.beans.BeanTest;

@Configuration
public class ConfigurationTest {
	
	public ConfigurationTest() {
		System.err.println("ConfigurationTest initialize ...");
	}
	
	@Bean
	public BeanTest initBeanTest() {
		return new BeanTest("test", "test");
	}

}
