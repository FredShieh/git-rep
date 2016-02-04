package com.scnu.lab.general.utils;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.google.gson.JsonObject;
import com.scnu.lab.cache.MemcachedFactory;
import com.scnu.lab.message.WeixinSendMessage;
import com.scnu.lab.model.WeixinInfo;
import com.scnu.lab.model.WeixinUser;
import com.scnu.lab.service.WeixinInfoService;
@Repository
public class WeixinUtil {
	private static Logger logger=Logger.getLogger(WeixinUtil.class);
	@Autowired
	private  WeixinInfoService weixinInfoService;
	//获取用户基本信息，请求url
	private static final String GET_WEIXIN_INFO_URL="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	//
	private static final String GET_ACCESSTOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//accesstoken,memcached保存到key
	private static final String ACCESS_TOKEN_KEY="access_token_key";
	
	public  WeixinUser getWeixinUserFromWeixinServer(String openId,WeixinUser user ){
		if(null==user){
			user=new WeixinUser();
			user.setOpenId(openId);
		}
		BASE64Encoder encoder=new BASE64Encoder();
		String accessToken=getAccessToken();
		logger.info("accessToken:"+accessToken);
		JsonObject userJson=NetUtil.post(GET_WEIXIN_INFO_URL.replaceFirst("ACCESS_TOKEN", accessToken).replaceFirst("OPENID", openId), new JsonObject());
		user.setCity(StringUtils.null2Blank(userJson.get("city").getAsString()));
		user.setCountry(StringUtils.null2Blank(userJson.get("country").getAsString()));
		user.setFollowTime(new Date());
		user.setIsFollow(Integer.valueOf(StringUtils.null2Blank(userJson.get("subscribe"))));
		user.setNickName(encoder.encode(StringUtils.null2Blank(userJson.get("nickname").getAsString()).getBytes()));
		user.setHeadImgUrl(StringUtils.null2Blank(userJson.get("headimgurl").getAsString()));
		user.setProvince(StringUtils.null2Blank(userJson.get("province").getAsString()));
		return user;
	}
	
	
	public  String getAccessToken(){
		String accessToken=(String) MemcachedFactory.get(ACCESS_TOKEN_KEY);
		if(StringUtil.isEmpty(accessToken)){
			getAccessTokenFromWeixinServerAndSave();
			return (String) MemcachedFactory.get(ACCESS_TOKEN_KEY);
		}else{
			return accessToken;
		}
	}


	private  void getAccessTokenFromWeixinServerAndSave() {
		logger.info("过期，从新获取accesstoken");
		WeixinInfo info=null;
		try{
			info=weixinInfoService.getFirstWeixinGzh();
		}catch(Exception e){
			logger.error(e);
		}
		JsonObject jObject=NetUtil.post(GET_ACCESSTOKEN_URL.replaceFirst("APPID",info.getAppId()).replaceFirst("APPSECRET", info.getAppSecret()), new JsonObject());
		String accessToken=jObject.get("access_token").getAsString();
		logger.info("accestoken:"+accessToken);
		MemcachedFactory.set(ACCESS_TOKEN_KEY, accessToken, 7000*1000);
	}


	public static void sendMessage(WeixinSendMessage sendMessage,
			HttpServletResponse response) {
		Document doc=null;
//		Element root=null;
		OutputStreamWriter writer=null;
		try{
			doc=DocumentHelper.createDocument();
			Element root=doc.addElement("xml");
			Element toUserName=root.addElement("ToUserName");
			toUserName.setText(sendMessage.getToUserName());
			Element fromUserName=root.addElement("FromUserName");
			fromUserName.setText(sendMessage.getFromUserName());
			Element createTime=root.addElement("CreateTime");
			createTime.setText(sendMessage.getCreateTime());
			Element msgType=root.addElement("MsgType");
			msgType.setText(sendMessage.getMsgType());
			Element content=root.addElement("Content");
			content.setText(sendMessage.getContent());
			String xmlOut=doc.asXML();
			writer=new OutputStreamWriter(response.getOutputStream());
//			BufferedWriter writer=new BufferedWriter()
			writer.write(xmlOut);
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
		}finally{
			if(null!=writer){
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
				}
			}
		}
		
	}




	
	public static void main(String args[]) throws IOException{
		BASE64Encoder encoder=new BASE64Encoder();
		String a=encoder.encode("XHM".getBytes());
		System.out.println(a);
		BASE64Decoder decoder=new BASE64Decoder();
		byte[] bytes=decoder.decodeBuffer("IkhN8J+QkiI=");
		String b=new String(bytes,Charset.forName("UTF-8"));
		System.out.println(b);
	}
}
