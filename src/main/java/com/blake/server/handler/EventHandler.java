package com.blake.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.blake.server.request.RequestUtils;
import com.blake.server.response.UnifiedResponse;
import com.blake.server.type.RequestType;
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

			String msg = "error when reading transferring data " + e;
			logger.error(msg);
			UnifiedResponse.sendErrResponse(response, 5001, msg);
			return;
		}
		
		RequestUtils requestUtils = RequestUtils.parseRequest(buffer.toString(), response);
		if(null == requestUtils || !requestUtils.checkParameter()) { 
			
			String msg = "request parameter is not correct, please check ";
			logger.error(msg);
			UnifiedResponse.sendErrResponse(response, 5002, msg);
			return;
		}
		
		if(logger.isDebugEnabled()) {
			
			logger.debug(" requestUtils path is : " + requestUtils.getPath());
			logger.debug(" requestUtils data is : " + requestUtils.getData());
			logger.debug(" requestUtils append is : " + requestUtils.isAppend());
		}
		if(RequestType.fromString(requestUtils.getType()) == RequestType.insert) {
			
			if(HadoopServerImpl.checkFileExist(requestUtils.getPath())) {
				
				HadoopServerImpl.appendToHdfs(requestUtils.getPath(), requestUtils.getData());
			} else {
				
				HadoopServerImpl.writeToHdfs(requestUtils.getPath(), requestUtils.getData());
			}
			
			UnifiedResponse.sendSuccessResponse(response);
		}
		
		if(RequestType.fromString(requestUtils.getType()) == RequestType.query) {
			
			String data = HadoopServerImpl.readFileFromHdfs(requestUtils.getPath());
			UnifiedResponse.sendSuccessResponseWithData(response, data);
		}
		
		UnifiedResponse.sendErrResponse(response, 4000, "request type not defined or recognized");
		
	}

}
