package com.scnu.lab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scnu.lab.dao.SystemTextDAO;
import com.scnu.lab.service.SystemTextService;

@Service
@Transactional
public class SystemTextServiceImpl implements SystemTextService {
	@Autowired
	private SystemTextDAO systemTextDAO;

	@Override
	public String findFirstSystemTextByName(String name) {
		return systemTextDAO.findTextByName(name);
	}

}
