package com.blake.server.response;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnifiedResponse {
	
	final static Logger logger = LoggerFactory.getLogger(UnifiedResponse.class);

	public static void sendErrResponse(HttpServletResponse response, int errcode, String msg) {

		String respStr = "{\"result\":false,\"err\":"+ errcode +",\"ver\":1,\"desc\":\""+ msg +" \"}";
		if(logger.isDebugEnabled()) {
			
			logger.debug("the response is {}" , respStr);
		}
		response.setContentLength(respStr.getBytes().length);
		try {
			
			response.getWriter().write(respStr);
			response.getWriter().close();
			return;
		} catch (IOException e) {
			
			logger.error("catch IOEXception : {}", e);
		}
	}

	public static void sendSuccessResponse(HttpServletResponse response) {

		String respStr = "{\"result\":true,\"err\":0,\"ver\":1}";
		if(logger.isDebugEnabled()) {
			
			logger.debug("the response is {}" , respStr);
		}
		response.setContentLength(respStr.getBytes().length);
		try {
			
			response.getWriter().write(respStr);
			response.getWriter().close();
			return;
		} catch (IOException e) {
			
			logger.error("catch IOEXception : {}", e);
		}
	}

	public static void sendSuccessResponse(HttpServletResponse response,
			long count) {

		JSONObject result =  new JSONObject();
		result.put("result", true);
		result.put("err", 0);
		result.put("count", count); 
		result.put("ver", 1);
		
		if(logger.isDebugEnabled()) {
			
			logger.debug("the response is {}" , result.toString());
		}
		response.setContentLength(result.toString().getBytes().length);
		try {
			
			response.getWriter().write(result.toString());
			response.getWriter().close();
			return;
		} catch (IOException e) {
			
			logger.error("catch IOEXception : {}", e);
		}
	}

}
