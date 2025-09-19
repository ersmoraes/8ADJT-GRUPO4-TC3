package com.hospital.agendamento.service;

import com.hospital.common.events.ConsultaEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.consultas}")
    private String consultasExchange;
    
    @Value("${rabbitmq.routing.key.notificacao}")
    private String notificacaoRoutingKey;
    
    public void enviarEventoConsulta(ConsultaEvent evento) {
        try {
            rabbitTemplate.convertAndSend(consultasExchange, notificacaoRoutingKey, evento);
            System.out.println("Evento enviado: " + evento.getTipoEvento() + " - Consulta ID: " + evento.getConsulta().getId());
        } catch (Exception e) {
            System.err.println("Erro ao enviar evento para RabbitMQ: " + e.getMessage());
        }
    }
}