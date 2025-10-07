package com.hospital.notificacao.service;

import com.hospital.notificacao.model.Notificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public void enviarNotificacaoConsulta(Notificacao notificacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notificacao.getPacienteEmail());
            message.setSubject(getAssuntoEmail(notificacao.getTipoEvento()));
            message.setText(getCorpoEmail(notificacao));
            message.setFrom(remetente);

            mailSender.send(message);
            
            System.out.println("Email enviado com sucesso para: " + notificacao.getPacienteEmail());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email: " + e.getMessage());
            throw new RuntimeException("Falha no envio do email", e);
        }
    }
    
    public void enviarLembreteConsulta(Notificacao notificacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(notificacao.getPacienteEmail());
            message.setSubject("Lembrete: Consulta médica agendada");
            message.setText(getCorpoLembrete(notificacao));

            mailSender.send(message);
            
            System.out.println("Lembrete enviado com sucesso para: " + notificacao.getPacienteEmail());
        } catch (Exception e) {
            System.err.println("Erro ao enviar lembrete: " + e.getMessage());
            throw new RuntimeException("Falha no envio do lembrete", e);
        }
    }
    
    private String getAssuntoEmail(String tipoEvento) {
        switch (tipoEvento) {
            case "CRIADA":
                return "Consulta médica agendada - Hospital System";
            case "EDITADA":
                return "Consulta médica reagendada - Hospital System";
            case "CANCELADA":
                return "Consulta médica cancelada - Hospital System";
            default:
                return "Atualização de consulta médica - Hospital System";
        }
    }
    
    private String getCorpoEmail(Notificacao notificacao) {
        StringBuilder corpo = new StringBuilder();
        corpo.append("Olá, ").append(notificacao.getPacienteNome()).append("!\n\n");
        
        switch (notificacao.getTipoEvento()) {
            case "CRIADA":
                corpo.append("Sua consulta foi agendada com sucesso!\n\n");
                break;
            case "EDITADA":
                corpo.append("Sua consulta foi reagendada!\n\n");
                break;
            case "CANCELADA":
                corpo.append("Sua consulta foi cancelada.\n\n");
                break;
        }
        
        if (!notificacao.getTipoEvento().equals("CANCELADA")) {
            corpo.append("Detalhes da consulta:\n");
            corpo.append("Data e Hora: ").append(notificacao.getDataConsulta().format(FORMATTER)).append("\n");
            corpo.append("Status: ").append(notificacao.getStatusConsulta()).append("\n\n");
            corpo.append("Por favor, chegue com 15 minutos de antecedência.\n\n");
        }
        
        corpo.append("Em caso de dúvidas, entre em contato conosco.\n\n");
        corpo.append("Atenciosamente,\n");
        corpo.append("Hospital System");
        
        return corpo.toString();
    }
    
    private String getCorpoLembrete(Notificacao notificacao) {
        StringBuilder corpo = new StringBuilder();
        corpo.append("Olá, ").append(notificacao.getPacienteNome()).append("!\n\n");
        corpo.append("Este é um lembrete sobre sua consulta médica:\n\n");
        corpo.append("Data e Hora: ").append(notificacao.getDataConsulta().format(FORMATTER)).append("\n\n");
        corpo.append("Por favor, não se esqueça de comparecer à consulta.\n");
        corpo.append("Chegue com 15 minutos de antecedência.\n\n");
        corpo.append("Atenciosamente,\n");
        corpo.append("Hospital System");
        
        return corpo.toString();
    }
}