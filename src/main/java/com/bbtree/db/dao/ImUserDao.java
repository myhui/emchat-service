package com.bbtree.db.dao;

import com.bbtree.db.entity.ImUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
@Transactional
public interface  ImUserDao extends CrudRepository<ImUser, Long> {

    @Query("select userId from  ImUser where userId in (:userIds)")
    List<Long> findUserIdIn(@Param("userIds") List<Long> userIds);

    @Query("select imName from ImUser where userId in(:userIds)")
    List<String> findByImNameIn(@Param("userIds") List<Long> userIds);

//    @Query("from ImUser where userId < :maxuid and userId > :minuid order by userId LIMIT :start, :pagesize")
//    List<ImUser> findUsersByUserId(@Param("maxuid") long maxuid, @Param("minuid") long minuid, @Param("start")long start, @Param("pagesize") long size);


    Page<ImUser> findByUserIdBetween(long min, long max, Pageable pageable);

    @Query("select count(1) from ImUser where userId < :maxuid and userId > :minuid ")
    Long countUsersByUserId(@Param("maxuid") long maxuid, @Param("minuid") long minuid);

}
