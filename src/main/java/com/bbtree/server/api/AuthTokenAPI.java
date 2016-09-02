package com.bbtree.server.api;

public interface AuthTokenAPI{	
	Object getAuthToken(String clientId, String clientSecret);

	Object getAuthToken();
}
