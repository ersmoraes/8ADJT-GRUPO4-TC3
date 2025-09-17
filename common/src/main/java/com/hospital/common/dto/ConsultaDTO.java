package com.hospital.common.dto;

import com.hospital.common.enums.StatusConsulta;
import java.time.LocalDateTime;

public class ConsultaDTO {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime dataHora;
    private String observacoes;
    private StatusConsulta status;
    
    // Construtores
    public ConsultaDTO() {}
    
    public ConsultaDTO(Long id, Long pacienteId, Long medicoId, 
                      LocalDateTime dataHora, String observacoes, StatusConsulta status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.status = status;
    }
}