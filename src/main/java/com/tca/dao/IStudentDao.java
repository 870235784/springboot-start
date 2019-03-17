package com.tca.dao;

import org.apache.ibatis.annotations.Mapper;

import com.tca.beans.Student;

@Mapper
public interface IStudentDao {
	
	/**
	 * 
	 * @param sname
	 * @return
	 */
	int countBySname(String sname);
	
	/**
	 * 
	 * @param student
	 */
	void insertStudent(Student student);
	
	
}
