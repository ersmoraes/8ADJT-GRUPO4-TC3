package com.hospital.historico.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Health Check personalizado para monitorar a conexão com RabbitMQ
 * Útil para monitoramento em produção
 */
@Component
public class RabbitMQHealthIndicator implements HealthIndicator {
    
    private final RabbitTemplate rabbitTemplate;
    
    public RabbitMQHealthIndicator(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Override
    public Health health() {
        try {
            // Tenta verificar se a conexão está ativa
            rabbitTemplate.getConnectionFactory().createConnection().isOpen();
            return Health.up()
                    .withDetail("rabbitmq", "Conexão ativa")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("rabbitmq", "Falha na conexão: " + e.getMessage())
                    .build();
        }
    }
}