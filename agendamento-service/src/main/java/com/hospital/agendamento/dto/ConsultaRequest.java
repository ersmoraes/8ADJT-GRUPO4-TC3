package com.hospital.agendamento.dto;

import com.hospital.common.enums.StatusConsulta;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConsultaRequest {
    private Long pacienteId;
    private Long medicoId;
    private String medicoNome;
    private LocalDateTime dataHora;
    private String observacoes;
    private StatusConsulta status;
    
    // Construtores
    public ConsultaRequest() {}
    
    public ConsultaRequest(Long pacienteId, Long medicoId, String medicoNome, LocalDateTime dataHora, String observacoes) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.medicoNome = medicoNome;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = StatusConsulta.AGENDADA;
    }
}