package com.jerry.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 邮件发送工具类
 * @author zhangzhilin
 */
@Component
public class SendMailUtil {

    /**日志**/
    private final static Logger log= LoggerFactory.getLogger(SendMailUtil.class);

    @Resource(type = JavaMailSenderImpl.class)
    private JavaMailSender sender;

    /** 发件人邮箱*/
    @Value("${spring.mail.username}")
    private String userEmail;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发送邮件验证码
     * @param email 收件人地址
     * @return
     */
    public boolean sendCode(String email,String type){
        String code=randomCode();
        if(!Objects.isNull(type)) {
            try {
                log.info("发送验证码邮件中");
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setSubject("【测试验证码】验证消息");
                mailMessage.setText("验证码为:" + code + ",有效期5分钟,请勿泄露给他人");
                mailMessage.setTo(email);
                mailMessage.setFrom("577435092@qq.com");
                sender.send(mailMessage);
                StringBuilder builder=new StringBuilder();
                builder.append(email);
                builder.append("_");
                builder.append(type);
                builder.append("_code");
                redisTemplate.opsForValue().set(builder.toString(), code);//加入Redis中
                redisTemplate.expire(builder.toString(), 5, TimeUnit.MINUTES);//设置验证码过期时间5分钟
                log.info("发送验证码成功");
                return true;
            } catch (MailException e) {
                log.error("验证码邮件发送失败");
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 生成随机验证码 6位数
     * @return 验证码
     */
    private String randomCode(){
        String sources="0123456789";
        int size=6;
        int codeLen=sources.length();
        Random rand=new Random(System.currentTimeMillis());
        StringBuilder ranCode=new StringBuilder(size);
        for(int i=0;i<size;i++){
            ranCode.append(sources.charAt(rand.nextInt(codeLen-1)));
        }
        return ranCode.toString();
    }
}
