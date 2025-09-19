package com.hospital.agendamento.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${rabbitmq.exchange.consultas}")
    private String consultasExchange;
    
    @Value("${rabbitmq.queue.notificacao}")
    private String notificacaoQueue;
    
    @Value("${rabbitmq.queue.historico}")
    private String historicoQueue;
    
    @Value("${rabbitmq.routing.key.notificacao}")
    private String notificacaoRoutingKey;
    
    @Value("${rabbitmq.routing.key.historico}")
    private String historicoRoutingKey;
    
    @Bean
    public TopicExchange consultasExchange() {
        return new TopicExchange(consultasExchange);
    }
    
    @Bean
    public Queue notificacaoQueue() {
        return QueueBuilder.durable(notificacaoQueue).build();
    }
    
    @Bean
    public Queue historicoQueue() {
        return QueueBuilder.durable(historicoQueue).build();
    }
    
    @Bean
    public Binding notificacaoBinding() {
        return BindingBuilder
                .bind(notificacaoQueue())
                .to(consultasExchange())
                .with(notificacaoRoutingKey);
    }
    
    @Bean
    public Binding historicoBinding() {
        return BindingBuilder
                .bind(historicoQueue())
                .to(consultasExchange())
                .with(historicoRoutingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}