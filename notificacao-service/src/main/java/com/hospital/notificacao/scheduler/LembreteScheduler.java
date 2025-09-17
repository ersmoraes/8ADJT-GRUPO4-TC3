package com.hospital.notificacao.scheduler;

import com.hospital.notificacao.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LembreteScheduler {
    
    @Autowired
    private NotificacaoService notificacaoService;
    
    // Executar todos os dias às 09:00
    @Scheduled(cron = "0 0 9 * * *")
    public void enviarLembretes() {
        System.out.println("Iniciando processamento de lembretes diários...");
        notificacaoService.processarLembretes();
    }
    
    // Reprocessar notificações pendentes a cada 30 minutos
    @Scheduled(fixedRate = 1800000) // 30 minutos = 30 * 60 * 1000 ms
    public void reprocessarNotificacoesPendentes() {
        System.out.println("Reprocessando notificações pendentes...");
        notificacaoService.reprocessarNotificacoesPendentes();
    }
}
