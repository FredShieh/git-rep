package com.scnu.lab.dao;

import org.springframework.stereotype.Repository;

import com.scnu.lab.model.Student;

@Repository
public class StudentDAO extends BaseDAOHibernate4 {

	@Override
	protected Class<Student> getModelClass() {
		return Student.class;
	}

	

}
