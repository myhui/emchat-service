/**
 * 版权信息（@copyright Copyright 2016-2016 BBTREE.COM All Right Reserved）
 * 与该类相关联类：
 * 作者: Administrator 
 * 部门：技术研发-JAVA
 * 版本：1.0.0
 * JDK: 1.8
 * 创建、开发日期：2016/6/21 9:57
 * 最后修改日期：
 * 复审人：
 */
package com.bbtree.utils;

import com.alibaba.fastjson.JSON;

/**
 * 名称: JsonUtil
 * 描述:  
 *
 * 作者: Administrator
 * 日期: 2016/6/21 9:57
 * 版本: 
 */
public class JsonUtil {

    public static String toJSONString(Object obj){
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String data, Class<T> cls){
        return JSON.parseObject(data, cls);
    }
}
