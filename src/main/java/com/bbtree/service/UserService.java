package com.bbtree.service;

import com.bbtree.constants.Constant;
import com.bbtree.db.dao.ImUserDao;
import com.bbtree.db.entity.ImGroup;
import com.bbtree.db.entity.ImUser;
import com.bbtree.redis.RedisUtil;
import com.bbtree.redis.entity.ClassInfo;
import com.bbtree.redis.entity.UserInfo;
import com.bbtree.server.api.IMUserAPI;
import com.bbtree.server.comm.ClientContext;
import com.bbtree.server.comm.EasemobRestAPIFactory;
import com.bbtree.server.comm.body.IMUserBody;
import com.bbtree.server.comm.body.IMUsersBody;
import com.bbtree.server.comm.body.ResetPasswordBody;
import com.bbtree.server.comm.utils.MD5Util;
import com.bbtree.server.comm.wrapper.BodyWrapper;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.bbtree.utils.HttpUtil;
import com.bbtree.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ImUserDao userDao;

    @Value("${app.php_url}")
    String PHP_URL;


    EasemobRestAPIFactory factory = ClientContext.getEasemobRestAPIFactory();
    IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);

    public ResponseWrapper userRegister(String username, String password, String nickName){
        BodyWrapper userBody = new IMUserBody(username, password, nickName);
        ResponseWrapper ret = (ResponseWrapper) user.createNewIMUserSingle(userBody);
        return ret;
    }

    public ResponseWrapper userRegister(long username, String password, String nickName){
        BodyWrapper userBody = new IMUserBody(username+"", password, nickName);
        ResponseWrapper ret = (ResponseWrapper)user.createNewIMUserSingle(userBody);
        return ret;
    }

    //批量注册的用户数量不要过多，建议在20-60之间。
    public ResponseWrapper userRegisterBatch(List<UserInfo> userlist){
        List<IMUserBody> users = new ArrayList<IMUserBody>();
        for(UserInfo u:userlist){
            String imName = Constant.IM_PREFIX+u.getUserId();
            String password = MD5Util.md5Digest(imName + Constant.IM_PASSWORD_SUFFIX);
            users.add(new IMUserBody( imName, password, null));
        }
        BodyWrapper usersBody = new IMUsersBody(users);
        try {

            Thread.currentThread().sleep(30);
            logger.info("get url ....  {}",System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ResponseWrapper)user.createNewIMUserBatch(usersBody);
    }

    public String saveFailUser(List<UserInfo> users){

        try {
            Map<String, Object> param = new HashMap<>();
            param.put("user_id", getStringArrayByUser(users));

            Map<String,Object> jsdata = new HashMap<>();
            jsdata.put("jsdata", JsonUtil.toJSONString(param));
            return HttpUtil.doGet(PHP_URL+Constant.FAIL_CREATE_URL, jsdata);
        } catch (Exception e) {
            logger.error("调用php用户im注册，错误{}",e);
        }

        return null;
    }

    public String saveFailUser(String user){

        try {
            logger.info("用户IM激活失败。 user {}", user);
            Map<String, Object> param = new HashMap<>();

            Map<String,String> us = new HashMap<>();
            us.put("user_id", user);

            param.put("jsdata", JsonUtil.toJSONString(us));
            return HttpUtil.doGet(PHP_URL+Constant.FAIL_CREATE_URL, param);
        } catch (Exception e) {
            logger.error("调用php用户im注册，错误{}",e);
        }

        return null;
    }

    public String getStringArrayByUser(List<UserInfo> users){
        if(users==null){
            return "";
        }
        StringBuffer buffer = new StringBuffer("");
        for(int i =0 ;i<users.size(); i++){
            buffer.append(users.get(i).getUserId()+"");
            if(i != users.size() -1){
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    /**
     * 保存注册成功用户
     * @param responseWrapper
     * @return 注册成功用户im_name
     */
    public List<String> saveSuccessUser(ResponseWrapper responseWrapper){

        List<String> us = new ArrayList<>();

        List<ImUser> users = getImUsersByResponse(responseWrapper);
//        userDao.save(users);
        for (ImUser user : users){
            if(!userDao.exists(user.getUserId())){
                ImUser u = userDao.save(user);
                us.add(u.getImName());
            }else {
                ImUser u = userDao.findOne(user.getUserId());
                us.add(u.getImName());
            }
            us.add(user.getImName());

        }
        return us;
    }

    public List<ImUser> getImUsersByResponse(ResponseWrapper responseWrapper){
        ObjectNode body = (ObjectNode)responseWrapper.getResponseBody();
        JsonNode users = body.get("entities");

        if (users ==null || users.size()<1){
            return null;
        }
        List<ImUser> imUsers = new ArrayList<>();
        for (JsonNode u : users){

            if(u.get("activated").asBoolean()){
                ImUser imUser = new ImUser();

                String username = u.get("username").asText();
                long userId = resolveUserName(username);
                if(userId == 0){
                    logger.error("用户注册失败，{}",username);
                    continue;
                }
                imUser.setImName(username);
                imUser.setUserId(userId);
                imUser.setImUUID(u.get("uuid").asText());
                imUser.setStatus(1);
                long created = u.get("created").asLong();
                imUser.setCreateTime(new Date(created));

                imUsers.add(imUser);
            }else {
                saveFailUser(u.get("username").asText().replace(Constant.IM_PREFIX, ""));
            }

        }
        return imUsers;
    }

    private long resolveUserName(String userName){
        if(StringUtils.isEmpty(userName) || !userName.contains(Constant.IM_PREFIX)){
            return 0;
        }
        try {
            String us = userName.replace(Constant.IM_PREFIX, "");
            long uid = Long.parseLong(us);
            return uid;
        }catch (Exception e){
            logger.error("用户名解析用户ID 错误。 username = {}",userName);
        }
        return 0;

    }


    /**
     * 注册用户
     * @param users
     * @return 注册用户的 im_name
     */
    public List<String> createUsers(List<UserInfo> users) {
        if(users == null || users.size() < 1){
            return null;
        }

        List<String> staff = new ArrayList<>();

        int maxSize = 30;

        if (users.size() > maxSize) {
            List<UserInfo> tempUs = new ArrayList<>();
            for (UserInfo u : users) {

                if (tempUs.size() < maxSize) {
                    tempUs.add(u);
                } else {
                    ResponseWrapper response = userRegisterBatch(tempUs);
                    List<String> s = saveUsersByResponse(response, tempUs);
                    if(s!=null){
                        staff.addAll(s);
                    }
                    tempUs.clear();
                }
            }
            if (tempUs.size() > 1) {
                ResponseWrapper response = userRegisterBatch(tempUs);
                List<String> s = saveUsersByResponse(response, tempUs);
                if(s!=null){
                    staff.addAll(s);
                }
            }

        } else {
            ResponseWrapper response = userRegisterBatch(users);
            List<String> s = saveUsersByResponse(response, users);
            if(s!=null){
                staff.addAll(s);
            }
        }


        return staff;
    }

//    public String getImUserForIm(long userId){
//        if(userId == 0){
//            return null;
//        }
//        Object t = user.getIMUsersByUserName(Constant.IM_PREFIX+userId);
//    }

    private List<String> saveUsersByResponse(ResponseWrapper response, List<UserInfo> createUsers){
        if (null != response && 200 == response.getResponseStatus() && null != response.getResponseBody()) {
            logger.info("注册成功   response = {}", response);
            return saveSuccessUser(response);

        } else {
            if (null != response && null != response.getResponseBody()) {
                ObjectNode obj = (ObjectNode) response.getResponseBody();
                if ("duplicate_unique_property_exists".equals(obj.get("error").asText())) {
                    logger.warn("该些用户之前已经注册, {}", response);


                    //查询用户注册情况
//                    for(UserInfo userInfo : createUsers){
//
//                    }

                    //查询用户是否已经注册
                    List<String> imUsers = userDao.findByImNameIn(getUserIdsByUserInfos(createUsers));
                    if(imUsers!=null){
                        return imUsers;
                    }
                    saveFailUser(createUsers);
                    return null;
                }
            } else if (null == response) {
                logger.warn("无用户");
                return null;
            }

            logger.warn("多个用户注册fail， 请求错误。 response : {}", response);
//            saveFailUser(createUsers);
            return null;

        }
    }

    private List<Long> getUserIdsByUserInfos(List<UserInfo> userInfos){
        if(userInfos==null)
            return null;
        List<Long> uids = new ArrayList<>();
        for(UserInfo uinfo : userInfos){
            if(uinfo!=null)
                uids.add(uinfo.getUserId());
        }
        return uids;
    }

    private List<String> getImNameByImUsers(List<ImUser> imusers){
        if(imusers==null)
            return null;
        List<String> unames = new ArrayList<>();
        for(ImUser im : imusers){
            if(im!=null)
                unames.add(im.getImName());
        }
        return unames;
    }

    public List<Long> findExistUser(List<Long> ls) {
        if(ls == null || ls.size() < 1)
            return null;
        return userDao.findUserIdIn(ls);
    }


    public void resetPassword(String imName){
        String password = MD5Util.md5Digest(imName+ Constant.IM_PASSWORD_SUFFIX);
        logger.info("重置密码  用户：{}  , 密码： {}",imName, password);
        ResetPasswordBody resetPass = new ResetPasswordBody(password);
        try {
            Thread.currentThread().sleep(35);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseWrapper rw = (ResponseWrapper)user.modifyIMUserPasswordWithAdminToken(imName, resetPass);
        logger.info("重置密码  return ：{}", rw);
    }



    @Autowired
    RedisUtil redisUtil;

    public void resetPassword(long min, long max){
        int start = 0;
        int size = 1000;

        long total = userDao.countUsersByUserId(max, min);
        redisUtil.set("im_user_total", total);
        logger.info("size -is {}",size);
        for (; start <= total;){
            Sort sort = new Sort(Sort.Direction.DESC, "userId");
            Pageable page = new PageRequest(start, size, sort);
            Page<ImUser> users = userDao.findByUserIdBetween(min, max, page);
            List<ImUser> imUsers = users.getContent();
            for (ImUser user : imUsers){
                resetPassword(user.getImName());
            }
            start+= size;
            redisUtil.set("im_b_start", start);
        }

    }


}
