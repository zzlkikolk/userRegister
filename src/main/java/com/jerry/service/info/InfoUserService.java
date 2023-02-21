package com.jerry.service.info;

import com.jerry.base.BaseService;
import com.jerry.pojo.Dto.UserDto;
import com.jerry.pojo.info.InfoUser;

/**
 * 用户业务接口
 * @author zhangzhilin
 */
public interface InfoUserService extends BaseService<InfoUser> {
    InfoUser getUser(String email);

    /**
     * 创建用户
     * @param userDto 用户信息
     * @return
     */
    boolean saveUser(UserDto userDto);

    boolean updateUser(UserDto userDto);

}
