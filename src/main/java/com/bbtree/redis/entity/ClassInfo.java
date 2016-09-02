package com.bbtree.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class ClassInfo {

    long classId;

    @JSONField(name = "class_name")
    String className;

    @JSONField(name = "userList")
    List<UserInfo> users = new ArrayList<>();

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }
}
