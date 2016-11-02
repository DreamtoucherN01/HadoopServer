package com.blake.util;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JsonParser {
	
	static Logger logger = Logger.getLogger(JsonParser.class);
	
	public static String parseString(JSONObject jo, String name) {
		
		String ret = null;
		if(jo.containsKey(name)){
			
			ret = (String) jo.get(name);
			if(StringUtils.isBlank(ret)) {
				
				logger.error("json "+ name +" is not correct, please check ");
				return null;
			}
		}
		return ret;
	}

	public static boolean parseBoolean(JSONObject jo, String name) {

		
		boolean ret = true;
		if(jo.containsKey(name)){
			
			ret = jo.getBoolean(name);
		}
		return ret;
	}

	public static JSONObject parseJSONObject(JSONObject jo, String name) {

		JSONObject ret = null;
		if(jo.containsKey(name)){
			
			ret = (JSONObject) jo.get(name);
			if(null == ret) {
				
				logger.error("json "+ name +" is not correct, please check ");
				return null;
			}
		}
		return ret;
	}

}
