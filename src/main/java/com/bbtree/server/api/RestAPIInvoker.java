package com.bbtree.server.api;

import com.bbtree.server.comm.wrapper.BodyWrapper;
import com.bbtree.server.comm.wrapper.HeaderWrapper;
import com.bbtree.server.comm.wrapper.QueryWrapper;
import com.bbtree.server.comm.wrapper.ResponseWrapper;

import java.io.File;

public interface RestAPIInvoker {
	ResponseWrapper sendRequest(String method, String url, HeaderWrapper header, BodyWrapper body, QueryWrapper query);
	ResponseWrapper uploadFile(String url, HeaderWrapper header, File file);
    ResponseWrapper downloadFile(String url, HeaderWrapper header, QueryWrapper query);
}
