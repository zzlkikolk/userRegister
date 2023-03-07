package com.jerry.controller.mq;

import com.jerry.base.Contact;
import com.jerry.base.NoAuth;
import com.jerry.base.RabbitMessage;
import com.jerry.base.Result;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/sendMessage")
public class MqController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @NoAuth
    @GetMapping("/send")
    public Result<?> send(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return new Result<>().success();
    }

    @NoAuth
    @RabbitMessage(routerKey = Contact.DIRECT_QUERY_KEY,exchangeType = DirectExchange.class,queryName = Contact.DIRECT_QUERY_KEY)
    @GetMapping("/send1")
    public Result<?> send1(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        return new Result<>().success().put(map);
    }

    public static void main(String[] args) {
        String str="createTim1e";
        int hash=str.hashCode();
        int x=hash>>>16;
        int y=hash ^ x;
        int i=15 & y;
        System.out.println(i);
    }
}
