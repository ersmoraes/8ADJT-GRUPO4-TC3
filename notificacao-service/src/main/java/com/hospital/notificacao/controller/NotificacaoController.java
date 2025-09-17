package com.hospital.notificacao.controller;

import com.hospital.notificacao.model.Notificacao;
import com.hospital.notificacao.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {
    
    @Autowired
    private NotificacaoService notificacaoService;
    
    @GetMapping("/pendentes")
    public ResponseEntity<List<Notificacao>> listarPendentes() {
        List<Notificacao> pendentes = notificacaoService.listarNotificacoesPendentes();
        return ResponseEntity.ok(pendentes);
    }
    
    @PostMapping("/reprocessar")
    public ResponseEntity<String> reprocessarPendentes() {
        notificacaoService.reprocessarNotificacoesPendentes();
        return ResponseEntity.ok("Notificações pendentes reprocessadas");
    }
    
    @PostMapping("/lembretes")
    public ResponseEntity<String> enviarLembretes() {
        notificacaoService.processarLembretes();
        return ResponseEntity.ok("Lembretes processados");
    }
}