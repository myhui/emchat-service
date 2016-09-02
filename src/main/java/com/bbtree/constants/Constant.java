package com.bbtree.constants;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * create by hui.
 * Data : 2016/7/13
 * Time : 11:27
 */
@Component
public class Constant {

    private static final Logger log = LoggerFactory.getLogger(Constant.class);

    public static final String IM_SCHOOL = "im_school";

    public static final String IM_PREFIX = "hyww_";

    public static final String TOKEN = "imToken";

    public static final String FAIL_CREATE_URL = "/service/v2/im/imReRegUser/";

    public static final String IM_PASSWORD_SUFFIX = "_123456";

    public static final String IM_ADMIN = "adminhyww";

}
