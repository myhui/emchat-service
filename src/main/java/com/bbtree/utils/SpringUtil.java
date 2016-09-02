package com.bbtree.utils;

import com.bbtree.redis.RedisUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/7/14.
 */
@Component
public class SpringUtil implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringUtil.applicationContext = applicationContext ;
    }
    /**
     * 获得spring容器中id为name的bean
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public static RedisUtil getRedisBean() {
        return (RedisUtil)getBean("redisUtil");
    }

    public static boolean isDev(){
        String [] profiles = applicationContext.getEnvironment().getActiveProfiles();
        return Arrays.binarySearch(profiles, "dev")==-1?false:true;
    }

}
