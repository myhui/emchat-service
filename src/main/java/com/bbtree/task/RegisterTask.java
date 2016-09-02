package com.bbtree.task;

import com.bbtree.redis.RedisUtil;
import com.bbtree.redis.entity.SchoolInfo;
import com.bbtree.service.RegisterService;
import com.bbtree.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Created by Administrator on 2016/7/13.
 */
@Component
public class RegisterTask {

    private static final Logger logger = LoggerFactory.getLogger(RegisterTask.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RegisterService registerService;

    private static volatile boolean isStart = false;

    public static void setIsStart(boolean isStart) {
        RegisterTask.isStart = isStart;
    }

    public static boolean isStart() {
        return isStart;
    }

    private static final String COMMON_DATE = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(COMMON_DATE);

    @Scheduled(fixedDelay = 5000)
    public void task() {
        if(!isStart){
            return;
        }
        registerIm();

    }

    public void registerIm() {

            while (true){

                if(!isStart){
                    return;
                }
                SchoolInfo info =  (SchoolInfo)redisUtil.lpop(Constant.IM_SCHOOL);

                if (info == null) {
                    break;
                }
                logger.info("开始注册学校:{}, data = {}",info.getSchoolId(),info);
                registerService.startToRegister(info);

                logger.info("school = {}, 已经注册", info.getSchoolName());
            }

    }

    private Set<String> getSchools(){
        return redisUtil.keys(Constant.IM_SCHOOL);
    }
}
