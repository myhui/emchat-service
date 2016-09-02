package com.bbtree.service;

import com.bbtree.constants.GroupType;
import com.bbtree.db.dao.ImGroupDao;
import com.bbtree.db.dao.ImGroupUserDao;
import com.bbtree.db.entity.ImGroup;
import com.bbtree.db.entity.ImGroupUser;
import com.bbtree.db.entity.ImUser;
import com.bbtree.redis.entity.ClassInfo;
import com.bbtree.redis.entity.SchoolInfo;
import com.bbtree.redis.entity.UserInfo;
import com.bbtree.server.api.ChatGroupAPI;
import com.bbtree.server.comm.ClientContext;
import com.bbtree.server.comm.EasemobRestAPIFactory;
import com.bbtree.server.comm.body.ChatGroupBody;
import com.bbtree.server.comm.body.UserNamesBody;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.bbtree.constants.Constant;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    UserService userService;

    @Autowired
    ImGroupUserDao groupUserDao;

    @Autowired
    ImGroupDao groupDao;

    EasemobRestAPIFactory factory = ClientContext.getEasemobRestAPIFactory();
    ChatGroupAPI group = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);


    public List<String> addBatchUserToGroup( String groupId, String [] us){
        if(us==null || us.length ==0)
            return null;

        if(us.length > 60){
            List<String> ret = new ArrayList<>();

            List<String> tempUs = new ArrayList<>();


            for (String u : us) {

                if (tempUs.size() < 60) {
                    tempUs.add(u);
                } else {
                    String [] tem = ListToArray(tempUs);
                    UserNamesBody body = new UserNamesBody(tem);
                    ResponseWrapper response = (ResponseWrapper)group.addBatchUsersToChatGroup(groupId, body);
                    List<String> ls = saveUserToGroupByResponse(response, groupId, tem);

                    if(ls!=null){
                        ret.addAll(ls);
                    }
                    tempUs.clear();
                }
            }
            if (tempUs.size() > 1) {
                String [] tem = ListToArray(tempUs);
                UserNamesBody body = new UserNamesBody(tem);
                ResponseWrapper response = (ResponseWrapper)group.addBatchUsersToChatGroup(groupId, body);
                List<String> ls = saveUserToGroupByResponse(response, groupId, tem);
                if(ls!=null){
                    ret.addAll(ls);
                }
            }

            return ret;

        }

        UserNamesBody body = new UserNamesBody(us);
        ResponseWrapper response = (ResponseWrapper)group.addBatchUsersToChatGroup(groupId, body);
        return saveUserToGroupByResponse(response, groupId, us);
    }

    private String[] ListToArray(List<String> list){
        if(list==null)
            return new String[0];

        String [] tt = new String[list.size()];
        for (int i =0; i< list.size(); i++){
            tt[i] = list.get(i);
        }
        return tt;
    }

    private List<String> saveUserToGroupByResponse(ResponseWrapper response, String groupId, String[] users){
        if(users==null){
            return null;
        }
        if (null != response && response.getResponseStatus()!=null &&200 == response.getResponseStatus() && null != response.getResponseBody()) {
            logger.info("用户：{}, 已经添加到组:{}", Arrays.toString(users), groupId);

            //创建 用户-组 关系
            List<ImGroupUser> groupUsers = saveGroupUser(groupId, users);
            if(groupUsers!=null){
                return getImNameByImGroupUsers(groupUsers);
            }
        }else {
            logger.error("添加用户到组失败。  组:{}, 用户:{}, response : {}", groupId, Arrays.toString(users), response);

        }
        return null;

    }


    private List<String> getImNameByImGroupUsers(List<ImGroupUser> imusers){
        if(imusers==null)
            return null;
        List<String> unames = new ArrayList<>();
        for(ImGroupUser im : imusers){
            if(im!=null)
                unames.add(im.getImName());
        }
        return unames;
    }

    public ImGroup saveGroup(String groupId, long created, long cId, String cName, GroupType groupType) {

        ImGroup group = new ImGroup();
        group.setObjId(cId);
        group.setImGroupId(groupId);
        group.setImGroupName(cName);
        group.setGroupType(groupType.getType());
        group.setCreateDate(new Date(created));
        group.setStatus(1);

//        ImGroup tem = groupDao.findByObjIdAndGroupType(group.getObjId(), group.getGroupType());
//        if(tem != null){
//            return tem;
//        }
        return groupDao.save(group);

    }

    public List<ImGroupUser> saveGroupUser(String groupId, String... usernames){
        List<ImGroupUser> list = new ArrayList<>();
        if(usernames == null){
            return null;
        }
        for(String username:usernames){
            ImGroupUser groupUser = new ImGroupUser(groupId, username);
            ImGroupUser u = groupUserDao.findByImGroupIdAndImName(groupId, username);
            if(u!=null){
                list.add(u);
            }else {
                list.add(groupUserDao.save(groupUser));
            }
        }
        return list;
    }

    /**
     * 创建 空组 (学校  or 班级)
     * @param cId  id
     * @param cName 组名
     * @param groupType  1 学校   2 班级
     * @return  groupId
     */
    public String createGroup(long cId, String cName, GroupType groupType){
        if(groupType==null || cId ==0){
            return null;
        }

        //查询组是否已经创建
        ImGroup tem = groupDao.findByObjIdAndGroupType(cId, groupType.getType());
        if(tem!=null){
            return tem.getImGroupId();
        }
        ChatGroupBody groupBody = new ChatGroupBody(cName, cName, true, 500l, true, Constant.IM_ADMIN, new String[0]);
        ResponseWrapper response = (ResponseWrapper)group.createChatGroup(groupBody);
        return saveGroupByResponse(response, cId, cName, groupType);

    }


    private String saveGroupByResponse(ResponseWrapper response, long cId, String cName, GroupType groupType) {
        try {
            if (null != response && response.getResponseStatus()!=null && 200 == response.getResponseStatus() && null != response.getResponseBody()) {
                logger.info("群组已创建， response {}", response);

                ObjectNode body = (ObjectNode) response.getResponseBody();
                String groupId = body.get("data").get("groupid").asText();
                long created = body.get("timestamp").asLong();

                ImGroup group = saveGroup(groupId, created, cId, cName, groupType);
                if(group==null){
                    return null;
                }
                return groupId;
//                return group.getImGroupId();
            } else {

                if (null != response && null != response.getResponseBody()) {
                    logger.error("创建群组失败， response = {}", response);

                    //查询组是否已经创建
                    ImGroup group = groupDao.findByObjIdAndGroupType(cId,groupType.getType());
                    if(group!=null){
                        return group.getImGroupId();
                    }
                    return null;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
