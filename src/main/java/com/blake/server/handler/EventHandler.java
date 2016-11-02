package com.blake.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.blake.server.request.RequestUtils;
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
		
		RequestUtils requestUtils = RequestUtils.parseRequest(buffer.toString(), response);
		if(null == requestUtils || !requestUtils.checkParameter(response)) { 
			
			logger.error("request parameter is not correct, please check ");
			UnifiedResponse.sendErrResponse(response, 5002);
			return;
		}
		
		HadoopServerImpl.writeToHdfs(requestUtils.getPath(), requestUtils.getData(), requestUtils.isAppend());
		UnifiedResponse.sendSuccessResponse(response);
	}

}
