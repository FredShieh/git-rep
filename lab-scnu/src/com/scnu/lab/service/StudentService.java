package com.scnu.lab.service;

import com.scnu.lab.model.Student;

public interface StudentService {
	/**
	 * 爬出学生信息
	 * @param studentNumber 学生账号
	 * @param password 学生密码
	 * @param authCode 登录的验证码
	 * @return
	 */
	public Student getFromFangzheng(String studentNumber,String password,String authCode);
	
	/**
	 * 增加学生到数据库
	 * @param student
	 */
	public void addStudent(Student student);
}
