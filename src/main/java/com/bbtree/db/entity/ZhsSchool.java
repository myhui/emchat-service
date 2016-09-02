package com.bbtree.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/18.
 */
@Entity
@Table(name = "zhs_school")
public class ZhsSchool implements Serializable{

    private static final long serialVersionUID = 3202843436745736267L;
    @Id
    @Column(name = "school_id", nullable = false)
    long schoolId;

    @Column(name = "school_name")
    String schoolName;

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
