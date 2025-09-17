package com.hospital.agendamento.dto;

import com.hospital.common.enums.StatusConsulta;
import java.time.LocalDateTime;

public class ConsultaRequest {
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime dataHora;
    private String observacoes;
    private StatusConsulta status;
    
    // Construtores
    public ConsultaRequest() {}
    
    public ConsultaRequest(Long pacienteId, Long medicoId, LocalDateTime dataHora, String observacoes) {
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = StatusConsulta.AGENDADA;
    }
}