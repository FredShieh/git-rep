package com.scnu.lab.general.utils;


import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class NetUtil {

	public static JsonObject post(String url,JsonObject json){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JsonObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");
			post.setEntity(s);
			 
			HttpResponse res = client.execute(post);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity entity = res.getEntity();
				response =new JsonParser().parse(EntityUtils.toString(entity, "utf-8")).getAsJsonObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
			return response;
		}
		
}
