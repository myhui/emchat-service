package com.bbtree.service;

import com.bbtree.constants.Constant;
import com.bbtree.db.dao.ZhsSchoolDao;
import com.bbtree.db.dao.ZhsUserDao;
import com.bbtree.redis.RedisUtil;
import com.bbtree.redis.entity.ClassInfo;
import com.bbtree.redis.entity.SchoolInfo;
import com.bbtree.redis.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2016/8/18.
 */
@Service
public class DataProvideService {

    private static final Logger logger = LoggerFactory.getLogger(DataProvideService.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ZhsSchoolDao zhsSchoolDao;

    @Autowired
    ZhsUserDao zhsUserDao;

    public String dataProvide(long school_id) {

        //只支持单个单个学校注册
        if (0 != school_id) {
            //IM学校对象
            SchoolInfo school = new SchoolInfo();

            school.setSchoolId(school_id);
            logger.info("开始处理学校:" + school_id);

            String school_name = zhsSchoolDao.findSchoolNameBySchoolId(school_id);

            if(school_name == null){
                return "不存在此学校,"+school_id;
            }
            school.setSchoolName(school_name);

            //学校下班级列表
            List<ClassInfo> classList = new ArrayList<ClassInfo>();

            //学校下老师列表(包括普通老师与园领导)
            List<UserInfo> teacherList = new ArrayList<UserInfo>();

            //学校下老师列表(包括普通老师与园领导)
            List<UserInfo> presidentList = new ArrayList<UserInfo>();

            //园领导
            Set<Integer> user_ids_president = zhsUserDao.findPresidentIdsBySchoolId(school_id);
            Iterator<Integer> iPresidentIds = user_ids_president.iterator();//先迭代出来
            while(iPresidentIds.hasNext()){//遍历
                UserInfo user = new UserInfo();
                user.setUserId(iPresidentIds.next());
                user.setType(3);
                presidentList.add(user);
            }

            school.setPresidentList(presidentList);

            //学校所有老师(不含园长)
            Set<Integer> user_ids_all_teacher = zhsUserDao.findTeacherIdsBySchoolId(school_id);
            Iterator<Integer> iTeacherIds = user_ids_all_teacher.iterator();//先迭代出来
            while(iTeacherIds.hasNext()) {//遍历
                UserInfo user = new UserInfo();
                user.setUserId(iTeacherIds.next());
                user.setType(2);
                teacherList.add(user);
            }

            //学校老师列表初始化完毕
            school.setTeachers(teacherList);

            //********************************************班级列表******************************************************

            List<Object[]> classLists = zhsSchoolDao.findClassBySchoolId(school_id);

            for (Object[] m : classLists) {
                if(m.length<2){
                    continue;
                }
                ClassInfo classInfo = new ClassInfo();

                int classId = (Integer) m[0];
                String className = (String) m[1];

                classInfo.setClassId(classId);
                classInfo.setClassName(className);

                Set<Integer> teacherIds = zhsUserDao.findTeacherIdsByClassId(classId);


                List<UserInfo> userList = new ArrayList<UserInfo>();
                for (UserInfo teacher : teacherList) {
                    if (teacher != null && teacherIds != null && teacherIds.size() > 0) {
                        if (teacherIds.contains(teacher.getUserId())) {
                            userList.add(teacher);
                        }
                    }
                }
                userList.addAll(presidentList);

                Set<Integer> userIds = zhsSchoolDao.findAllUidFromClass(classId);
                Iterator<Integer> iUserIds = userIds.iterator();//先迭代出来
                while(iUserIds.hasNext()) {//遍历
                    UserInfo user = new UserInfo();
                    user.setUserId(iUserIds.next());
                    user.setType(1);
                    userList.add(user);
                }

                classInfo.setUsers(userList);
                classList.add(classInfo);

            }

            school.setClassList(classList);
            long r = redisUtil.lpush(Constant.IM_SCHOOL, school);
            logger.info("学校Id:" + school_id + "处理结果:" + (r > 0 ? "成功" : "失败"));

            return "学校Id:" + school_id + "处理结果:" + (r > 0 ? "成功" : "失败");
        } else {
            logger.error("请求参数错误");
            return "请求参数有误！！！ ";
        }

    }
}
