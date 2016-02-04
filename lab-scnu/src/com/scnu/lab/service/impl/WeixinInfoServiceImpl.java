package com.scnu.lab.service.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scnu.lab.dao.WeixinInfoDAO;
import com.scnu.lab.model.WeixinInfo;
import com.scnu.lab.service.WeixinInfoService;

@Transactional
@Service
public class WeixinInfoServiceImpl implements WeixinInfoService {
	private Logger logger=Logger.getLogger(WeixinInfoServiceImpl.class);
	@Autowired
	private WeixinInfoDAO weixinInfoDAO;
	
	@Override
	public WeixinInfo getFirstWeixinGzh() {
		logger.info("into servcie impl");
		return weixinInfoDAO.getFirstWeixinInfo();
	}

	

}
