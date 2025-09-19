package com.hospital.historico.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.consultas}")
    private String consultasExchange;

    @Value("${rabbitmq.queue.historico}")
    private String historicoQueue;
    
    @Value("${rabbitmq.routing.key.historico}")
    private String historicoRoutingKey;

    /**
     * Configura o exchange principal para eventos de consultas
     * Topic Exchange permite roteamento flexível baseado em routing keys
     */
    @Bean
    public TopicExchange consultasExchange() {
        return ExchangeBuilder
                .topicExchange(consultasExchange)
                .durable(true) // Exchange persiste mesmo após restart do RabbitMQ
                .build();
    }

    /**
     * Configura a fila específica para o serviço de histórico
     * Fila durável para garantir que as mensagens não sejam perdidas
     */
    @Bean
    public Queue historicoQueue() {
        return QueueBuilder
                .durable(historicoQueue)
                .withArgument("x-dead-letter-exchange", "") // DLQ para mensagens com falha
                .withArgument("x-dead-letter-routing-key", historicoQueue + ".dlq")
                .build();
    }
    
    /**
     * Configura a Dead Letter Queue para mensagens que falharam no processamento
     */
    @Bean
    public Queue historicoDeadLetterQueue() {
        return QueueBuilder
                .durable(historicoQueue + ".dlq")
                .build();
    }
    
    /**
     * Binding que conecta a fila do histórico ao exchange
     * Todas as mensagens com routing key 'consulta.historico' serão direcionadas para esta fila
     */
    @Bean
    public Binding historicoBinding() {
        return BindingBuilder
                .bind(historicoQueue())
                .to(consultasExchange())
                .with(historicoRoutingKey);
    }
    
    /**
     * Binding adicional para capturar todas as mensagens de consulta
     * Útil para manter histórico completo independente do tipo de evento
     */
    @Bean  
    public Binding historicoAllEventsBinding() {
        return BindingBuilder
                .bind(historicoQueue())
                .to(consultasExchange())
                .with("consulta.*"); // Captura: consulta.notificacao, consulta.historico, etc.
    }
    
    /**
     * Conversor de mensagens para JSON
     * Permite serialização automática de objetos Java para JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Configurar para aceitar todos os tipos de classe (necessário para DTOs)
        converter.setCreateMessageIds(true);
        return converter;
    }
    
    /**
     * Template do RabbitMQ configurado com conversor JSON
     * Usado para enviar mensagens (caso necessário no futuro)
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        
        // Configurações de retry para garantia de entrega
        template.setRetryTemplate(retryTemplate());
        template.setMandatory(true); // Falha se não conseguir rotear a mensagem
        
        return template;
    }
    
    /**
     * Configuração de retry para reenvio automático em caso de falha
     */
    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate =
            new RetryTemplate();
            
        // Política de retry: 3 tentativas com intervalo exponencial
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 1 segundo
        backOffPolicy.setMultiplier(2.0); // Dobra o tempo a cada tentativa
        backOffPolicy.setMaxInterval(10000); // Máximo 10 segundos
        
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        return retryTemplate;
    }
}