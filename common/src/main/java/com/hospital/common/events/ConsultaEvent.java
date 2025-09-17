package com.hospital.common.events;

import com.hospital.common.dto.ConsultaDTO;
import java.time.LocalDateTime;

public class ConsultaEvent {
    private String tipoEvento; // CRIADA, EDITADA, CANCELADA
    private ConsultaDTO consulta;
    private String pacienteEmail;
    private String pacienteNome;
    private LocalDateTime timestampEvento;
    
    // Construtores
    public ConsultaEvent() {}
    
    public ConsultaEvent(String tipoEvento, ConsultaDTO consulta, 
                        String pacienteEmail, String pacienteNome) {
        this.tipoEvento = tipoEvento;
        this.consulta = consulta;
        this.pacienteEmail = pacienteEmail;
        this.pacienteNome = pacienteNome;
        this.timestampEvento = LocalDateTime.now();
    }
}