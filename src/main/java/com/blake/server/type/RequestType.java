package com.blake.server.type;

public enum RequestType {

	insert("insert"),
	query("query");
	
	private String requestType;

	private RequestType(String requestType) {
		
		this.requestType = requestType;
	}
	
	public static RequestType fromString(String requestType) {
		
		for(RequestType type : RequestType.values()) {
			
			if(type.requestType.equals(requestType)) {
				
				return type;
			}
		}
		
		throw new IllegalArgumentException();
	}
}
