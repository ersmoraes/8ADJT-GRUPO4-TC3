package com.hospital.historico.model;

import com.hospital.common.enums.StatusConsulta;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "historico_consultas")
public class HistoricoConsulta {
    @Id
    private Long consultaId; // Mesmo ID da consulta original
    
    @Column(nullable = false)
    private Long pacienteId;
    
    @Column(nullable = false)
    private Long medicoId;
    
    @Column(nullable = false)
    private String pacienteNome;
    
    @Column(nullable = false)
    private String medicoNome;
    
    @Column(nullable = false)
    private String pacienteEmail;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Column(length = 1000)
    private String observacoes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status;
    
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao = LocalDateTime.now();
    
    // Construtores
    public HistoricoConsulta() {}
    
    public HistoricoConsulta(Long consultaId, Long pacienteId, Long medicoId, 
                            String pacienteNome, String medicoNome, String pacienteEmail,
                            LocalDateTime dataHora, String observacoes, StatusConsulta status) {
        this.consultaId = consultaId;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
        this.pacienteEmail = pacienteEmail;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = status;
    }
}