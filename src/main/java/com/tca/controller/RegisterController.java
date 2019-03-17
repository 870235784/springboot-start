package com.tca.controller;

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
	@RequestMapping("/doRegister")
	public ModelAndView doRegister(Student student){
		ModelAndView mv = new ModelAndView();
		
		//判断用户名是否使用
		String sname = student.getSname();
		if (studentDao.countBySname(sname) > 0){
			mv.addObject("errorMsg", "该用户名已使用");
			mv.setViewName("register");
			return mv;
		}
		
		//注册成功，则跳转到注册成功页面
		studentDao.insertStudent(student);
		mv.addObject("student", student);
		mv.setViewName("welcome");
		return mv;
	}
}
