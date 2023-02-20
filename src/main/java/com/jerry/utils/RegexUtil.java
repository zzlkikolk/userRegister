package com.jerry.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类
 * @author zhangzhilin
 */
@Component
public class RegexUtil {
    /** 邮箱正则表达式**/
    @Value("${regex.mail}")
    private String mailRegex;

    /**
     * 邮箱正则匹配
     * @param email 邮箱地址
     * @return
     */
    public  boolean checkEmail(String email){
        Pattern r = Pattern.compile(mailRegex);
        Matcher m = r.matcher(email);
        return m.matches();
    }
}
