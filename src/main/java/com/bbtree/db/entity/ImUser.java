package com.bbtree.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/13.
 */
@Entity
@Table(name = "zhs_im_user")
public class ImUser implements Serializable {

    private static final long serialVersionUID = 522889572773714584L;

    @Id
    @Column(nullable = false, name = "user_id")
    long userId;

    @Column(name = "im_name")
    String imName;

    @Column(name = "im_uuid")
    String imUUID;

    @Column(name = "status")
    int status;

    @Column(name = "create_time")
    Date createTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getImName() {
        return imName;
    }

    public void setImName(String imName) {
        this.imName = imName;
    }

    public String getImUUID() {
        return imUUID;
    }

    public void setImUUID(String imUUID) {
        this.imUUID = imUUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        if(createTime == null){
            return null;
        }else {
            return (Date)createTime.clone();
        }

    }

    public void setCreateTime(Date createTime) {
        if(createTime == null){
            this.createTime = null;
        }else{
            this.createTime = (Date)createTime.clone();
        }

    }

    @Override
    public String toString(){
        return this.userId + this.imName + this.imUUID;
    }
}
