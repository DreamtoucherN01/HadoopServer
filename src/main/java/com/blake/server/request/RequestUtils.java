package com.blake.server.request;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.blake.util.JsonParser;

public class RequestUtils {
	
	/**
	 * json object contains 
	 * 		
	 * 		type  : insert/delete
	 * 		place : hadoop/hbase
	 * 		data  : jsonObject
	 * 		path  : storage path 
	 * 
	 */
	
	static Logger logger = Logger.getLogger(RequestUtils.class);
	
	public String type;
	
	public JSONObject data;
	
	public String path;
	
	public String place;
	
	public boolean append ;

	
	public RequestUtils() {

		
	}
	
	public RequestUtils(String type, JSONObject data, String path, String place,
			boolean append) {
		
		super();
		this.type = type;
		this.data = data;
		this.path = path;
		this.place = place;
		this.append = append;
	}

	public static RequestUtils parseRequest(String requestData, HttpServletResponse response) {
		
		String type;
		JSONObject data;
		String path;
		String place;
		boolean append ;
		
		JSONObject jo = null;
		try{
			
			jo =   JSONObject.fromObject(URLDecoder.decode(requestData, HTTP.UTF_8));
			
		} catch(Exception e) {
			
			logger.error("message is not wrapped using json, please check " + e);
			return null;
		}
		type = JsonParser.parseString(jo, "type");
		place = JsonParser.parseString(jo, "place");
		path = JsonParser.parseString(jo, "path");
		data = JsonParser.parseJSONObject(jo, "data");
		append = JsonParser.parseBoolean(jo, "append");
		return new RequestUtils( type,  data,  path,  place, append);
	}
	

	public boolean checkParameter(HttpServletResponse response) {

		
		if(StringUtils.isBlank(this.type) || !this.type.equals("insert") 
				|| StringUtils.isBlank(this.place) || StringUtils.isBlank(this.path)) {
			
			return false;
		}
		
		if( null == this.data) {
			
			return false;
		}
		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	@Override
	public String toString() {
		return "RequestUtils [type=" + type + ", data=" + data + ", path="
				+ path + ", place=" + place + ", append=" + append + "]";
	}
	
}
