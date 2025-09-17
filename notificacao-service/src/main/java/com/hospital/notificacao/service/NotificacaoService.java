package com.hospital.notificacao.service;

import com.hospital.notificacao.model.Notificacao;
import com.hospital.notificacao.repository.NotificacaoRepository;
import com.hospital.common.events.ConsultaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificacaoService {
    
    @Autowired
    private NotificacaoRepository notificacaoRepository;
    
    @Autowired
    private EmailService emailService;
    
    public void processarEventoConsulta(ConsultaEvent evento) {
        try {
            // Criar registro de notificação
            Notificacao notificacao = new Notificacao(
                evento.getConsulta().getId(),
                evento.getConsulta().getPacienteId(),
                evento.getPacienteNome(),
                evento.getPacienteEmail(),
                evento.getConsulta().getDataHora(),
                evento.getTipoEvento()
            );
            
            notificacao.setStatusConsulta(evento.getConsulta().getStatus());
            
            // Verificar se já existe notificação para este evento
            var notificacaoExistente = notificacaoRepository
                .findByConsultaIdAndTipoEvento(evento.getConsulta().getId(), evento.getTipoEvento());
                
            if (notificacaoExistente.isPresent()) {
                System.out.println("Notificação já existe para consulta " + evento.getConsulta().getId() + 
                                 " e evento " + evento.getTipoEvento());
                return;
            }
            
            notificacao = notificacaoRepository.save(notificacao);
            
            // Enviar email imediatamente
            enviarEmailNotificacao(notificacao);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar evento de consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void enviarEmailNotificacao(Notificacao notificacao) {
        try {
            emailService.enviarNotificacaoConsulta(notificacao);
            
            notificacao.setEmailEnviado(true);
            notificacao.setDataEnvioEmail(LocalDateTime.now());
            notificacao.setMensagemErro(null);
            
            notificacaoRepository.save(notificacao);
            
        } catch (Exception e) {
            notificacao.setEmailEnviado(false);
            notificacao.setMensagemErro(e.getMessage());
            
            notificacaoRepository.save(notificacao);
            
            System.err.println("Erro ao enviar email para notificação ID: " + notificacao.getId());
        }
    }
    
    public void processarLembretes() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime amanha = agora.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime doisDias = amanha.plusDays(1);
        
        List<Notificacao> notificacoesParaLembrete = notificacaoRepository
            .findNotificacoesParaLembrete(amanha, doisDias);
            
        for (Notificacao notificacao : notificacoesParaLembrete) {
            try {
                emailService.enviarLembreteConsulta(notificacao);
                System.out.println("Lembrete enviado para consulta ID: " + notificacao.getConsultaId());
            } catch (Exception e) {
                System.err.println("Erro ao enviar lembrete para consulta ID: " + 
                                 notificacao.getConsultaId() + " - " + e.getMessage());
            }
        }
        
        System.out.println("Processamento de lembretes concluído. Enviados: " + 
                         notificacoesParaLembrete.size() + " lembretes.");
    }
    
    public List<Notificacao> listarNotificacoesPendentes() {
        return notificacaoRepository.findByEmailEnviadoFalse();
    }
    
    public void reprocessarNotificacoesPendentes() {
        List<Notificacao> pendentes = listarNotificacoesPendentes();
        
        for (Notificacao notificacao : pendentes) {
            enviarEmailNotificacao(notificacao);
        }
        
        System.out.println("Reprocessamento de notificações pendentes concluído. " + 
                         "Processadas: " + pendentes.size() + " notificações.");
    }
}