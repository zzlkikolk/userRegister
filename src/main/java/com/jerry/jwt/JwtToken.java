package com.jerry.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现Shiro Token认证接口，让Shiro接受Jwt认证方案
 * @author zhangzhilin
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token){
        this.token=token;
    }
    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
