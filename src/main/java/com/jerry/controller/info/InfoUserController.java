package com.jerry.controller.info;

import com.jerry.base.NoAuth;
import com.jerry.base.Result;
import com.jerry.pojo.Dto.UserDto;
import com.jerry.pojo.info.InfoUser;
import com.jerry.service.info.InfoUserService;
import com.jerry.utils.RegexUtil;
import com.jerry.utils.SendMailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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


//    @GetMapping("/userInfo/{id}")
//    public Result<InfoUser> getUserInfo(@PathVariable("id") Integer id){
//
//        InfoUser user=infoUserService.getOne(id);
//        return new Result<InfoUser>().success().put(user);
//    }

    @GetMapping("/userInfo/{email}")
    public Result<?> getUserInfo(@PathVariable("email") String email){
        InfoUser user= infoUserService.getUser(email);
        if(user!=null){
            user.setUserPassword(null);
            return new Result<>().success().put(user);
        }else {
             return new Result<>().success("未找到");
        }
    }

    @PostMapping("/save")
    public Result<?> save(@RequestBody InfoUser user){
        user=infoUserService.save(user);
        if(StringUtils.isEmpty(user.getPkId())){
            return new Result<>().error(500,"保存失败");
        }else {
            return new Result<>().success();
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
            if(isEmail){
                boolean sendSuccessfully=sendMailUtil.sendCode(user.getUserEmail());//给用户发送验证码
                if(sendSuccessfully){
                    return new Result<>().success();
                }else{
                    return new Result<>().error("发送失败，稍后再试");
                }
            }else {
                return new Result<>().error("邮箱格式不正确");
            }
        }else {
            return new Result<>().error("邮箱为空");
        }
    }


}
