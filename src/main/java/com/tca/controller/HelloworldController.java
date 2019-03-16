package com.tca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tca.service.IProfileService;

@RestController
public class HelloworldController {
	
	@Autowired
	private IProfileService profileService;
	
	@RequestMapping("/")
	public String hello() {
		return "hello springboot world";
	}
	
	@RequestMapping("/hot")
	public String hotDeployment() {
		return "hot deployment";
	}
	
	@RequestMapping("/profile")
	public String profile() {
		return profileService.profileName();
	}
	
}
