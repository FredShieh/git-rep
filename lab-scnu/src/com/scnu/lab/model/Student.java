package com.scnu.lab.model;

import java.util.Date;
/**
 * 教务网获取的学生信息
 * @author fred-
 *
 */
public class Student {
	private Integer id;
	private Integer uid;
	private String name;
	private String studentNumber;
	private String major;
	private String academy;
	private Integer grade;
	private Date enrollmentTime;
	private String mobile ;
	
	public Student(){
		
	}
	
	public Date getEnrollmentTime() {
		return enrollmentTime;
	}

	public void setEnrollmentTime(Date enrollmentTime) {
		this.enrollmentTime = enrollmentTime;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getAcademy() {
		return academy;
	}
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Date getEnrollment_time() {
		return enrollmentTime;
	}
	public void setEnrollment_time(Date enrollment_time) {
		this.enrollmentTime = enrollment_time;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	
	
}
