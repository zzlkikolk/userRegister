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
    private String userPassword;

    /** 邮箱验证码**/
    private String emailCode;

    /** 验证码类型，注册验证码，重置密码验证码，注销验证码**/
    private String codeType;

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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", emailCode='" + emailCode + '\'' +
                ", codeType='" + codeType + '\'' +
                '}';
    }
}
