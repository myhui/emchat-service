package com.bbtree.server.api.impl;

import com.bbtree.server.api.AuthTokenAPI;
import com.bbtree.server.api.EasemobRestAPI;
import com.bbtree.server.comm.ClientContext;
import com.bbtree.server.comm.body.AuthTokenBody;
import com.bbtree.server.comm.constant.HTTPMethod;
import com.bbtree.server.comm.helper.HeaderHelper;
import com.bbtree.server.comm.wrapper.BodyWrapper;
import com.bbtree.server.comm.wrapper.HeaderWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasemobAuthToken extends EasemobRestAPI implements AuthTokenAPI{
	
	public static final String ROOT_URI = "/token";
	
	private static final Logger log = LoggerFactory.getLogger(EasemobAuthToken.class);
	
	@Override
	public String getResourceRootURI() {
		return ROOT_URI;
	}

	public Object getAuthToken(String clientId, String clientSecret) {
		String url = getContext().getSeriveURL() + getResourceRootURI();
		BodyWrapper body = new AuthTokenBody(clientId, clientSecret);
		HeaderWrapper header = HeaderHelper.getDefaultHeader();
		
		return getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
	}

	public Object getAuthToken() {
		return ClientContext.getInstance().getToken();
	}
}
