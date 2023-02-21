package com.jerry.controller.info;

import com.jerry.base.Contact;
import com.jerry.base.NoAuth;
import com.jerry.base.Result;
import com.jerry.pojo.Dto.UserDto;
import com.jerry.pojo.info.InfoUser;
import com.jerry.service.info.InfoUserService;
import com.jerry.utils.AESUtil;
import com.jerry.utils.RegexUtil;
import com.jerry.utils.SendMailUtil;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户信息控制器
 * @author zhangzhilin
 */
@RestController
@RequestMapping("/user")
public class InfoUserController {

    @Resource
    private RegexUtil regexUtil;

    @Resource(name="infoUserService")
    private InfoUserService infoUserService;

    @Resource
    private SendMailUtil sendMailUtil;

    @Resource
    private RedisTemplate redisTemplate;

    /** 用户信息**/
    @GetMapping("/userInfo/{email}")
    @RequiresAuthentication
    public Result<?> getUserInfo(@PathVariable("email") String email){
        InfoUser user= infoUserService.getUser(email);
        if(user!=null){
            return new Result<>().success().put(user);
        }else {
             return new Result<>().success("未找到");
        }
    }

    /** 当前用户信息**/
    @GetMapping("/userInfo")
    @RequiresAuthentication
    public Result<?> getUserInfo(){
      String uEmail= ( (InfoUser)SecurityUtils.getSubject().getPrincipal()).getUserEmail();
        InfoUser user= infoUserService.getUser(uEmail);
        if(user!=null){
            return new Result<>().success().put(user);
        }else {
            return new Result<>().success("未找到");
        }
    }

    /** 用户注册**/
    @NoAuth
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserDto user){
        if(!StringUtils.isEmpty(user)){
            String cacheCode= (String) redisTemplate.opsForValue().get(user.getUserEmail()+"_REGISTER"+"_code");
            if(cacheCode!=null&&cacheCode.equals(user.getEmailCode())){
                //验证码验证通过后删除缓存
                redisTemplate.delete(user.getUserEmail()+"_REGISTER"+"_code");
              boolean check=infoUserService.saveUser(user);
                if(check){
                    return new Result<>().success(200,"注册成功");
                }else {
                    return new Result<>().error(400,"注册失败");
                }
            }else {
                return new Result<>().error(400,"请输入正确的验证码");
            }
        }else {
            return new Result<>().error(400,"请输入正确参数");
        }
    }

    /** 重置密码**/
    @PostMapping("/reset")
    @RequiresAuthentication
    public Result<?> resetPassword(@RequestBody UserDto user){
        InfoUser infoUser= (InfoUser) SecurityUtils.getSubject().getPrincipal();
        user.setUserEmail(infoUser.getUserEmail());
        if(!StringUtils.isEmpty(user.getUserPassword())){
            String cacheCode= (String) redisTemplate.opsForValue().get(user.getUserEmail()+"_RESET"+"_code");
            if(cacheCode!=null&&cacheCode.equals(user.getEmailCode())){
                //验证码验证通过后删除缓存
                redisTemplate.delete(user.getUserEmail()+"_RESET"+"_code");
                boolean check=infoUserService.updateUser(user);
                if(check){
                    return new Result<>().success(200,"修改成功");
                }else {
                    return new Result<>().error(400,"账号不存在");
                }
            }else {
                return new Result<>().error(400,"请输入正确的验证码");
            }
        }else {
            return new Result<>().error(400,"请输入正确参数");
        }
    }
    /**
     * 邮箱验证码发送
     * @param user 用户注册信息
     * @return 结果集
     */
    @NoAuth
    @PostMapping("/sendCode")
    public Result<?> sendCode(@RequestBody UserDto user){
        if(user.getUserEmail()!=null) {
            boolean isEmail = regexUtil.checkEmail(user.getUserEmail());
            if(isEmail&&!StringUtils.isEmpty(user.getCodeType())){//如果类型为空不发送验证码
                boolean inType=Arrays.asList(Contact.TYPE).contains(user.getCodeType());//判断验证码类型
                boolean sendSuccessfully=false;
                if(inType) {
                    sendSuccessfully = sendMailUtil.sendCode(user.getUserEmail(), user.getCodeType());//给用户发送验证码
                }
                if(sendSuccessfully){
                    return new Result<>().success();
                }else{
                    return new Result<>().error("发送失败，稍后再试");
                }
            }else {
                return new Result<>().error(400,"缺少参数");
            }
        }else {
            return new Result<>().error("邮箱为空");
        }
    }


}
