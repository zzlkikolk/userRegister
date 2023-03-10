package com.jerry.service.info.impl;

import cn.hutool.core.util.DesensitizedUtil;
import com.jerry.dao.info.InfoUserDao;
import com.jerry.pojo.Dto.UserDto;
import com.jerry.pojo.info.InfoUser;
import com.jerry.service.info.InfoUserService;
import com.jerry.utils.AESUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 用户接口业务实现
 * @author zhangzhilin
 */
@Service("infoUserService")
public class InfoUserServiceImpl implements InfoUserService {



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
           String email_1=DesensitizedUtil.email(user.getUserEmail());//数据脱敏
           user.setUserEmail(email_1);
           user.setUserPassword(null);
        }else{
            user=infoUserDao.findByUserEmail(email);
            if(user!=null) {
                String email_1 = DesensitizedUtil.email(user.getUserEmail());//数据脱敏
                user.setUserEmail(email_1);
                user.setUserPassword(null);
            }
        }
        return user;
    }

    /**
     * 新增用户
     * @param userDto
     * @return 成功/失败
     */
    @Transactional
    @Override
    public boolean saveUser(UserDto userDto) {
        if(userDto.getUserEmail()!=null&&userDto.getUserPassword()!=null) {
            InfoUser queryUser = infoUserDao.findByUserEmail(userDto.getUserEmail());
            if (Objects.isNull(queryUser)) {//新增时获取用户是否存在，存在返回FALSE
                String pass= AESUtil.encrypt(userDto.getUserPassword());
                InfoUser infoUser = new InfoUser();
                BeanUtils.copyProperties(userDto, infoUser);
                if(userDto.getUserName()==null){
                    infoUser.setUserName("注册用户");
                }
                infoUser.setUserPassword(pass);
                infoUser.setUserHead("default.png");
                infoUser.setCreateTime(LocalDateTime.now());
                infoUser.setUpdateTime(LocalDateTime.now());
                infoUser.setUnCheck(0);
                InfoUser result = save(infoUser);
                if (!Objects.isNull(result)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 更新用户信息
     * @param userDto
     * @return 成功/失败
     */
    @Transactional
    @Override
    public boolean updateUser(UserDto userDto) {
        InfoUser queryUser=infoUserDao.findByUserEmail(userDto.getUserEmail());
        if(!Objects.isNull(queryUser)) {//判断用户是否存在，不存在则返回FALSE
            InfoUser infoUser = new InfoUser();
            infoUser.setPkId(queryUser.getPkId());//更新时需获得主键
            BeanUtils.copyProperties(userDto, infoUser);
            if(StringUtils.isBlank(infoUser.getUserHead())){
                infoUser.setUserHead(queryUser.getUserHead());
            }
            if(Objects.isNull(infoUser.getUnCheck())){
                infoUser.setUnCheck(queryUser.getUnCheck());
            }
            if(Objects.isNull(infoUser.getCreateTime())){
                infoUser.setCreateTime(queryUser.getCreateTime());
            }
            if(StringUtils.isBlank(infoUser.getUserName())){
                infoUser.setUserName(queryUser.getUserName());
            }
            if(StringUtils.isBlank(infoUser.getUserName())){
                infoUser.setUserName(queryUser.getUserName());
            }if(StringUtils.isBlank(infoUser.getUserPassword())){
                infoUser.setUserPassword(queryUser.getUserPassword());
            }else {
                String pass= AESUtil.encrypt(infoUser.getUserPassword());
                infoUser.setUserPassword(pass);
            }
            infoUser.setUpdateTime(LocalDateTime.now());
            InfoUser result=infoUserDao.save(infoUser);
            if(!Objects.isNull(result)) {
                /*
                    更新成功后刷新缓存，删除当前缓存中的token
                 */
                redisTemplate.delete(infoUser.getUserEmail() + "_token");
                redisTemplate.opsForValue().set(infoUser.getUserEmail() + "_info", result);
                return true;
            }
            else {
                return false;
            }
        }else {
            return false;
        }
    }

}
