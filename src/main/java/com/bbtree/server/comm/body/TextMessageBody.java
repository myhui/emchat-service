package com.bbtree.server.comm.body;

import com.bbtree.server.comm.constant.MsgType;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TextMessageBody extends MessageBody {
	private ObjectNode msg;

	public TextMessageBody(String targetType, String[] targets, String from, Map<String, String> ext, String msg) {
		super(targetType, targets, from, ext);

		this.msg = JsonNodeFactory.instance.objectNode();
		this.msg.put("type", MsgType.TEXT);
		this.msg.put("msg", msg);
	}

	public ObjectNode getMsg() {
		return msg;
	}

    public ContainerNode<?> getBody() {
        if(!isInit()){
//            this.getMsgBody().put("type", MsgType.TEXT);
            this.getMsgBody().put("msg", msg);
            this.setInit(true);
        }

        return this.getMsgBody();
    }

    public Boolean validate() {
		return super.validate() && msg != null;
	}
}


