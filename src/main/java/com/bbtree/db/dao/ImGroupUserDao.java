package com.bbtree.db.dao;

import com.bbtree.db.entity.ImGroup;
import com.bbtree.db.entity.ImGroupUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2016/7/13.
 */
@Transactional
public interface ImGroupUserDao extends CrudRepository<ImGroupUser, Long> {

    ImGroupUser findByImGroupIdAndImName(String groupId, String name);
}
