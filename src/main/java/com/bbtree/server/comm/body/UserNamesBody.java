package com.bbtree.server.comm.body;

import com.bbtree.server.comm.wrapper.BodyWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserNamesBody implements BodyWrapper {
    private String[] users;

    public UserNamesBody(String[] users) {
        this.users = users.clone();
    }

    public String[] getUsers() {
        return users.clone();
    }

    public void setUsers(String[] users) {
        this.users = users.clone();
    }

    public ContainerNode<?> getBody() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        ArrayNode names = JsonNodeFactory.instance.arrayNode();
        body.put("usernames", names);

        for (String user: users) {
            names.add(user);
        }

        return body;
    }

    public Boolean validate() {
        return null != users && users.length > 0;
    }
}
