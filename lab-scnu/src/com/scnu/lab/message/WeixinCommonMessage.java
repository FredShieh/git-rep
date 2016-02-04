package com.scnu.lab.message;

/**
 * 接受用户普通消息
 * @author fred-
 *
 */
public class WeixinCommonMessage extends WeixinMessage {
	private String msgId;
	private String content;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
