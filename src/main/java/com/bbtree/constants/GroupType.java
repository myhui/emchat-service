package com.bbtree.constants;

/**
 * Created by Administrator on 2016/7/16.
 */
public enum GroupType {
    SCHOOL_TYPE("学校组",1), CLASS_TYPE("班级组",2);
    // 成员变量
    private String name;
    private int type;
    // 构造方法
    GroupType(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public int getType(){
        return type;
    }
    //覆盖方法
    @Override
    public String toString() {
        return this.type+"_"+this.name;
    }
}
