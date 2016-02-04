package com.scnu.lab.controllers.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scnu.lab.general.utils.ConfigUtil;
import com.scnu.lab.model.WeixinInfo;
import com.scnu.lab.service.WeixinInfoService;


@Controller
public class TestController extends BaseController {

	@Autowired
	private WeixinInfoService weixinInfoService;
	
	@RequestMapping("/test/1")
	public String test(HttpServletRequest request,HttpServletResponse response){
		try{
			String a=ConfigUtil.readPropertyValue("test");
			request.setAttribute("testStr", a);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/test/1";
	}

}
