package com.bbtree.db.dao;

import com.bbtree.db.entity.ImGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2016/7/13.
 */
@Transactional
public interface ImGroupDao extends CrudRepository<ImGroup, Long> {

    ImGroup findByObjIdAndGroupType(long objId, int type);

}
