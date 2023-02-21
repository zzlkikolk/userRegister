package com.jerry.controller.auth;

import com.jerry.base.Result;
import com.jerry.dao.info.InfoUserDao;
import com.jerry.jwt.JwtUtil;
import com.jerry.pojo.info.InfoUser;
import com.jerry.utils.AESUtil;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 认证、跳转控制器
 * @author zhangzhilin
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private InfoUserDao infoUserDao;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 未授权重定向地址
     * @return
     */
    @RequestMapping("/401")
    public Result<?> response401(){
        return new Result<>().error(403,"没有访问权限");
    }

    /**
     * token失效重定向地址
     * @return
     */
    @RequestMapping("/unCheck")
    public Result<?> unCheck(){
        return new Result<>().error(401,"Token过期，请重新登陆");
    }


    /**
     * 用户登录
     * @param user 登录参数
     * @return 携带token的结果集
     */
    @PostMapping("/login")
    public Result<?> Login(@RequestBody InfoUser user){
        String passWord= AESUtil.encrypt(user.getUserPassword());
        InfoUser dataBaseUser=infoUserDao.findByUserEmail(user.getUserEmail());
        if(dataBaseUser!=null){
            if(passWord.equals(dataBaseUser.getUserPassword())){
                String token= jwtUtil.createToken(dataBaseUser.getUserPassword(), dataBaseUser.getUserEmail());
                String token_key=user.getUserEmail()+"_token";
                redisTemplate.opsForValue().set(token_key,token);//存放当前token到Redis
                redisTemplate.expire(token_key,30, TimeUnit.MINUTES);//设置过期时间
                String user_key=user.getUserEmail()+"_info";
                redisTemplate.opsForValue().set(user_key,dataBaseUser);//存入用户信息到Redis
                redisTemplate.expire(user_key,30,TimeUnit.MINUTES);//设置过期时间
                return new Result<>().success().put(token);
            }else {
                return new Result<>().error("登录失败，请检查账号密码");
            }
        }else {
            return new Result<>().error(400,"账号未注册");
        }
    }

    /**
     * 用户登出
     * @return 结果集
     */
    @GetMapping("/Logout")
    public Result<?> userLogout() {
        InfoUser user = (InfoUser) SecurityUtils.getSubject().getPrincipal();
        String cacheToken = (String) redisTemplate.opsForValue().get(user.getUserEmail() + "_token");
        if (cacheToken != null) {
            redisTemplate.delete(user.getUserEmail() + "_token");
            redisTemplate.delete(user.getUserEmail()+"_info");
        }
        return new Result<>().success(200,"您已登出");
    }
}
