package com.bbtree.server.api.impl;

import com.bbtree.server.api.ChatMessageAPI;
import com.bbtree.server.api.EasemobRestAPI;
import com.bbtree.server.comm.constant.HTTPMethod;
import com.bbtree.server.comm.helper.HeaderHelper;
import com.bbtree.server.comm.wrapper.HeaderWrapper;
import com.bbtree.server.comm.wrapper.QueryWrapper;

public class EasemobChatMessage extends EasemobRestAPI implements ChatMessageAPI {

    private static final String ROOT_URI = "chatmessages";

    public Object exportChatMessages(Long limit, String cursor, String query) {
        String url = getContext().getSeriveURL() + getResourceRootURI();
        HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
        QueryWrapper queryWrapper = QueryWrapper.newInstance().addLimit(limit).addCursor(cursor).addQueryLang(query);

        return getInvoker().sendRequest(HTTPMethod.METHOD_DELETE, url, header, null, queryWrapper);
    }

    @Override
    public String getResourceRootURI() {
        return ROOT_URI;
    }
}
