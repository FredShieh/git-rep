package com.scnu.lab.controllers.weixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scnu.lab.cache.MemcachedFactory;
import com.scnu.lab.constant.SystemTextConstant;
import com.scnu.lab.controllers.base.BaseController;
import com.scnu.lab.general.utils.MyDigest;
import com.scnu.lab.general.utils.StringUtil;
import com.scnu.lab.general.utils.StringUtils;
import com.scnu.lab.general.utils.WeixinMessageBuilder;
import com.scnu.lab.general.utils.WeixinUtil;
import com.scnu.lab.message.WeixinEventMessage;
import com.scnu.lab.message.WeixinMessage;
import com.scnu.lab.message.WeixinSendMessage;
import com.scnu.lab.model.WeixinInfo;
import com.scnu.lab.service.SystemTextService;
import com.scnu.lab.service.WeixinInfoService;
import com.scnu.lab.service.WeixinUserService;

@Controller
public class WeixinController extends BaseController {
	//日志类
	Logger logger=Logger.getLogger(getClass());
	
	@Autowired
	private WeixinInfoService weixinInfoService;
	@Autowired
	private WeixinUserService weixinUserService;
	@Autowired
	private SystemTextService systemTextService;
	/**
	 * 验证微信信息
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param request
	 * @param response
	 * @author xiehm
	 */
	@RequestMapping("/wx/receive")
	public void verifyFromWeixin(String signature,String timestamp,String nonce,String echostr,HttpServletRequest request,HttpServletResponse response){
		//获得公众号信息
		WeixinInfo weixinInfo=weixinInfoService.getFirstWeixinGzh();
		//判断是否为首次接入的验证信息
		if(!StringUtil.isEmpty(signature) && !StringUtil.isEmpty(echostr)){
			List<String> strList=new ArrayList<String>();
			strList.add(timestamp); 
			strList.add(nonce);
			strList.add(weixinInfo.getToken());
			//字典排序
			Collections.sort(strList);
			String total="";
			for(int i=0;i<strList.size();i++){
				total=total+strList.get(i)+"";
			}
			String mySignature=MyDigest.SHA1(total);
			if(mySignature.equals(signature)){
				responseWriter(echostr, response);
			}else{
				responseWriter(echostr+"false", response);
			}
		}else{
			//接受用户消息
			WeixinMessage message=WeixinMessageBuilder.buildReceiveMessage(request);
			WeixinSendMessage sendMessage=WeixinMessageBuilder.buildSendMessage(message, "");
			logger.info("openId:"+sendMessage.getToUserName());
			//排重
			long lastTime=0;
			String cacheStr=StringUtils.null2Blank(MemcachedFactory.get(message.getFromUserName()));
			lastTime=StringUtil.isEmpty(cacheStr)?0:Long.valueOf(cacheStr);
			long thisTime=Long.valueOf(message.getCreateTime());
			if(thisTime==lastTime ){
				WeixinUtil.sendMessage(sendMessage, response);
				return;
			}
			MemcachedFactory.set(message.getFromUserName(), thisTime);
			if(message.getMsgType().equals("event")){
				WeixinEventMessage eventMessage=null;
				try{
					eventMessage=(WeixinEventMessage) message;
				}catch(Exception e){
					logger.error(e.toString());
				}
				if(eventMessage.getEvent().equals("subscribe")){
					sendMessage=WeixinMessageBuilder.buildSendMessage(message,systemTextService.findFirstSystemTextByName(SystemTextConstant.WELCOME_INFO));
					logger.info("openId为："+message.getFromUserName()+"关注公众号！"); 
					try{
						weixinUserService.addOrUpdateUserToSubscribe(message);
					}catch(Exception e){
						logger.error(e.toString());
					}
				}else if (eventMessage.getEvent().equals("unsubscribe")){
					logger.info("openId为："+message.getFromUserName()+"取消关注公众号！"); 
					weixinUserService.updateUserToUnsubscribe(message);
				}else{
					sendMessage=WeixinMessageBuilder.buildSendMessage(message, systemTextService.findFirstSystemTextByName(SystemTextConstant.DEFAULT_REPLY));
				}
			}else{
				logger.info("该消息为一般消息");
				sendMessage=WeixinMessageBuilder.buildSendMessage(message, systemTextService.findFirstSystemTextByName(SystemTextConstant.DEFAULT_REPLY));
				
			}
			logger.info("发送至"+sendMessage.getToUserName()+"消息内容："+sendMessage.getContent());
			WeixinUtil.sendMessage(sendMessage,response);
			
		}
	}

}
