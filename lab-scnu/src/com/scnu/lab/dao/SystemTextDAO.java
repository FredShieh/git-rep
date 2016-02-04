package com.scnu.lab.dao;

import org.springframework.stereotype.Repository;

import com.scnu.lab.model.SystemText;

@Repository
public class SystemTextDAO extends BaseDAOHibernate4{

	

	@Override
	protected Class<SystemText> getModelClass() {
		// TODO Auto-generated method stub
		return SystemText.class;
	}

	public String findTextByName(String name) {
		SystemText text=(SystemText) super.findByProperty(getModelClass(), "name", name).get(0);
		return text.getValue();
	}

}
