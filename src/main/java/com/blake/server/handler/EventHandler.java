package com.blake.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.blake.server.response.UnifiedResponse;
import com.blake.storage.hadoop.HadoopServerImpl;

public class EventHandler {

	Logger logger = Logger.getLogger(EventHandler.class);
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	
	public EventHandler(HttpServletRequest request, HttpServletResponse response) {

		this.request = request;
		this.response = response;
	}

	public void handleEvent() {

		
		BufferedReader reader = null;
		String line = null;
		StringBuffer buffer = new StringBuffer();
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			while((line = reader.readLine()) != null) {
				
				buffer.append(line);
			}
			
		} catch (IOException e) {

			logger.error("error when reading transferring data " + e);
			UnifiedResponse.sendErrResponse(response, 5001);
			return;
		}
		
		JSONObject jo = null;
		try{
			
			jo =   JSONObject.fromObject(URLDecoder.decode(buffer.toString(), HTTP.UTF_8));
			
		} catch(Exception e) {
			
			logger.error("message is not wrapped using json, please check " + e);
			UnifiedResponse.sendErrResponse(response, 5002);
			return;
		}
		/**
		 * json object contains 
		 * 		
		 * 		type  : insert/delete
		 * 		place : hadoop/hbase
		 * 		data  : jsonObject
		 * 		path  : storage path 
		 * 
		 */
		String type = (String) jo.get("type");
		if(StringUtils.isBlank(type) || !type.equals("insert")) {
			
			logger.error("json type is not correct, please check ");
			UnifiedResponse.sendErrResponse(response, 5003);
			return;
		}
		
		String place = (String) jo.get("place");
		if(StringUtils.isBlank(place)) {
			
			logger.error("json place is not correct, please check ");
			UnifiedResponse.sendErrResponse(response, 5004);
			return;
		}
		
		String path = (String) jo.get("path");
		if(StringUtils.isBlank(path)) {
			
			logger.error("json path is not correct, please check ");
			UnifiedResponse.sendErrResponse(response, 5005);
			return;
		}
		
		JSONObject data = (JSONObject) jo.get("data");
		HadoopServerImpl.writeToHdfs(path, data);
		UnifiedResponse.sendSuccessResponse(response);
	}

}
