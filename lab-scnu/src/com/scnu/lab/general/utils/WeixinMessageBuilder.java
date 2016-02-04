package com.scnu.lab.general.utils;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.scnu.lab.message.WeixinCommonMessage;
import com.scnu.lab.message.WeixinEventMessage;
import com.scnu.lab.message.WeixinMessage;
import com.scnu.lab.message.WeixinSendMessage;

public class WeixinMessageBuilder {
	private static Logger logger=Logger.getLogger(WeixinMessageBuilder.class);
	
	/**
	 * 构造接受消息
	 * @param request
	 * @return
	 */
	public static WeixinMessage buildReceiveMessage(HttpServletRequest request){
		SAXReader reader=new SAXReader();
		WeixinEventMessage eventMessage=null;
		WeixinCommonMessage commonMessage=null;
		try{
			Document doc=reader.read(request.getInputStream());
			Element root=doc.getRootElement();
			logger.info("收到信息！内容如下：\r\n"+doc.asXML());
			if(root.elementText("MsgType").equals("event")){
				eventMessage=new WeixinEventMessage();
				eventMessage.setFromUserName(root.elementText("FromUserName"));
				eventMessage.setToUserName(root.elementText("ToUserName"));
				eventMessage.setCreateTime(root.elementText("CreateTime"));
				eventMessage.setMsgType(root.elementText("MsgType"));
				eventMessage.setEvent(root.elementText("Event"));
				return eventMessage;
			}else{
				logger.info("commonMessage!");
				commonMessage=new WeixinCommonMessage();
				commonMessage.setFromUserName(root.elementText("FromUserName"));
				commonMessage.setToUserName(root.elementText("ToUserName"));
				commonMessage.setCreateTime(root.elementText("CreateTime"));
				commonMessage.setMsgType(root.elementText("MsgType"));
				commonMessage.setContent(root.elementText("Content"));
				return commonMessage;
			}
		}catch(Exception e){
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[])throws Exception{
//		File file=new File("C:\\Users\\fred-\\Desktop\\2.txt");
//		SAXReader reader=new SAXReader();
//		Document doc=reader.read(file);
//		System.out.println(doc.asXML());
//		Element root=doc.getRootElement();
//		System.out.println(root.getName());
		WeixinMessage mes=new WeixinCommonMessage();
		WeixinCommonMessage mesc=(WeixinCommonMessage) mes;
		
	}

	public static WeixinSendMessage buildSendMessage(WeixinMessage message,String value) {
		logger.info("building message!");
		WeixinSendMessage sendMessage=new WeixinSendMessage();
		sendMessage.setCreateTime(String.valueOf(new Date().getTime()));
		sendMessage.setFromUserName(message.getToUserName());
		sendMessage.setToUserName(message.getFromUserName());
		sendMessage.setContent(value);
		sendMessage.setMsgType("text");
		return sendMessage;
	}

	public static WeixinEventMessage buildEventMessage(WeixinMessage message) {
		WeixinEventMessage eventMessage=new WeixinEventMessage();
		eventMessage.setCreateTime(message.getCreateTime());
		
		return null;
	}
}
