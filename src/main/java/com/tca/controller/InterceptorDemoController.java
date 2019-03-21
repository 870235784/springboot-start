package com.tca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterceptorDemoController {
	
	@GetMapping("/first/one")
	public String first() {
		return "first";
	}
	
	@GetMapping("/second/two")
	public String second() {
		return "second";
	}
}
