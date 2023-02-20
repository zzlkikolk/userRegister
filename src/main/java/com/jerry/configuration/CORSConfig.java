package com.jerry.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域处理配置类
 * @author zhangzhilin
 */
@Configuration
public class CORSConfig implements WebMvcConfigurer {
    /**
     * 配置跨域
     * @param registry
     * Origin：必须字段，用于指定请求源。
     * Access-Control-Request-Method：必须字段，用于描述真实请求的方法（PUT、DELETE等）。
     * Access-Control-Request-Headers：指定真实请求会额外发送的请求头字段信息
     * Access-Control-Allow-Credentials: 该字段是可选的。它的值是一个布尔值，表示是否允许发送Cookie。
     * 默认情况下，Cookie不包括在CORS请求之中。设为true，即表示服务器明确许可，Cookie可以包含在请求中，一起发给服务器。这个值也只能设为true，
     * 如果服务器不要浏览器发送Cookie，删除该字段即可
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//添加通过的mapping
                .allowedOrigins("*")//通过的url
                .allowedHeaders("*")//通过的请求头
                .allowedMethods("*")//通过的请求方法
                .allowCredentials(true);
    }

    /**
     * 配置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}
