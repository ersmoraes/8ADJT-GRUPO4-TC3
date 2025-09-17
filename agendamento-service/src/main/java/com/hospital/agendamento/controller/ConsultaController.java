package com.hospital.agendamento.controller;

import com.hospital.agendamento.dto.ConsultaRequest;
import com.hospital.agendamento.model.Consulta;
import com.hospital.agendamento.model.Usuario;
import com.hospital.agendamento.service.ConsultaService;
import com.hospital.common.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
    
    @Autowired
    private ConsultaService consultaService;
    
    @PostMapping
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public ResponseEntity<?> criarConsulta(@RequestBody ConsultaRequest consultaRequest, 
                                          Authentication authentication) {
        try {
            Consulta consulta = consultaService.criarConsulta(consultaRequest, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar consulta: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public ResponseEntity<?> atualizarConsulta(@PathVariable Long id,
                                              @RequestBody ConsultaRequest consultaRequest,
                                              Authentication authentication) {
        try {
            Consulta consulta = consultaService.atualizarConsulta(id, consultaRequest, authentication.getName());
            return ResponseEntity.ok(consulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar consulta: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Consulta>> listarConsultas(Authentication authentication) {
        try {
            List<Consulta> consultas = consultaService.listarConsultasPorUsuario(authentication.getName());
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obterConsulta(@PathVariable Long id, Authentication authentication) {
        try {
            Consulta consulta = consultaService.obterConsultaPorId(id, authentication.getName());
            return ResponseEntity.ok(consulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao obter consulta: " + e.getMessage());
        }
    }
    
    @GetMapping("/futuras")
    public ResponseEntity<List<Consulta>> listarConsultasFuturas(Authentication authentication) {
        try {
            List<Consulta> consultas = consultaService.listarConsultasFuturas(authentication.getName());
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ENFERMEIRO')")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long id, Authentication authentication) {
        try {
            consultaService.cancelarConsulta(id, authentication.getName());
            return ResponseEntity.ok().body("Consulta cancelada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cancelar consulta: " + e.getMessage());
        }
    }
}