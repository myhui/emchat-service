package com.bbtree.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/13.
 */
@Entity
@Table(name = "zhs_im_group")
public class ImGroup implements Serializable{

    private static final long serialVersionUID = 522567872773714584L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "id")
    long id;


    @Column(name = "obj_id")
    long objId;

    @Column(name = "im_group_id")
    String imGroupId;

    @Column(name = "im_group_name")
    String imGroupName;

    @Column(name = "group_type")
    int groupType;

    @Column(name = "create_date")
    Date createDate;

    @Column(name = "status")
    int status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getObjId() {
        return objId;
    }

    public void setObjId(long objId) {
        this.objId = objId;
    }

    public String getImGroupId() {
        return imGroupId;
    }

    public void setImGroupId(String imGroupId) {
        this.imGroupId = imGroupId;
    }

    public String getImGroupName() {
        return imGroupName;
    }

    public void setImGroupName(String imGroupName) {
        this.imGroupName = imGroupName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public Date getCreateDate() {
        if(createDate == null){
            return null;
        }else {
            return (Date)createDate.clone();
        }

    }

    public void setCreateDate(Date createDate) {
        if(createDate == null){
            this.createDate = null;
        }else {
            this.createDate = (Date) createDate.clone();
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
