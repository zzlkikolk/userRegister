package com.jerry.dao.info;

import com.jerry.pojo.info.InfoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * info_user Spring data JPA接口
 * @author zhangzhilin
 */
@Repository
public interface InfoUserDao extends JpaRepository<InfoUser, Serializable> {
    /**
     * jpa 生成查询条件（根据邮箱查询)
     * @param email 邮箱
     * @return
     */
    InfoUser findByUserEmail(String email);
}
