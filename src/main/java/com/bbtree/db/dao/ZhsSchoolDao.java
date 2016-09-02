package com.bbtree.db.dao;

import com.bbtree.db.entity.ZhsSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.connection.convert.MapConverter;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/18.
 */
@Transactional
public interface ZhsSchoolDao extends JpaRepository<ZhsSchool, Long> {

    @Query("select schoolName from ZhsSchool where schoolId = :schoolId")
    String findSchoolNameBySchoolId(@Param("schoolId") long schoolId);


    @Query(nativeQuery = true, value = "select class_id,class_name from zhs_class where school_id = :schoolId")
    List<Object[]> findClassBySchoolId(@Param("schoolId") long schoolId);

    @Query(value = "select r.user_id from zhs_relatives r join zhs_child c join zhs_user u on r.child_id = c.child_id and r.user_id = u.user_id where c.class_id = :classId" ,nativeQuery = true)
    Set<Integer> findAllUidFromClass(@Param("classId")long classId);
}
