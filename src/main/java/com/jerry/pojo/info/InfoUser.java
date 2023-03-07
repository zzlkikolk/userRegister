package com.jerry.pojo.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息
 * @author zhangzhilin
 */
@Entity
@Table(name="info_user")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })//把无需转json的字段列出来
public class InfoUser implements Serializable {
    private static final long serialVersionUID = -6823085606836498661L;
    /**
     * -AUTO主键由程序控制, 是默认选项 ,不设置就是这个
     *
     * -IDENTITY 主键由数据库生成, 采用数据库自增长, Oracle不支持这种方式
     *
     * -SEQUENCE 通过数据库的序列产生主键, MYSQL  不支持
     *
     * -Table 提供特定的数据库产生主键, 该方式更有利于数据库的移植
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id")
    private Integer pkId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_head")
    private String userHead;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "un_check")
    private Integer unCheck;


    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUnCheck() {
        return unCheck;
    }

    public void setUnCheck(Integer unCheck) {
        this.unCheck = unCheck;
    }

    @Override
    public String toString() {
        return "infoUser{" +
                "pkId=" + pkId +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", userHead='" + userHead + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", unCheck=" + unCheck +
                '}';
    }
}
