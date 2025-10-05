package com.hospital.common.events;

import com.hospital.common.dto.ConsultaDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ConsultaEvent {
    private String tipoEvento; // CRIADA, EDITADA, CANCELADA
    private ConsultaDTO consulta;
    private String pacienteEmail;
    private String medicoNome;
    private String pacienteNome;
    private LocalDateTime timestampEvento;
    
    // Construtores
    public ConsultaEvent() {}

    public ConsultaEvent(String tipoEvento, ConsultaDTO consulta, String email, String medicoNome, String pacienteNome)
    {
        this.tipoEvento = tipoEvento;
        this.consulta = consulta;
        this.pacienteEmail = pacienteEmail;
        this.pacienteNome = pacienteNome;
        this.medicoNome = medicoNome;
        this.timestampEvento = LocalDateTime.now();
    }
}