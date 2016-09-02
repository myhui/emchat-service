package com.bbtree.db.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/13.
 */
@Entity
@Table(name = "zhs_im_group_user")
public class ImGroupUser implements Serializable{

    private static final long serialVersionUID = 522889572333714584L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(name = "im_group_id")
    String imGroupId;


    @Column(name = "im_name")
    String imName;

    public ImGroupUser(String groupId, String imName){
        this.imGroupId = groupId;
        this.imName = imName;
    }

    public ImGroupUser(){}

    public ImGroupUser(long id, String imGroupId, String imName){
        this.id =id;
        this.imGroupId = imGroupId;
        this.imName = imName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImGroupId() {
        return imGroupId;
    }

    public void setImGroupId(String imGroupId) {
        this.imGroupId = imGroupId;
    }

    public String getImName() {
        return imName;
    }

    public void setImName(String imName) {
        this.imName = imName;
    }
}
