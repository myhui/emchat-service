package com.bbtree.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2016/7/13.
 */
public class UserInfo {

    @JSONField(name = "user_id")
    long userId;

    @JSONField(name = "maintype")
    int type;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public UserInfo(long userId){
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserInfo(){
    }
}
