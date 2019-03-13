package com.tca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {
	
	@RequestMapping("/")
	public String hello() {
		return "hello springboot";
	}
	
	@RequestMapping("/hot")
	public String hotDeployment() {
		return "hot deployment";
	}
	
}