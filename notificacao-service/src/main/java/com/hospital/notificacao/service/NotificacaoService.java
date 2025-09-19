package com.hospital.notificacao.service;

import com.hospital.common.events.ConsultaEvent;
import com.hospital.notificacao.model.Notificacao;
import com.hospital.notificacao.repository.NotificacaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
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
                log.info("Notificação já existe para consulta {} e evento {}", evento.getConsulta().getId(),
                        evento.getTipoEvento());
                return;
            }

            notificacao = notificacaoRepository.save(notificacao);

            // Enviar email imediatamente
            enviarEmailNotificacao(notificacao);

        } catch (Exception e) {
            log.error("Erro ao processar evento de consulta: {}", e.getMessage());
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

            log.error("Erro ao enviar email para notificação ID: {}", notificacao.getId());
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
                log.info("Lembrete enviado para consulta ID: {}", notificacao.getConsultaId());
            } catch (Exception e) {
                log.error("Erro ao enviar lembrete para consulta ID: {} - {}", notificacao.getConsultaId(),
                        e.getMessage());
            }
        }

        log.info("Processamento de lembretes concluído. Enviados: {} lembretes.", notificacoesParaLembrete.size());
    }

    public List<Notificacao> listarNotificacoesPendentes() {
        return notificacaoRepository.findByEmailEnviadoFalse();
    }

    public void reprocessarNotificacoesPendentes() {
        List<Notificacao> pendentes = listarNotificacoesPendentes();

        for (Notificacao notificacao : pendentes) {
            enviarEmailNotificacao(notificacao);
        }

        log.info("Reprocessamento de notificações pendentes concluído. Processadas: {} notificações.",
                pendentes.size());
    }
}