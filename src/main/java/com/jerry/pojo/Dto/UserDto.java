package com.jerry.pojo.Dto;

import java.io.Serializable;

/**
 * 基础用户信息Dto
 * @author zhangzhilin
 */
public class UserDto implements Serializable {
    private static final long serialVersionUID = 4456117717233671452L;

    /**用户名 **/
    private String userName;

    /**用户邮箱 **/
    private String userEmail;

    /** 用户密码**/
    private String passWord;

    /** 邮箱验证码**/
    private String emailCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", passWord='" + passWord + '\'' +
                ", emailCode='" + emailCode + '\'' +
                '}';
    }
}
