package com.tca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tca.beans.Student;
import com.tca.dao.IStudentDao;


@Controller
public class RegisterController {
	
	@Autowired
	private IStudentDao studentDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
	
	/**
	 * 跳转到注册页面
	 * @return
	 */
	@RequestMapping("/register")
	public String toRegisterPage(){
		LOGGER.info("转入登陆界面");
		return "register";
	}
	
	/**
	 * 注册
	 * @param student
	 * @return
	 */
	@RequestMapping("/doRegister")
	public ModelAndView doRegister(Student student){
		ModelAndView mv = new ModelAndView();
		
		//判断用户名是否使用
		String sname = student.getSname();
		if (studentDao.countBySname(sname) > 0){
			LOGGER.info("该用户名已被使用");
			mv.addObject("errorMsg", "该用户名已使用");
			mv.setViewName("register");
			return mv;
		}
		LOGGER.info("该用户名未被使用");
		
		//注册成功，则跳转到注册成功页面
		studentDao.insertStudent(student);
		mv.addObject("student", student);
		mv.setViewName("welcome");
		LOGGER.info("注册成功");
		return mv;
	}
}
