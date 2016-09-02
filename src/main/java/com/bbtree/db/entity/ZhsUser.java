package com.bbtree.db.entity;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 2016/8/18.
 */
@Entity
@Table(name = "zhs_user")
public class ZhsUser {

    @Id
    @Column(name = "user_id")
    long userId;

    @Column(name = "maintype")
    int maintype;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getMaintype() {
        return maintype;
    }

    public void setMaintype(int maintype) {
        this.maintype = maintype;
    }
}
