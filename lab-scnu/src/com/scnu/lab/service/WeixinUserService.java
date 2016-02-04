package com.scnu.lab.service;

import com.scnu.lab.message.WeixinMessage;

public interface WeixinUserService {
	/**
	 * 用户关注，更新用户，或者新增用户
	 * @param message
	 */
	void addOrUpdateUserToSubscribe(WeixinMessage message);

	/**
	 * 用户取消关注
	 * @param message
	 */
	void updateUserToUnsubscribe(WeixinMessage message);

}
