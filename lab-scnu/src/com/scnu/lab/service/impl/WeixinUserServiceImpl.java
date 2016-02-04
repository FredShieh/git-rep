package com.scnu.lab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scnu.lab.dao.WeixinUserDAO;
import com.scnu.lab.general.utils.WeixinUtil;
import com.scnu.lab.message.WeixinMessage;
import com.scnu.lab.model.WeixinUser;
import com.scnu.lab.service.WeixinUserService;

@Transactional
@Service
public class WeixinUserServiceImpl implements WeixinUserService {
	@Autowired
	private WeixinUserDAO weixinUserDAO;
	@Autowired
	private WeixinUtil weixinUtil;
	@Override
	public void addOrUpdateUserToSubscribe(WeixinMessage message) {
		String openId=message.getFromUserName();
		WeixinUser user=weixinUserDAO.findWeixinUserByOpenId(openId);
		if(null==user){
			user=weixinUtil.getWeixinUserFromWeixinServer(openId, null);
			weixinUserDAO.addWeixinUser(user);
		}else{
			user=weixinUtil.getWeixinUserFromWeixinServer(openId, user);
			weixinUserDAO.updateWeixinUser(user);
		}
	}

	
	@Override
	public void updateUserToUnsubscribe(WeixinMessage message) {
		String openId=message.getFromUserName();
		WeixinUser user=weixinUserDAO.findWeixinUserByOpenId(openId);
		if(null==user){
			return;
		}
		user.setIsFollow(0);
		weixinUserDAO.updateWeixinUser(user);
		
	}

	

}
