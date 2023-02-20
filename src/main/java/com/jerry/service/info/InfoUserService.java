package com.jerry.service.info;

import com.jerry.base.BaseService;
import com.jerry.pojo.info.InfoUser;

/**
 * 用户业务接口
 * @author zhangzhilin
 */
public interface InfoUserService extends BaseService<InfoUser> {
    InfoUser getUser(String email);
}
