package com.scnu.lab.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.scnu.lab.model.WeixinInfo;


@Repository
public class WeixinInfoDAO extends BaseDAOHibernate4{
	private Logger logger=Logger.getLogger(WeixinInfoDAO.class);
	@Override
	protected Class<WeixinInfo> getModelClass() {
		return WeixinInfo.class;
	}

	public WeixinInfo getFirstWeixinInfo(){
		WeixinInfo info=new WeixinInfo();
		return (WeixinInfo) super.serach(info, null, 0, 1).get(0);
	}
	
}
