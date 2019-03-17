package com.tca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class RegisterController {
	
	/**
	 * 跳转到注册页面
	 * @return
	 */
	@RequestMapping("/register")
	public String toRegisterPage(){
		return "register";
	}
	
	/**
	 * 注册
	 * @param student
	 * @return
	 */
	/*@RequestMapping("/doRegister")
	public ModelAndView doRegister(Student student){

	}*/

}
