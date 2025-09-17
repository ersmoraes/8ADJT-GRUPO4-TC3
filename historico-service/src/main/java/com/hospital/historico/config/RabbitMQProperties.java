package com.hospital.historico.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Classe para mapear propriedades de configuração do RabbitMQ
 * Facilita a manutenção e validação de configurações
 */
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {
    
    private Exchange exchange = new Exchange();
    private Queue queue = new Queue();
    private RoutingKey routingKey = new RoutingKey();
    
    // Getters e Setters
    public Exchange getExchange() { return exchange; }
    public void setExchange(Exchange exchange) { this.exchange = exchange; }
    
    public Queue getQueue() { return queue; }
    public void setQueue(Queue queue) { this.queue = queue; }
    
    public RoutingKey getRoutingKey() { return routingKey; }
    public void setRoutingKey(RoutingKey routingKey) { this.routingKey = routingKey; }
    
    public static class Exchange {
        private String consultas;
        
        public String getConsultas() { return consultas; }
        public void setConsultas(String consultas) { this.consultas = consultas; }
    }
    
    public static class Queue {
        private String historico;
        
        public String getHistorico() { return historico; }
        public void setHistorico(String historico) { this.historico = historico; }
    }
    
    public static class RoutingKey {
        private Key key = new Key();
        
        public Key getKey() { return key; }
        public void setKey(Key key) { this.key = key; }
        
        public static class Key {
            private String historico;
            
            public String getHistorico() { return historico; }
            public void setHistorico(String historico) { this.historico = historico; }
        }
    }
}