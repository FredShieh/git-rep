package com.scnu.lab.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scnu.lab.model.Student;
import com.scnu.lab.service.StudentService;

@Transactional
@Service
public class StudentServiceImpl implements StudentService {

	@Override
	public Student getFromFangzheng(String studentNumber, String password,
			String authCode) {
		// TODO 冬超做这个
		return null;
	}

	@Override
	public void addStudent(Student student) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
