package com.bbtree.web;

import com.bbtree.constants.Constant;
import com.bbtree.db.dao.ImGroupDao;
import com.bbtree.db.dao.ImUserDao;
import com.bbtree.db.entity.ImGroup;
import com.bbtree.db.entity.ImUser;
import com.bbtree.redis.RedisUtil;
import com.bbtree.redis.entity.UserInfo;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.bbtree.service.DataProvideService;
import com.bbtree.service.UserService;
import com.bbtree.task.RegisterTask;
import com.bbtree.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    ImUserDao userDao;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;

    @Autowired
    ImGroupDao groupDao;

    @Autowired
    DataProvideService dataProvideService;

    @RequestMapping("/import/{schoolId}")
    @ResponseBody
    public String importSchool(@PathVariable long schoolId) {
        String result = dataProvideService.dataProvide(schoolId);
        return result;
    }

    @RequestMapping("/test-mysql")
    @ResponseBody
    public String insertUser(@RequestParam(name = "user_id" , defaultValue = "29485070") long user_id) {
        List<Long> l = new ArrayList<>();
        l.add(123l);
        l.add(234l);
        l.add(3455l);
        System.out.println("79990090=========");
        userDao.findUserIdIn(l);

        return "";
    }


    @RequestMapping("/test-redis")
    @ResponseBody
    public String insertRedis() {
        ImUser user = new ImUser();
        user.setUserId(123);
        user.setCreateTime(new Date());
        user.setImName("user1");
        user.setImUUID("321");
        user.setStatus(1);

        String userStr = JsonUtil.toJSONString(user);
        boolean b = redisUtil.set("test", userStr);

        return b+"";
    }

    @RequestMapping("/test-im")
    @ResponseBody
    public String testIm() {
        ImUser user = new ImUser();
        user.setUserId(1234449999);
        user.setCreateTime(new Date());
        user.setImName("user1");
        user.setImUUID("321");
        user.setStatus(1);

        boolean b = redisUtil.set("test", user);

        ImUser u = (ImUser)redisUtil.get("test");

        ResponseWrapper ret = userService.userRegister(u.getUserId(), "hywwtest_"+u.getUserId(), "");

        return ret.getResponseBody()+"";
    }


    @RequestMapping("/save")
    @ResponseBody
    public String save() {

        String token = "{\"code\":200,\"body\":{\"access_token\":\"YWMtvIeWKEjgEeanep89bOCGaAAAAVcYsZuhbbwAgffzPZmD8YbLzxbc3Z672Qk\",\"expires_in\":5183999,\"application\":\"a083fe90-705a-11e4-a5e2-77904fbeeb73\",\"create_time\":1468403942,\"client_id\":\"YXA6oIP-kHBaEeSl4neQT77rcw\"}}";
        redisUtil.set(Constant.TOKEN, token);
        Object tokenObj = redisUtil.get(Constant.TOKEN);
        return "";
    }

    @RequestMapping("/task")
    @ResponseBody
    public String task(@RequestParam(name = "start" , defaultValue = "0") int start) {
        String ret = "任务关闭";
        if(start == 200) {
            if(!RegisterTask.isStart()){
                RegisterTask.setIsStart(true);
                ret = "任务开始";
            }else {
                RegisterTask.setIsStart(false);
            }


        }else {
            RegisterTask.setIsStart(false);
        }
        return ret;
    }


    @RequestMapping("/test-php")
    @ResponseBody
    public String testPHP() {

        List<UserInfo> users = new ArrayList<>();
        users.add(new UserInfo(45435));
        users.add(new UserInfo(3453252));
        String re = userService.saveFailUser(users);
        return re;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {

        ImGroup imGroup= groupDao.findByObjIdAndGroupType(123, 1);
        return "";
    }

    @RequestMapping("/reset_pass")
    @ResponseBody
    public String resetPass(@RequestParam(name = "b" , defaultValue = "0") long b,
                            @RequestParam(name = "e" , defaultValue = "0") int e) {

        userService.resetPassword(b,e);
        return "success";
    }



  }