package com.bbtree.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redicache 工具类
 */
@SuppressWarnings("unchecked")
@Component
public class RedisUtil {
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object lpop(final String key) {
        Object result = null;
        ListOperations<Serializable, Object> operations = redisTemplate.opsForList();
        result = operations.leftPop(key);
        return result;
    }

    public long lpush(String key, Object value) {
        try {
            ListOperations<Serializable, Object> operations = redisTemplate.opsForList();
            return operations.leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Set<String> keys(final String keys){
        return redisTemplate.keys(keys);
    }

    //string
    public String getStr(final String key){
        String result = "";
        try {
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            result = operations.get(key);
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String lpopStr(final String key){
        try {
            ListOperations<String, String> operations = stringRedisTemplate.opsForList();
            return operations.leftPop(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> lrangeStr(final String key, int start, int end){
        try {
            ListOperations<String ,String > operations = stringRedisTemplate.opsForList();
            return operations.range(key, start, end);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String lindexStr(final String key, int index){
        try {
            ListOperations<String ,String > operations = stringRedisTemplate.opsForList();
            return operations.index(key, index);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}