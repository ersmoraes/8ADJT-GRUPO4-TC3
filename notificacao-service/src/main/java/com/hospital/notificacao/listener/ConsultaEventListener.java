package com.hospital.notificacao.listener;

import com.hospital.notificacao.service.NotificacaoService;
import com.hospital.common.events.ConsultaEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventListener {
    
    @Autowired
    private NotificacaoService notificacaoService;
    
    @RabbitListener(queues = "${rabbitmq.queue.notificacao}")
    public void processarEventoConsulta(ConsultaEvent evento) {
        try {
            System.out.println("Recebido evento de consulta para notificação: " + evento.getTipoEvento() + 
                             " - ID: " + evento.getConsulta().getId() + 
                             " - Paciente: " + evento.getPacienteNome());
            
            notificacaoService.processarEventoConsulta(evento);
            
            System.out.println("Notificação processada com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao processar evento para notificação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}