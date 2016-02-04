package com.scnu.lab.model;

/**
 * 微信公众号appid和appsecret信息
 * @author fred-
 *
 */
public class WeixinInfo {
	private Integer id;
	private String appId;
	private String appSecret;
	private String token;
	public WeixinInfo(){
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
