package com.tca.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:properties-config/jdbc.properties")
public class JDBCConfigurationController {

	@Value("${jdbc.driver}")
	private String driver;
	
	@Value("${jdbc.url}")
	private String url;
	
	@RequestMapping("/driver")
	public String driver() {
		return driver;
	}
	
	@RequestMapping("/url")
	public String url() {
		return url;
	}
	
}
