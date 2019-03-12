package com.tca.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@Controller
public class HelloworldController {
	
	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		return "hello springboot";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HelloworldController.class, args);
	}
}
