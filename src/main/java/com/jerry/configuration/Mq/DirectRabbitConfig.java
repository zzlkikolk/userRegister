package com.jerry.configuration.Mq;

import com.jerry.base.Contact;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitConfig {

    //Direct交换机 起名：DirectExchange
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange",true,false);
    }

    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange",true,false);
    }

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange("topicExchange",true,false);
    }

    @Bean
    Queue DirectQuery(){
        return new Queue(Contact.DIRECT_QUERY);
    }

    @Bean
    Queue FanoutQuery(){
        return new Queue(Contact.FANOUT_QUERY);
    }

    @Bean
    Queue TopicQueryOne(){
        return new Queue(Contact.TOPIC_QUERY_ONE);
    }

    @Bean
    Queue TopicQueryALL(){
        return new Queue(Contact.TOPIC_QUERY_ALL);
    }

    @Bean
    Binding bindDirect(){
        return BindingBuilder.bind(DirectQuery()).to(directExchange()).with(Contact.DIRECT_QUERY_KEY);
    }
    @Bean
    Binding bindFanout(){
        return BindingBuilder.bind(FanoutQuery()).to(fanoutExchange());
    }

    @Bean
    Binding bindTopicOne(){
        return  BindingBuilder.bind(TopicQueryOne()).to(topicExchange()).with(Contact.TOPIC_QUERY_ONE_KEY);
    }
    @Bean
    Binding bindTopicALL(){
        return  BindingBuilder.bind(TopicQueryALL()).to(topicExchange()).with(Contact.TOPIC_QUERY_ALL_KEY);
    }

}
