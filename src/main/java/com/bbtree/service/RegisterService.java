package com.bbtree.service;

import com.alibaba.fastjson.JSONObject;
import com.bbtree.constants.Constant;
import com.bbtree.constants.GroupType;
import com.bbtree.db.dao.ImGroupDao;
import com.bbtree.db.entity.ImGroup;
import com.bbtree.db.entity.ImUser;
import com.bbtree.redis.RedisUtil;
import com.bbtree.redis.entity.ClassInfo;
import com.bbtree.redis.entity.SchoolInfo;
import com.bbtree.redis.entity.UserInfo;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.bbtree.utils.JsonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service
public class RegisterService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ImGroupDao groupDao;


    public void startToRegister(SchoolInfo info) {
        if (info == null) {
            logger.error("学校为空 。");
            return;
        }
        try {

            //注册学校group
            String groupId = groupService.createGroup(info.getSchoolId(), info.getSchoolName(), GroupType.SCHOOL_TYPE);
            if (groupId == null) {
                logger.error("学校组创建失败。 school_id = {}", info.getSchoolId());
            }

            //注册学校老师和园长
            List<UserInfo> teacher = info.getTeachers();
            List<UserInfo> president = info.getPresidentList();

            if (president != null && president.size() > 0) {
                teacher.addAll(president);
            }

            //过滤已注册用户
            List<Long> exitUsers = getUserIdsByUserInfo(teacher);
            teacher = filterUserInfos(teacher, exitUsers);

            List<String> staff = userService.createUsers(teacher);
            if (staff != null && staff.size() > 0) {
                groupService.addBatchUserToGroup(groupId, ListToStringArray(staff));
            }

            //注册班级
            List<ClassInfo> classs = info.getClassList();
            for (ClassInfo cls : classs) {
                registerCls(cls);
            }

        } catch (Exception e) {
            logger.error("=====================导入学校={},错误。{}", info.getSchoolId(), e);
        }

    }

    private List<Long> getUserIdsByUserInfo(List<UserInfo> uinfos) {
        if (uinfos == null)
            return null;
        List<Long> ls = new ArrayList<>();
        for (UserInfo userInfo : uinfos) {
            if (userInfo != null) {
                ls.add(userInfo.getUserId());
            }
        }
        return userService.findExistUser(ls);
    }

    private List<UserInfo> filterUserInfos(List<UserInfo> users, List<Long> exitsUsers) {
        if (exitsUsers == null)
            return users;
        if (users == null)
            return null;

        List<UserInfo> us = new ArrayList<>();

        for (UserInfo info : users) {
            if (!exitsUsers.contains(info.getUserId())) {
                us.add(info);
            }
        }
        return us;
    }

    private String[] ListToStringArray(List<String> list) {
        if (list != null && list.size() > 0) {
            String[] arrayStr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arrayStr[i] = list.get(i);
            }
            return arrayStr;
        }
        return new String[0];
    }

    private String[] ListToArray(List<UserInfo> Users) {

        if (Users != null && Users.size() > 0) {
            String[] arrayStr = new String[Users.size()];
            for (int i = 0; i < Users.size(); i++) {
                arrayStr[i] = Users.get(i).getUserId() + "";
            }
            return arrayStr;
        }
        return new String[0];
    }

    private void registerCls(ClassInfo cls) {

        String groupId = groupService.createGroup(cls.getClassId(), cls.getClassName(), GroupType.CLASS_TYPE);

        //获取班级用户
        List<UserInfo> classUsers = cls.getUsers();

        List<UserInfo> parents = new ArrayList<>();

        List<UserInfo> teachers = new ArrayList<>();

        //过滤已经注册 老师 园长
        for (UserInfo u : classUsers) {

            if (u.getType() == 1) {
                parents.add(u);
            }
            if (u.getType() == 2 || u.getType() == 3) {
                teachers.add(u);
            }
        }

        if (StringUtils.isNotEmpty(groupId)) {
            groupService.addBatchUserToGroup(groupId, addPrefix(ListToArray(teachers)));
        }

        //过滤已注册用户
        List<Long> exitUsers = getUserIdsByUserInfo(parents);
        parents = filterUserInfos(parents, exitUsers);

        List<String> users = userService.createUsers(parents);  //注册班级用户

        if (StringUtils.isNotEmpty(groupId)) {
            groupService.addBatchUserToGroup(groupId, ListToStringArray(users));
        }

    }

    private String[] addPrefix(String[] ss) {
        if (ss == null)
            return new String[0];
        String[] sss = new String[ss.length];
        for (int i = 0; i < ss.length; i++) {
            sss[i] = Constant.IM_PREFIX + ss[i];
        }
        return sss;
    }

}
