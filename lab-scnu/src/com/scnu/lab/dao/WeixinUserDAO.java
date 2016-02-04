package com.scnu.lab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scnu.lab.model.WeixinUser;

@Repository
public class WeixinUserDAO extends BaseDAOHibernate4{

	@Override
	protected Class<WeixinUser> getModelClass() {
		return WeixinUser.class;
	}

	public WeixinUser findWeixinUserByOpenId(String openId) {
		List<WeixinUser> list=super.findByProperty(getModelClass(), "openId",openId);
		if(list.size()==0 || null==list){
			return null;
		}else{
			return list.get(0);
		}
	}

	public void addWeixinUser(WeixinUser user) {
		super.save(user);
	}

	public void updateWeixinUser(WeixinUser user) {
		super.update(user);
	}

	

}
