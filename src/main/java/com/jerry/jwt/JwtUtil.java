package com.jerry.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 生产/解析Jwt工具
 * @author zhangzhilin
 */
@ConfigurationProperties(prefix = "config.jwt")
@Component
public class JwtUtil {

    //加密秘钥
    private String secret;
    //失效时长
    private long expire;

    public String createToken(String userPassword,String userAccount){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);//过期时间
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userAccount", userAccount);
        return builder.withExpiresAt(expireDate).sign(Algorithm.HMAC256(userPassword+secret));
    }

    /**
     * 验证token合法性 成功返回token
     */
    public  boolean verify(String token, String account, String password)  {
        try {
            JWTVerifier build = JWT.require(Algorithm.HMAC256(password)).withClaim("userAccount",account).build();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserAccount(String token){
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("userAccount").asString();
        }catch (Exception e){
            return null;
        }
    }
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
