package com.hospital.historico.listener;

import com.hospital.common.events.ConsultaEvent;
import com.hospital.historico.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsultaEventListener {

    private final HistoricoService historicoService;

    @RabbitListener(queues = "${rabbitmq.queue.historico}")
    public void processarEventoConsulta(ConsultaEvent evento) {
        try {
            log.info("Recebido evento de consulta: {} - ID: {}", evento.getTipoEvento(), evento.getConsulta().getId());

            historicoService.processarEventoConsulta(evento);

            log.info("Evento processado com sucesso");
        } catch (Exception e) {
            log.error("Erro ao processar evento de consulta: {}", e.getMessage());
        }
    }
}