package com.jerry.configuration;

import com.jerry.dao.info.InfoUserDao;
import com.jerry.jwt.JwtToken;
import com.jerry.jwt.JwtUtil;
import com.jerry.pojo.info.InfoUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义Shiro Realm
 * @author zhangzhilin
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private InfoUserDao infoUserDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate redisTemplate;

    /**使realm支持jwt的认证方案
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权（认证通过后给用户授权)
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //暂无角色和权限表，所以手动添加
        Set<String> rolesSet = new HashSet<>();
        rolesSet.add("admin");
        rolesSet.add("user");
        Set<String> powerSet =new HashSet<>();
        powerSet.add("delete_power");
        powerSet.add("look_power");
        powerSet.add("save_power");
        simpleAuthorizationInfo.setStringPermissions(powerSet);
        simpleAuthorizationInfo.setRoles(rolesSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证-认证用户token合法性
     * 步骤
     *  1.先从redis中获取用户token
     *  2.先判断token是否一致
     *  3.一致则通过，若Redis中无数据，则先判断合法性，再添加到Redis中（在token header中携带版本号）
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token=(String) authenticationToken.getCredentials();
        String userAccount=jwtUtil.getUserAccount(token);
        if(userAccount!=null){
            String cacheToken = (String) redisTemplate.opsForValue().get(userAccount + "_token");//读取缓存中的Token
            InfoUser infoUser = null;
            if (cacheToken != null) {
                if (cacheToken.equals(token)) {
                    infoUser = infoUserDao.findByUserEmail(userAccount);
                    if (infoUser == null) {
                        throw new UnknownAccountException("用户信息为空");
                    }
                    if (!jwtUtil.verify(cacheToken, userAccount, infoUser.getUserPassword())) {
                        throw new UnknownAccountException("验证失败");
                    }
                    return new SimpleAuthenticationInfo(infoUser, token, this.getName());//执行登录
                } else {
                    throw new AuthenticationException("登录过期");
                }
            } else {//如果缓存为空
                throw new AuthenticationException("登录过期");
            }
        }else {
            throw  new AuthenticationException("token 有误");
        }
    }
}
