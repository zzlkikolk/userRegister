package com.jerry.service.info.impl;

import com.jerry.dao.info.InfoUserDao;
import com.jerry.pojo.info.InfoUser;
import com.jerry.service.info.InfoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户接口业务实现
 * @author zhangzhilin
 */
@Service("infoUserService")
public class InfoUserServiceImpl implements InfoUserService {



    private final static Logger log= LoggerFactory.getLogger(InfoUserServiceImpl.class);

    @Resource
    private InfoUserDao infoUserDao;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public List<InfoUser> list(InfoUser entity) {
        return null;
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public InfoUser save(InfoUser entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUnCheck(0);
        entity.setUserHead("1");
        return infoUserDao.save(entity);
    }

    @Override
    public void removeById(Serializable id) {
        infoUserDao.deleteById(id);
    }

    @Override
    public InfoUser getOne(Serializable id) {
        return infoUserDao.getOne(id);
    }

    @Override
    public InfoUser getUser(String email) {
        InfoUser user=(InfoUser) redisTemplate.opsForValue().get(email+"_info");
        if(user!=null){
            return user;
        }else{
            return infoUserDao.findByUserEmail(email);
        }
    }
}
