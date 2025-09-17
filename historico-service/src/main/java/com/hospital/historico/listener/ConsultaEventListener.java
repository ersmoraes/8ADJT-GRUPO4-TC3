package com.hospital.historico.listener;

import com.hospital.historico.service.HistoricoService;
import com.hospital.common.events.ConsultaEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEventListener {
    
    @Autowired
    private HistoricoService historicoService;
    
    @RabbitListener(queues = "${rabbitmq.queue.historico}")
    public void processarEventoConsulta(ConsultaEvent evento) {
        try {
            System.out.println("Recebido evento de consulta: " + evento.getTipoEvento() + 
                             " - ID: " + evento.getConsulta().getId());
            
            historicoService.processarEventoConsulta(evento);
            
            System.out.println("Evento processado com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao processar evento de consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }
}