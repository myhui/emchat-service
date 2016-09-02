package com.bbtree.db.dao;

import com.bbtree.db.entity.ZhsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/18.
 */
@Transactional
public interface ZhsUserDao extends JpaRepository<ZhsUser, Long>{

    @Query(value = "select user_id from zhs_president where school_id = :schoolId", nativeQuery = true)
    Set<Integer> findPresidentIdsBySchoolId(@Param("schoolId")long schoolId);

    @Query(value = "select user_id from zhs_teacher where school_id = :schoolId", nativeQuery = true)
    Set<Integer> findTeacherIdsBySchoolId(@Param("schoolId")long schoolId);

    @Query(value = "select user_id from zhs_user where school_id = :schoolId", nativeQuery = true)
    Set<Integer> findUserIdsBySchoolId(@Param("schoolId")long schoolId);

    @Query(value = "select user_id from zhs_teacher_class where class_id = :classId", nativeQuery = true)
    Set<Integer> findTeacherIdsByClassId(@Param("classId")long classId);
}
