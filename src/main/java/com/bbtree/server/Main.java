package com.bbtree.server;

import com.alibaba.fastjson.JSONObject;
import com.bbtree.server.api.*;
import com.bbtree.server.api.impl.EasemobSendMessage;
import com.bbtree.server.comm.ClientContext;
import com.bbtree.server.comm.EasemobRestAPIFactory;
import com.bbtree.server.comm.body.*;
import com.bbtree.server.comm.wrapper.BodyWrapper;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.fasterxml.jackson.databind.node.ContainerNode;

import java.util.ArrayList;
import java.util.List;

public class Main {

//	public static void main(String[] args) throws Exception {
//		EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
//
//		IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
//
//        SendMessageAPI send = (SendMessageAPI)factory.newInstance(EasemobRestAPIFactory.SEND_MESSAGE_CLASS);
//
////        TextMessageBody messageBody = new TextMessageBody("chatgroups",new String[]{"hyt123oooooo13","tkdoslsdklsdlf"},"from",null, "智慧树测试" );
////        ResponseWrapper rw = (ResponseWrapper)send.sendMessage(messageBody);
////        System.out.println(rw);
//
//        ResetPasswordBody resetPass = new ResetPasswordBody("3673c69888a89e685c13ed76c5a90f31");
//
//        ResponseWrapper rw = (ResponseWrapper)user.modifyIMUserPasswordWithAdminToken("hyww_4285611_123456", resetPass);
//
//        System.out.println(rw);
//
//
////		// Create a IM user
////		BodyWrapper userBody = new IMUserBody("User101", "123456", "HelloWorld");
////		user.createNewIMUserSingle(userBody);
////
////		// Create some IM users
////		List<IMUserBody> users = new ArrayList<IMUserBody>();
////		users.add(new IMUserBody("User00222", "123456", null));
////		users.add(new IMUserBody("User00331333", "123456", null));
////		BodyWrapper usersBody = new IMUsersBody(users);
////		Object t3 = user.createNewIMUserBatch(usersBody);
////
////		System.out.println(t3);
////
////		// Get a IM user
////		Object t = user.getIMUsersByUserName("User002333");
////
////		// Get a fake user
////		Object t2 = user.getIMUsersByUserName("FakeUser001");
////
////		// Get 12 users
////		user.getIMUsersBatch(null, null);
//		/**/
//	}

}
