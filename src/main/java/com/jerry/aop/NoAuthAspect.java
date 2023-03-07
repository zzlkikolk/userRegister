package com.jerry.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Method;


/**
 * 忽略鉴权切面
 */
@Aspect
@Component
@Slf4j
public class NoAuthAspect  {



    /** 切入点**/
    @Pointcut(value = "@annotation(com.jerry.base.NoAuth)")
    public void NoAuthAspect(){

    }

    /** 环绕通知 **/
    @Around("NoAuthAspect()") 
    public Object getMethodUrl(ProceedingJoinPoint point) throws Throwable{
        /*
                获取前缀
                签名：  包名+类名 +方法名
         */
        MethodSignature methodSignature= (MethodSignature) point.getSignature();
        String url=getMethodUrl(methodSignature);
        return point.proceed();
    }

    private String getMethodUrl(MethodSignature methodSignature){
        /*获取方法*/
        Method method=methodSignature.getMethod();
        //获取类名
        String controllerName=method.getDeclaringClass().getName();
        try {
            Class controllerClass=Class.forName(controllerName);
            RequestMapping conRequest= (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
            StringBuilder builder=new StringBuilder(conRequest.value()[0]);
            /*拼接映射*/
            if(method.isAnnotationPresent(PostMapping.class)){
                PostMapping annotation=method.getAnnotation(PostMapping.class);
                builder.append(annotation.value()[0]);
            }else if(method.isAnnotationPresent(GetMapping.class)){//path参数还未测试
                GetMapping annotation=method.getAnnotation(GetMapping.class);
                builder.append(annotation.value()[0]);
            }else if(method.isAnnotationPresent(PutMapping.class)){
                PutMapping annotation=method.getAnnotation(PutMapping.class);
                builder.append(annotation.value()[0]);
            }else if(method.isAnnotationPresent(DeleteMapping.class)){
                DeleteMapping annotation=method.getAnnotation(DeleteMapping.class);
                builder.append(annotation.value()[0]);
            }else if(method.isAnnotationPresent(PatchMapping.class)){
                PatchMapping annotation=method.getAnnotation(PatchMapping.class);
                builder.append(annotation.value()[0]);
            }else if(method.isAnnotationPresent(RequestMapping.class)){
                RequestMapping annotation=method.getAnnotation(RequestMapping.class);
                builder.append(annotation.value()[0]);
            }
            log.info("忽略鉴权url-------->"+builder.toString());
            return builder.toString();
        } catch (ClassNotFoundException e) {
            log.error("获取url失败"+NoAuthAspect.class);
            return null;
        }

    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext=applicationContext;
//    }
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//
//        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
//    }
}
