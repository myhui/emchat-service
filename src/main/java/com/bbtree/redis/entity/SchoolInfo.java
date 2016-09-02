package com.bbtree.redis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class SchoolInfo {

    @JSONField(name = "school_id")
    long schoolId;

    @JSONField(name = "school_name")
    String schoolName;

    @JSONField(name = "classList")
    List<ClassInfo> classList = new ArrayList<>();

    @JSONField(name = "teacherList")
    List<UserInfo> teachers = new ArrayList<>();

    @JSONField(name = "presidentList")
    List<UserInfo> presidentList = new ArrayList<>();

    public List<UserInfo> getPresidentList() {
        return presidentList;
    }

    public void setPresidentList(List<UserInfo> presidentList) {
        this.presidentList = presidentList;
    }

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

    public List<ClassInfo> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassInfo> classList) {
        this.classList = classList;
    }

    public List<UserInfo> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<UserInfo> teachers) {
        this.teachers = teachers;
    }
}
