package com.jerry.aop;

import com.jerry.base.RabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Mq注解切面
 */
@Aspect
@Slf4j
@Component
public class RabbitAspect implements ApplicationContextAware {

    private  ApplicationContext application;
    @Resource
    private RabbitTemplate rabbitTemplate;
    /*
        切入点
     */
    @Pointcut(value = "@annotation(com.jerry.base.RabbitMessage)")
    public void RabbitAspect(){

    }
    /** 环绕通知 **/
    @Around("RabbitAspect()")
    public Object getMethodResult(ProceedingJoinPoint point) throws Throwable{
        /*
                获取前缀
                签名：  包名+类名 +方法名
         */
        MethodSignature methodSignature= (MethodSignature) point.getSignature();

        return point.proceed();
    }

    /** 后置通知,获取返回值**/
    @AfterReturning(value = "RabbitAspect()",returning = "result")
    public void getResult(JoinPoint point, Object result) throws Exception{
            /*
                获取前缀
                签名：  包名+类名 +方法名
         */
        MethodSignature methodSignature= (MethodSignature) point.getSignature();
        Method method=methodSignature.getMethod();
        RabbitMessage rabbitAnnotation=method.getAnnotation(RabbitMessage.class);//获取注解信息
        sendMessage(rabbitAnnotation,result);
        log.info("方法返回值--------->"+result.toString());
    }
    private void sendMessage(RabbitMessage rabbitMessage,Object result) throws Exception {
        //路由键
        String routerKey=rabbitMessage.routerKey();
        //路由类型
        Class exchangeClazz=rabbitMessage.exchangeType();
        String queryName=rabbitMessage.queryName();
        /*
            直接路由
         */
        if(DirectExchange.class==exchangeClazz){
            DirectExchange directExchange=application.getBean(DirectExchange.class);
            rabbitTemplate.convertAndSend(directExchange.getName(),routerKey,result);
        }/*
               广播路由
            */
        else if(FanoutExchange.class==exchangeClazz){
            FanoutExchange fanoutExchange=application.getBean(FanoutExchange.class);
            rabbitTemplate.convertAndSend(fanoutExchange.getName(),result);
        }
        /*
            匹配路由
         */
        else if(TopicExchange.class==exchangeClazz){
            TopicExchange topicExchange=application.getBean(TopicExchange.class);
            Queue queue=new Queue(queryName,true);
            Binding binding=BindingBuilder.bind(queue).to(topicExchange).with(routerKey);

            rabbitTemplate.convertAndSend(topicExchange.getName(),routerKey,result);
        }else {
            throw new Exception("消息类型错误");
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.application=applicationContext;
    }

}
