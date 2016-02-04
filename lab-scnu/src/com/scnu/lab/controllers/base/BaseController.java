package com.scnu.lab.controllers.base;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;




/**
 * controller父类，放置通用的属性和处理方法，供普通controller调用
 * 
 * @author shanw_000
 *
 */
public class BaseController {
    protected Logger logger = Logger.getLogger(getClass());

	
	/**
	 * 向response写入结果值
	 * 
	 * @param result 结果
	 * @param response
	 * @throws Exception
	 */
	protected void responseWriter(boolean result, HttpServletResponse response) {
		try{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(String.valueOf(String.valueOf(result)));
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	protected void responseWriter(String result,HttpServletResponse response){
		try{
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(result);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected static Map<String, String> getParameterMap(HttpServletRequest request) {  
	    // 参数Map  
	    Map<?, ?> properties = request.getParameterMap();  
	    // 返回值Map  
	    Map<String, String> returnMap = new HashMap<String, String>();  
	    Iterator<?> entries = properties.entrySet().iterator();  
	    Map.Entry entry;  
	    String name = "";  
	     
	    while (entries.hasNext()) {  
	    	String value = ""; 
	        entry = (Map.Entry) entries.next();  
	        name = (String) entry.getKey();  
	        Object valueObj = entry.getValue();  
	        if(null == valueObj){  
	            value = "";  
	        }else if(valueObj instanceof String[]){  
	            String[] values = (String[])valueObj;  
	            for(int i=0;i<values.length;i++){  
	                value = value + values[i] + ",";  
	            }  
	            value = value.substring(0, value.length()-1);  
	        }else{  
	            value = valueObj.toString();  
	        }  
	        returnMap.put(name, value);  
	    }  
	    return returnMap;  
	}
	
	
	
	/**
	 * 获取用户openId
	 * @param request
	 * @param response
	 * @return
	 */
	protected String getOpenId(HttpServletRequest request, HttpServletResponse response) {
		return "";
	}
}
