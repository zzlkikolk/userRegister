package com.jerry.jwt;

import com.jerry.base.Contact;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 配置过滤器
 * @author zhangzhilin
 */
public class JwtFilter extends AuthenticatingFilter {

    /** 日志 **/
    private static final Logger log= LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            log.error("token为空");
            return null;
        }
        return new JwtToken(token);
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest servletRequest=(HttpServletRequest)request;
        String url=servletRequest.getRequestURI();
        AtomicBoolean check= new AtomicBoolean(false);
        Contact.SET.forEach(u->{
            if(url!=null&&u.equals(url)){
                check.set(true);
            }
        });
        if(check.get())
            return true;
        try {
            return executeLogin(request,response);
        } catch (Exception e) {
            log.error("执行登录错误");
            return false;
        }
    }
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = this.createToken(request, response);
        if (token == null) {
            log.error("token为空,跳转至401,出错位置-->"+JwtFilter.class);
            response401(response);
            return false;
        } else {
            try {
                Subject subject = this.getSubject(request, response);
                subject.login(token);
                return this.onLoginSuccess(token, subject, request, response);
            } catch (AuthenticationException var5) {
                log.error("登录失败，跳转至登出");
                unCheck(response);
                return false;
            }
        }
    }

    //请求失败
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/auth/401");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void unCheck(ServletResponse response){
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendRedirect("/auth/unCheck");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
